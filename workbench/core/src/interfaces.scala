package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import java.util.spi.ResourceBundleProvider

// // PC
// class PCInput extends Bundle{
//     val w_en = Input(Bool())
//     val w_data = Input(UInt(conf.addr_width.W))
// }

//Exception
class exceptionInfo extends Bundle{
    val enable = Bool()
    val EPC = UInt(32.W)
    val badVaddr = UInt(32.W)
    val exeCode = UInt(EC_WIDTH.W)
    val excType = UInt(ET_WIDTH.W)
}
// GPR
class GPRReadIntput extends Bundle{
    val rs_addr = Input(UInt(REG_SZ.W))
    val rt_addr = Input(UInt(REG_SZ.W))
}
class GPRReadOutput extends Bundle{
    val rs_data = Output(UInt(conf.data_width.W))
    val rt_data = Output(UInt(conf.data_width.W))
}
class GPRWriteInput extends Bundle{
    val w_en = Input(UInt((conf.data_width / 8).W))
    val addr = Input(UInt(REG_SZ.W))
    val data = Input(UInt(conf.data_width.W))
}
//CP0

// FIXME
// class CP0WriteInput extends Bundle{
//     val enableEXL = Input(Bool())
//     val enableOther = Input(Bool())
//     val enableVaddress = Input(Bool())
//     val BD = Input(UInt(1.W))
//     val EXL = Input(UInt(1.W))
//     val ExcCode = Input(UInt(5.W))
//     val epc = Input(UInt(32.W))
//     val vAddr = Input(UInt(32.W))
// }

class CP0WriteInput extends Bundle{
    val en = Input(Bool())
    val data = Input(UInt(32.W))
}

class CP0WriteInputWB extends Bundle{
    val enableEXL = Input(Bool())
    val enableOther = Input(Bool())
    val enableVaddress = Input(Bool())
    val BD = Input(UInt(1.W))
    val EXL = Input(UInt(1.W))
    val ExcCode = Input(UInt(5.W))
    val epc = Input(UInt(32.W))
    val vAddr = Input(UInt(32.W))
}
// WB_IF 
class RB_IF extends Bundle{
  val pc_w_en = Output(Bool())
  val pc_w_data = Output(UInt(conf.addr_width.W))
}


// IF_ID
class IF_ID extends Bundle{
    // val if_commit = Output(Bool())
    val instr = Output(UInt(conf.data_width.W))
    val pcNext = Output(UInt(conf.addr_width.W))
    val except_info = Output(new exceptionInfo)
}

// ID_RF, RF_ISU, ID_ISU
class ID_ISU extends Bundle{
    // val id_commit = Output(Bool())
    val exu = Output(UInt(EX_ID_WIDTH.W))
    //FIXME rs_addr rt_addr 表示该指令要取的寄存器地址，如果rs_addr rt_addr不需要寄存器就设为0
    val read1 = Output(UInt(REG_SZ.W))
    val read2 = Output(UInt(REG_SZ.W))
    val write = Output(UInt(REG_SZ.W))
    val isRead1CP0 = Output(Bool())
    val isRead2CP0 = Output(Bool())
    val isWriteCP0 = Output(Bool())
    
    val rd_addr = Output(UInt(REG_SZ.W))
    val imm = Output(UInt(IMM_SZ.W))
    
    val shamt_rs_sel = Output(Bool())
    val shamt = Output(UInt(SHAMT_SZ.W))
    
    val imm_rt_sel = Output(Bool())
    val sign_ext = Output(Bool())
    
    val op = Output(UInt(OPCODE_WIDTH.W))
    
    val instr_index = Output(UInt(26.W)) //J type
    
    // val branch_op= Output(UInt(OPCODE_WIDTH.W)) //see config, use oop
    val pcNext = Output(UInt(conf.addr_width.W)) //currentpc + 4
    val current_instr = Output(UInt(conf.data_width.W))

    val except_info = Output(new exceptionInfo)
}

// ISU_ALU
class ISU_ALU extends Bundle{
    // val isu_commit_to_alu = Output(Bool())
    val alu_op = Output(UInt(OPCODE_WIDTH.W))
    val operand_1 = Output(UInt(conf.data_width.W))
    val operand_2 = Output(UInt(conf.data_width.W))
    val imm = Output(UInt(IMM_SZ.W))
    val rd_addr = Output(UInt(REG_SZ.W))

    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
}

