#include "common.h"
#include "diff_top.h"

#include <fcntl.h>
#include <poll.h>
#include <signal.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <unistd.h>

#include <thread>

/* clang-format off */
#define GPRS(X) \
  X(0)  X(1)  X(2)  X(3)  X(4)  X(5)  X(6)  X(7)  \
  X(8)  X(9)  X(10) X(11) X(12) X(13) X(14) X(15) \
  X(16) X(17) X(18) X(19) X(20) X(21) X(22) X(23) \
  X(24) X(25) X(26) X(27) X(28) X(29) X(30) X(31)
/* clang-format on */

bool DiffTop::check_states() {
  mips_instr_t instr = napi_get_instr();
  if (instr.is_syscall() || instr.is_eret()) return true;

#define check_eq(a, b, ...) \
  if ((a) != (b)) {         \
    napi_dump_states();     \
    eprintf(__VA_ARGS__);   \
    return false;           \
  }

  check_eq(napi_get_pc(), dut_ptr->io_commit_pc,
      "cycle %lu: pc: nemu:%08x <> dut:%08x\n", noop_cycles,
      napi_get_pc(), dut_ptr->io_commit_pc);
  check_eq(napi_get_instr(), dut_ptr->io_commit_instr,
      "cycle %lu: instr: nemu:%08x <> dut:%08x\n",
      noop_cycles, napi_get_instr(),
      dut_ptr->io_commit_instr);

  if (last_instr_is_store) {
    uint32_t nemu_ls_data = napi_mmio_peek(ls_addr, 4);
    check_eq(nemu_ls_data, ls_data,
        "cycle %lu: M[%08x]: nemu:%08x <> dut:%08x\n",
        noop_cycles, ls_addr, nemu_ls_data, ls_data);
  }

#define GPR_TEST(i)                                     \
  check_eq(napi_get_gpr(i), dut_ptr->io_commit_gpr_##i, \
      "cycle %lu, pc %08x, instr %08x: gpr[%d]: "       \
      "nemu:%08x <> dut:%08x\n",                        \
      noop_cycles, napi_get_pc(), napi_get_instr(), i,  \
      napi_get_gpr(i), dut_ptr->io_commit_gpr_##i);
  GPRS(GPR_TEST);
#undef GPR_TEST
  return true;
}

uint32_t DiffTop::get_dut_gpr(uint32_t r) {
  switch (r) {
#define GET_GPR(i) \
  case i: return dut_ptr->io_commit_gpr_##i;
    GPRS(GET_GPR);
#undef GET_GPR
  }
  return 0;
}

bool DiffTop::can_log_now() const { return false; }

void DiffTop::init_from_args(int argc, const char *argv[]) {
  const char *napi_args[4] = {"nemu", "-b", "-e", nullptr};

  for (int i = 1; i < argc; i++) {
    if (strcmp(argv[i], "-e") == 0 && i + 1 < argc)
      napi_args[3] = argv[(i++) + 1];
    else if (strcmp(argv[i], "-b") == 0)
      /* do nothing */;
    else {
      eprintf("unknown args '%s'\n", argv[i]);
      exit(0);
    }
  }

  if (!napi_args[3]) {
    eprintf("need '-e <elf>' as options\n");
    exit(0);
  }

  napi_init(4, napi_args);
}

// argv decay to the secondary pointer
DiffTop::DiffTop(int argc, const char *argv[]) {
  /* `soc_emu_top' must be created before srand */
  dut_ptr.reset(new verilator_top);

  /* srand */
  uint32_t seed = (unsigned)time(NULL) ^ (unsigned)getpid();
  ;
  srand(seed);
  srand48(seed);
  Verilated::randReset(seed);

  /* init nemu */
  init_from_args(argc, argv);

  /* init ddr */
  void *nemu_ddr_map = napi_map_dev("ddr", 0, ddr_size);
  memcpy(ddr, nemu_ddr_map, ddr_size);

  /* reset n noop_cycles */
  noop_reset_ncycles(10);

  /* print seed */
  printf(ESC_BLUE "seed %u" ESC_RST "\n", seed);
}

void DiffTop::noop_reset_ncycles(unsigned n) {
  for (int i = 0; i < n; i++) {
    dut_ptr->reset = 1;
    run_noop_one_cycle();
    dut_ptr->reset = 0;
  }

  this->noop_cycles = 0;
}

void DiffTop::run_nemu_one_instr() {
  /* launch timer interrupt */
  napi_set_irq(7, dut_ptr->io_commit_ip7);
  /* nemu executes one cycle */
  napi_exec(1);
}

void DiffTop::noop_tame_nemu() {
  /* keep consistency when execute mfc0 count */
  mips_instr_t instr = napi_get_instr();
  if (instr.is_mfc0_count()) {
    uint32_t r = instr.get_rt();
    uint32_t count0 = get_dut_gpr(r);
    napi_set_gpr(r, count0);
  }
}

bool DiffTop::run_noop_one_cycle() {
  dut_ptr->io_can_log_now = can_log_now();

  dut_ptr->clock = 0;
  dut_ptr->eval();

  dut_ptr->clock = 1;
  dut_ptr->eval();
  noop_cycles++;
  return dut_ptr->io_commit_valid;
}

void DiffTop::run_noop_one_instr() {
  while (!run_noop_one_cycle())
    ;
}

bool DiffTop::run_diff_one_instr() {
  bool chkflag = true;
  run_noop_one_instr();
  if (noop_state == NS_Trap) return true;

  run_nemu_one_instr();
  noop_tame_nemu();
  chkflag = check_states();

  if (!chkflag) noop_state = NS_Chkfail;

  noop_ninstr++;
  return chkflag;
}

int DiffTop::execute() {
  noop_state = NS_Running;
  while (noop_state == NS_Running) {
    bool chk = run_diff_one_instr();
    if (!chk) abort();
  }
  return noop_trap_code;
}

void DiffTop::device_io(int addr, int len, int data,
    char func, char strb, int *resp) {
  assert(func == MX_RD || func == MX_WR);
  assert((addr & 3) == 0);

  /* mmio */
  if (!(0 <= addr && addr < 0x08000000)) {
    /* deal with dev_io */
    if (func == MX_RD) {
      if (napi_addr_is_valid(addr)) {
        *resp = napi_mmio_peek(addr, len + 1);
      } else {
        napi_dump_states();
        eprintf(
            "bad addr 0x%08x received from SOC\n", addr);
        abort();
      }
    } else {
      if (addr == GPIO_TRAP) {
        update_noop_state(NS_Trap);
        noop_trap_code = data;
      } else if (addr == ULITE_BASE + ULITE_Tx) {
      }
    }
    return;
  }

  assert(0 <= addr && addr < 0x08000000);
  /* ddr io */
  if (func == MX_RD) {
    // MX_RD
    memcpy(resp, &ddr[addr], 4);
  } else {
    // MX_WR
    addr = addr & ~3;
    for (int i = 0; i < 4; i++) {
      if (strb & (1 << i))
        ddr[addr + i] = (data >> (i * 8)) & 0xFF;
    }

    last_instr_is_store = true;
    ls_addr = addr & ~3;
    memcpy(&ls_data, &ddr[ls_addr], 4);
  }
}
