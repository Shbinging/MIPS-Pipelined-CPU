package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import src.RegEnableUse

class InstrDecode extends Module{
    val io = IO(new Bundle{
        val in = Flipped(new IF_ID)
        val out_gpr_read = Flipped(new GPRReadIntput)
        val out_isu = new ID_ISU
    })

    val if_id_reg = RegEnableUse(io.in, io.in.if_commit)


    val instr_op = io.in.instr(31, 25)
    val funct = io.in.instr(5, 0)

    /*
        val id_commit = Output(Bool())
        val rd_addr = Output(UInt(REG_SZ.W))
        val imm = Output(UInt(IMM_SZ.W))
        val sign_ext = Output(Bool())
        val exu = Output(UInt(EX_ID_WIDTH.W))
        val op = Output(UInt(OPCODE_WIDTH.W))
        val imm_rt_sel = Output(Bool())
    */
    io.out_isu.id_commit := if_id_reg.if_commit
    
    // fill in io.out_isu
    when(io.in.if_commit){
        // TODO: decode the instr here
        when(io.in.instr===LUI){    

        }.elsewhen(io.in.instr===ADD){
        }.elsewhen(io.in.instr===ADDU){
        }.elsewhen(io.in.instr===SUB){
        }.elsewhen(io.in.instr===SUBU){
        }.elsewhen(io.in.instr===SLT){
        }.elsewhen(io.in.instr===SLTU){
        }.elsewhen(io.in.instr===AND){ 
        }.elsewhen(io.in.instr===OR){
        }.elsewhen(io.in.instr===XOR){
        }.elsewhen(io.in.instr===NOR){
        }.elsewhen(io.in.instr===SLTI){
        }.elsewhen(io.in.instr===SLTIU){
        }.elsewhen(io.in.instr===SRA){
        }.elsewhen(io.in.instr===SRL){
        }.elsewhen(io.in.instr===SLL){
        }.elsewhen(io.in.instr===SRAV){
        }.elsewhen(io.in.instr===SRLV){
        }.elsewhen(io.in.instr===SLLV){
        }
    } .otherwise{
        // TODO
    }

    io.out_gpr_read := io.in.instr(25, 16).asTypeOf(new GPRReadIntput)
}