class ISU_BRU extends Bundle{
    val bru_op = Output(UInt(OPCODE_WIDTH.W))
    val pcNext = Output(UInt(conf.data_width.W))
    val offset = Output(UInt(IMM_SZ.W))
    val rd = Output(UInt(REG_SZ.W))
    val rsData = Output(UInt(conf.data_width.W))
    val rtData = Output(UInt(conf.data_width.W))
    val instr_index = Output(UInt(26.W))

    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
}

class ISU_LSU extends Bundle{
    val lsu_op = Output(UInt(OPCODE_WIDTH.W)) 
    val rt = Output(UInt(REG_SZ.W))
    val rsData = Output(UInt(conf.data_width.W))
    val rtData = Output(UInt(conf.data_width.W))
    val imm = Output(UInt(IMM_SZ.W))

    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
}

class ISU_MDU extends Bundle{
  val mdu_op = Output(UInt(OPCODE_WIDTH.W))
  val rsData = Output(UInt(conf.data_width.W))
  val rtData = Output(UInt(conf.data_width.W))
  val rd = Output(UInt(REG_SZ.W))

  val current_pc = Output(UInt(conf.addr_width.W))
  val current_instr = Output(UInt(conf.data_width.W))
}

class ISU_PRU extends Bundle{
    val pru_op = Output(UInt(4.W))
    val except_info = Output(new exceptionInfo)
    val rs_data = Output(UInt(conf.data_width.W))
    val rs_addr = Output(UInt(5.W))
    val rt_addr = Output(UInt(5.W))
    val rd_addr = Output(UInt(5.W))
    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
}

class PRU_WB_ERET extends Bundle{
    val en = Bool()
    val w_pc_addr = UInt()
}

class PRU_WB_MFT extends Bundle{
    val en = Bool()
    val destSel = Bool() //0 general 1 cp0
    val destAddr = UInt(5.W)
    val CP0Sel = UInt(3.W) //XXX:not use right now
    val data = UInt(32.W)
}

class PRU_WB_TLBP extends Bundle{
    val en = Bool()
    val index_data = UInt(conf.data_width.W)
}

class PRU_WB_TLBR extends Bundle{
  val en = Bool()
  val entryhi = new EntryHi
  val entrylo_0 = new EntryLo
  val entrylo_1 = new EntryLo
}


class PRU_WB extends Bundle{
    val error = Output(new exceptionInfo)
    val eret = Output(new PRU_WB_ERET)
    val mft = Output(new PRU_WB_MFT)
    val tlbp = Output(new PRU_WB_TLBP)
    val tlbr = Output(new PRU_WB_TLBR)
    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
    val needCommit = Output(Bool())
}
// EXEC
class ALU_WB extends Bundle{
    val w_en = Output(Bool())
    val w_addr = Output(UInt(REG_SZ.W))
    val ALU_out = Output(UInt(32.W))
    val Overflow_out = Output(Bool())
   // val exu_id = Output(UInt(EX_ID_WIDTH.W))
    
    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
    val error = Output(new exceptionInfo)
}

class ALU_PASS extends Bundle{
    val rm_dirty = Output(Bool())
    val w_en = Output(Bool())
    val w_addr = Output(UInt(REG_SZ.W))
    val ALU_out = Output(UInt(32.W))
}

class BRU_WB extends Bundle{
    val w_en = Output(Bool())
    val w_addr = Output(UInt(REG_SZ.W))
    val w_data = Output(UInt(conf.data_width.W))
    val w_pc_en = Output(Bool())
    val w_pc_addr = Output(UInt(conf.data_width.W))

    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
    val noSlot = Output(Bool())
}

class LSU_WB extends Bundle{
    val w_en = Output(UInt(4.W))
    val w_addr = Output(UInt(REG_SZ.W))
    val w_data = Output(UInt(conf.data_width.W))

    val current_pc = Output(UInt(conf.addr_width.W))
    val current_instr = Output(UInt(conf.data_width.W))
    val error = Output(new exceptionInfo)
}

class MDU_WB extends Bundle{
  val w_en = Output(Bool())
  val w_addr = Output(UInt(REG_SZ.W))
  val w_data = Output(UInt(conf.data_width.W))

  val current_pc = Output(UInt(conf.addr_width.W))
  val current_instr = Output(UInt(conf.data_width.W))
  val error = Output(new exceptionInfo)
}

class WB_COMMMIT extends Bundle{
  val commit = Output(Bool())
  val commit_pc = Output(UInt(conf.addr_width.W))
  val commit_instr = Output(UInt(conf.data_width.W))
}

