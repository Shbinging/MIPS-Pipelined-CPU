package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

// PC
class PCInput extends Bundle{
    val w_en = Input(Bool())
    val w_data = Input(UInt(conf.addr_width.W))
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

// IF_ID
class IF_ID extends Bundle{
    val if_commit = Output(Bool())
    val instr = Output(UInt(conf.data_width.W))
}

// ID_RF, RF_ISU, ID_ISU
class ID_ISU extends Bundle{
    val id_commit = Output(Bool())
    val rd_addr = Output(UInt(REG_SZ.W))
    val imm = Output(UInt(IMM_SZ.W))
    
    val shamt_rs_sel = Output(Bool())
    val shamt = Output(UInt(SHAMT_SZ.W))
    
    val sign_ext = Output(Bool())
    val exu = Output(UInt(EX_ID_WIDTH.W))
    val op = Output(UInt(OPCODE_WIDTH.W))
    val imm_rt_sel = Output(Bool())
}

// ISU_ALU
class ISU_ALU extends Bundle{
    val isu_commit_to_alu = Output(Bool())
    val alu_op = Output(UInt(OPCODE_WIDTH.W))
    val operand_1 = Output(UInt(conf.data_width.W))
    val operand_2 = Output(UInt(conf.data_width.W))
    val rd_addr = Output(UInt(REG_SZ.W))
}

// EXEC
class EXEC_WB extends Bundle{
    val exec_commit = Output(Bool())
    val w_en = Output(Bool())
    val w_addr = Output(UInt(REG_SZ.W))
}

class ALUOutput extends Bundle{
    // TODO:
    val ALU_out = Output(UInt(32.W))
    val Less = Output(Bool())
    val Overflow_out = Output(Bool())
    val Zero = Output(Bool())
}

// read, write memory
class MemReq extends Bundle {
  val is_cached = Output(Bool())
  val is_aligned = Output(Bool())
  val addr = Output(UInt(conf.xprlen.W))
  val len = Output(UInt(ML_SZ.W))              // aligned
  val strb = Output(UInt((conf.xprlen / 8).W)) // unaligned
  val data  = Output(UInt(conf.xprlen.W))
  val func  = Output(UInt(MX_SZ.W))
}

class MemResp extends Bundle {
  val data = Output(UInt(conf.xprlen.W))
}

class MemIO extends Bundle {
  val req = DecoupledIO(new MemReq)
  val resp = Flipped(DecoupledIO(new MemResp))
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
}
