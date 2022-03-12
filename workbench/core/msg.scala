package njumips
package core

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class Instr extends Bundle {
  val op     = UInt(6.W)
  val rs_idx = UInt(REG_SZ.W)
  val rt_idx = UInt(REG_SZ.W)
  val rd_idx = UInt(REG_SZ.W)
  val shamt  = UInt(5.W)
  val func   = UInt(6.W)

  def imm    = Cat(rd_idx, shamt, func)
  def addr   = Cat(rs_idx, rt_idx, imm)
}

class CacheControl extends Bundle {
  val op = UInt(CACHE_OP_SZ.W)
  val addr = UInt(32.W)
}

class CP0Exception extends Bundle {
  val et = UInt(ET_WIDTH.W)
  val code = UInt(EC_WIDTH.W)
}

class RegFileIO extends Bundle {
  val rs_idx = Output(UInt(REG_SZ.W))
  val rt_idx = Output(UInt(REG_SZ.W))
  val wen = Output(Bool())
  val wid = Output(UInt(conf.INSTR_ID_SZ.W))
  val rd_idx = Output(UInt(REG_SZ.W))

  val rs_data = Flipped(ValidIO(Output(UInt(conf.xprlen.W))))
  val rt_data = Flipped(ValidIO(Output(UInt(conf.xprlen.W))))
}

class TLBReq extends Bundle {
  val func = Output(Bool()) // 1: load, 0:store
  val vaddr = Output(UInt(conf.xprlen.W))
}

class TLBResp extends Bundle {
  val paddr = Output(UInt(conf.xprlen.W))
  val is_cached = Output(Bool())
  val ex = Output(new CP0Exception)
}

class TLBTransaction extends Bundle {
  // when tlbx is executing, the req should be hanged
  val req = DecoupledIO(new TLBReq)
  val resp = Flipped(DecoupledIO(new TLBResp))
}

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

class FlushIO extends Bundle {
  val br_target = Output(UInt(conf.xprlen.W))
}

class BypassIO extends Bundle {
  val v = Output(Bool())
  val rd_idx = Output(UInt(REG_SZ.W))
  val wen = Output(Bool())
  val data = Output(UInt(conf.xprlen.W))
}

class WriteBackIO extends Bundle {
  val v = Output(Bool())
  val id = Output(UInt(conf.INSTR_ID_SZ.W))
  val pc = Output(UInt(conf.xprlen.W))
  val instr = Output(new Instr)
  val rd_idx = Output(UInt(REG_SZ.W))
  val wen = Output(Bool())
  val data = Output(UInt(conf.xprlen.W))
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

class NSCSCCCommitIO extends Bundle {
  val ninstr = Output(UInt(32.W))
  val wb_pc = Output(UInt(conf.xprlen.W))
  val wb_instr = Output(UInt(conf.xprlen.W))
  val wb_rf_wdata = Output(UInt(conf.xprlen.W))
  val wb_rf_wen = Output(Bool())
  val wb_rf_wnum = Output(UInt(5.W))
  val wb_valid = Output(Bool())
}