class DividerIO extends Bundle {
  val data_dividend_valid = Output(Bool())
  val data_divisor_valid = Output(Bool())
  val data_dout_valid = Input(Bool())
  val data_dividend_ready = Input(Bool())
  val data_divisor_ready = Input(Bool())
  val data_dividend_bits = Output(UInt(40.W))
  val data_divisor_bits = Output(UInt(40.W))
  val data_dout_bits = Input(UInt(80.W))
}

class MultiplierIO extends Bundle {
  val data_a = Output(UInt(33.W))
  val data_b = Output(UInt(33.W))
  val data_dout = Input(UInt(66.W))
}

class TLBEntryIO extends Bundle{
    val en = Output(Bool())
    val index = Output(UInt(32.W))
    val hi = Output(new EntryHi)
    val lo_0 = Output(new EntryLo)
    val lo_1 = Output(new EntryLo)
}

class TLBTranslatorReq extends Bundle{
  val va = Input(UInt(conf.addr_width.W))
  val ref_type = Input(UInt(MX_SZ.W))
}

class TLBTranslatorResp extends Bundle{
  val pa = Output(UInt(conf.addr_width.W))
  val cached = Output(Bool())
  val exception = Output(UInt(ET_WIDTH.W))
}

// read, write memory
class MemReq extends Bundle {
  val is_cached = Output(Bool())
  // val is_aligned = Output(Bool())
  val addr = Output(UInt(conf.xprlen.W))
  val len = Output(UInt(ML_SZ.W))              // aligned
  val strb = Output(UInt((conf.xprlen / 8).W)) // unaligned
  val data  = Output(UInt(conf.xprlen.W))
  val func  = Output(UInt(MX_SZ.W))
}

class MemResp extends Bundle {
  val data = Output(UInt(conf.xprlen.W))
}

class CacheReq extends Bundle{
  val is_cached = Output(Bool())
  val addr = Output(UInt(conf.xprlen.W))
  val len = Output(UInt(ML_SZ.W))              // aligned
  val strb = Output(UInt((conf.xprlen / 8).W)) // unaligned
  val data  = Output(UInt(conf.xprlen.W))
  val func  = Output(UInt(MX_SZ.W))
  val exception = Output(UInt(ET_WIDTH.W))
}

class CacheResp extends Bundle{
  val data = Output(UInt(conf.xprlen.W))
  val exception = Output(UInt(ET_WIDTH.W))
}

class MemIO extends Bundle {
  val req = DecoupledIO(new MemReq)
  val resp = Flipped(DecoupledIO(new MemResp))
}

class CacheIO extends Bundle{
  val req = DecoupledIO(new CacheReq)
  val resp = Flipped(Decoupled(new CacheResp))
}

class CacheCommandIO extends Bundle{
  val en = Output(Bool())
  val addr = Output(UInt(conf.data_width.W))
  val code = Output(UInt(3.W))
}


class SimDev extends BlackBox {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(Bool())
    val in = Flipped(new MemIO)
  })
}

class CommitIO extends Bundle {
  val valid = Output(Bool())
  val pc = Output(UInt(conf.xprlen.W))
  val instr = Output(UInt(conf.xprlen.W))
  val ip7 = Output(Bool())
  val gpr = Output(Vec(32, UInt(conf.xprlen.W)))
  val rd_idx = Output(UInt(REG_SZ.W))
  val wdata = Output(UInt(conf.xprlen.W))
  val wen = Output(Bool())
  val cp0_count = Output(UInt(conf.xprlen.W))
  val w_pc = Output(UInt(32.W))
}

// CP 0 
class EntryHi extends Bundle{
    val vpn2 = UInt(19.W)
    val zero_padding = UInt(5.W)
    val asid = UInt(8.W)
}

class EntryLo extends Bundle{
    val zero_padding = UInt(2.W)
    val pfn = UInt(24.W)
    val coherence = UInt(3.W)
    val dirty = Bool()
    val valid = Bool()
    val global = Bool()
}

