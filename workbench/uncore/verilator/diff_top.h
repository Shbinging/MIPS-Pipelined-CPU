#ifndef EMU_API_H
#define EMU_API_H

#include <cassert>
#include <cstdlib>
#include <ctime>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <utility>
#include <vector>

#include "verilator_top.h"
#include "verilator_top__Dpi.h"
extern "C" {
#include "napis.h"
}

#define MX_RD 0
#define MX_WR 1
#define GPIO_TRAP 0x10000000
#define ULITE_BASE 0x1fe50000
#define ULITE_Rx 0x0
#define ULITE_Tx 0x4
#define ULITE_STAT 0x8
#define ULITE_CTRL 0xC

class NEMU_MIPS32;
struct device_t;

// wrappers for nemu-mips32 library
class mips_instr_t {
  union {
    struct {
      uint32_t sel : 3;
      uint32_t __ : 8;
      uint32_t rd : 5;
      uint32_t rt : 5;
      uint32_t mf : 5;
      uint32_t cop0 : 6;
    };
    uint32_t val;
  };

public:
  mips_instr_t(uint32_t instr) : val(instr) {}

  bool is_mfc0_count() const {
    return cop0 == 0x10 && mf == 0x0 && rd == 0x9 &&
           sel == 0x0;
  }
  bool is_syscall() const { return val == 0x0000000c; }
  bool is_eret() const { return val == 0x42000018; }
  uint32_t get_rt() { return rt; }
};

class DiffTop {
private:
  std::unique_ptr<verilator_top> dut_ptr;

public:
  int64_t noop_ninstr = 0;
  int64_t noop_cycles = 0;
  int noop_trap_code = 0;

  enum noop_state_t {
    NS_Running,
    NS_Trap,
    NS_Chkfail,
  } noop_state;

  void update_noop_state(noop_state_t new_state) {
    assert(noop_state == NS_Running);
    noop_state = new_state;
  }

  /* lsu check */
  bool last_instr_is_store = false;
  uint32_t ls_addr = 0, ls_data = 0;

  static constexpr uint32_t ddr_size = 128 * 1024 * 1024;
  uint8_t ddr[ddr_size];

  bool check_states();
  uint32_t get_dut_gpr(uint32_t r);

  void noop_reset_ncycles(unsigned n);
  void noop_tame_nemu();
  bool run_noop_one_cycle();
  void run_noop_one_instr();
  void run_nemu_one_instr();
  bool run_diff_one_instr();

  bool can_log_now() const;

  void init_from_args(int argc, const char *argv[]);

public:
  // argv decay to the secondary pointer
  DiffTop(int argc, const char *argv[]);
  virtual ~DiffTop() = default;

  int execute();
  void device_io(int addr, int len, int data, char func,
      char wstrb, int *resp);
};

#endif
