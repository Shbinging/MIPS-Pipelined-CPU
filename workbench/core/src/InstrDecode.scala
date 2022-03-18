package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class InstrDecode extends Module{
    val io = IO(new Bundle{
        val if_id = Flipped(Decoupled(new IF_ID))
        val out_gpr_read = Flipped(new GPRReadIntput)
        val id_isu = Decoupled(new ID_ISU)
    })
    io.if_id.ready := true.B   // unidir handshake 
    val if_id_fire = RegNext(io.if_id.fire())
    val if_id_reg = RegEnableUse(io.if_id.bits, io.if_id.fire())


    val instr_op = if_id_reg.instr(31, 26)
    val funct = if_id_reg.instr(5, 0)
    val rt = if_id_reg.instr(20, 16)
    val rd = if_id_reg.instr(15, 11)
    /*
        val id_commit = Output(Bool())  *
        val rd_addr = Output(UInt(REG_SZ.W))
        val imm = Output(UInt(IMM_SZ.W)) *
    
        val shamt_rs_sel = Output(Bool())
        val shamt = Output(UInt(SHAMT_SZ.W)) *
    
        val sign_ext = Output(Bool())
        val exu = Output(UInt(EX_ID_WIDTH.W))
        val op = Output(UInt(OPCODE_WIDTH.W))
        val imm_rt_sel = Output(Bool())
    */
    io.id_isu <> DontCare  
    io.id_isu.bits.imm := if_id_reg.instr(15, 0)
    io.id_isu.bits.shamt := if_id_reg.instr(10, 6)
    
    // TODO: decode instructions
    

    io.id_isu.valid := if_id_fire    // complete in 1 cycle
    io.out_gpr_read := if_id_reg.instr(25, 16).asTypeOf(new GPRReadIntput)
}