class cp0_Status_12 extends Bundle{
    val CU = UInt(4.W)
    val RP = UInt(1.W)
    val FR = UInt(1.W)
    val RE = UInt(1.W)
    val MX = UInt(1.W)
    val PX = UInt(1.W)
    val BEV = UInt(1.W)
    val TS = UInt(1.W)
    val SR = UInt(1.W)
    val NMI = UInt(1.W)
    val EMPTY1 = UInt(1.W)
    val Impl = UInt(2.W)
    val IM = UInt(8.W)
    val KX = UInt(1.W)
    val SX = UInt(1.W)
    val UX = UInt(1.W)
    val UM = UInt(1.W)
    val R0 = UInt(1.W)
    val ERL = UInt(1.W)
    val EXL = UInt(1.W)
    val IE = UInt(1.W)
}

class cp0_Cause_13 extends Bundle{
    val BD = UInt(1.W)
    val TI = UInt(1.W)
    val CE = UInt(2.W)
    val DC = UInt(1.W)
    val PCI = UInt(1.W)
    val Empty4 = UInt(2.W)
    val IV = UInt(1.W)
    val WP = UInt(1.W)
    val Empty3 = UInt(6.W)
    val IP = UInt(8.W)
    val Empty2 = UInt(1.W)
    val ExcCode = UInt(5.W)
    val Empty1 = UInt(2.W)
}


class cp0_BadVaddr_8 extends Bundle{
    val badAddr = UInt(conf.data_width.W)
}

class cp0_Epc_14 extends Bundle{
    val epc = UInt(conf.data_width.W)
}

class cp0_Config_16 extends Bundle{
    val K0  = UInt(3.W)
    val Empty1 = UInt(4.W)
    val MT = UInt(3.W)
    val AR = UInt(3.W)
    val AT = UInt(2.W)
    val BE = UInt(1.W)
    val Impl = UInt(15.W)
    val M = UInt(1.W)
}

class cp0_Context_4 extends Bundle{
    val PTEBase = UInt(9.W)
    val BadVPN2 = UInt(19.W)
    val Empty1 = UInt(4.W)
}


class MemIO2AXI(dw:Int) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(new MemIO)
    val out = new AXI4IO(dw)
  })

  val is_req_wr = io.in.req.valid && io.in.req.bits.func === MX_WR
  val aw_fire = RegInit(N)
  val w_fire = RegInit(N)
  val addr = io.in.req.bits.addr
  val alen = Mux1H(Array(
    (io.in.req.bits.len === ML_1) -> "b00".U,
    (io.in.req.bits.len === ML_2) -> "b01".U,
    (io.in.req.bits.len === ML_4) -> "b10".U))

  io.out.aw.valid := is_req_wr && !aw_fire
  io.out.aw.addr := addr
  io.out.aw.id := 0.U
  io.out.aw.len := 0.U
  io.out.aw.size := alen
  io.out.aw.burst := 0.U
  io.out.aw.lock := 0.U
  io.out.aw.cache := 0.U
  io.out.aw.prot := 0.U
  io.out.aw.region := 0.U
  io.out.aw.qos := 0.U
  io.out.aw.user := 0.U
  when (is_req_wr && io.out.aw.fire() && !io.out.w.fire()) {
    aw_fire := Y
  }

  io.out.w.valid := is_req_wr && !w_fire
  io.out.w.id := 0.U
  io.out.w.data := io.in.req.bits.data
  io.out.w.strb := io.in.req.bits.strb
  io.out.w.last := Y
  io.out.w.user := 0.U
  when (is_req_wr && !io.out.aw.fire() && io.out.w.fire()) {
    w_fire := Y
  }

  val req_w_ready = (io.out.aw.ready && w_fire) ||
    (aw_fire && io.out.w.ready) ||
    (io.out.aw.ready && io.out.w.ready)

  when (io.in.req.fire()) {
    aw_fire := N
    w_fire := N
  }

  io.out.ar.valid := io.in.req.valid && io.in.req.bits.func === MX_RD
  io.out.ar.addr := addr
  io.out.ar.id := 0.U
  io.out.ar.len := 0.U
  io.out.ar.size := alen
  io.out.ar.burst := 0.U
  io.out.ar.lock := 0.U
  io.out.ar.cache := 0.U
  io.out.ar.prot := 0.U
  io.out.ar.region := 0.U
  io.out.ar.qos := 0.U
  io.out.ar.user := 0.U

  io.out.r.ready := io.in.resp.ready
  io.out.b.ready := io.in.resp.ready

  io.in.req.ready := Mux(io.in.req.bits.func === MX_WR,
    req_w_ready, io.out.ar.ready)
  io.in.resp.valid := io.out.r.valid || io.out.b.valid
  io.in.resp.bits.data := io.out.r.data
}
