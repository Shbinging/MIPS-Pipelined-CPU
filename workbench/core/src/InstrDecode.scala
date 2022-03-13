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


    val instr_op = io.in.instr(31, 26)
    val funct = io.in.instr(5, 0)
    val rt = io.in.instr(20, 16)
    val rd = io.in.instr(15, 11)
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
    io.out_isu <> DontCare  
    io.out_isu.id_commit := if_id_reg.if_commit
    io.out_isu.imm := io.in.instr(15, 0)
    io.out_isu.shamt := io.in.instr(10, 6)
    

    // fill in io.out_isu
    when(io.in.if_commit){
        // TODO: decode the instr here
        when(io.in.instr===LUI){    
            io.out_isu.rd_addr := rt
            io.out_isu.exu := ALU_ID
            io.out_isu.op := ALU_X_OP   // FIXME: ALU_LUI_OP
            io.out_isu.shamt_rs_sel := true.B   // false: shamt | true: rs
            io.out_isu.imm_rt_sel := false.B
        }.elsewhen(io.in.instr===ADD){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_ADDU_OP
            io.out_isu.shamt_rs_sel := true.B   
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===ADDU){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_ADDU_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SUB){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SUBU_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SUBU){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SUBU_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SLT){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SLT_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SLTU){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SLTU_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===AND){ 
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_AND_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===OR){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_OR_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===XOR){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_XOR_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===NOR){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_X2_OP     // FIXME: NOR
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SLTI){
            io.out_isu.rd_addr := rt
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SLT_OP
            io.out_isu.sign_ext := true.B
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := false.B
        }.elsewhen(io.in.instr===SLTIU){
            io.out_isu.rd_addr := rt
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SLT_OP
            io.out_isu.sign_ext := true.B
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := false.B
        }.elsewhen(io.in.instr===SRA){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SRA_OP
            io.out_isu.shamt_rs_sel := false.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SRL){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SRL_OP
            io.out_isu.shamt_rs_sel := false.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SLL){ 
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SLL_OP
            io.out_isu.shamt_rs_sel := false.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SRAV){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SRA_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SRLV){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SRL_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }.elsewhen(io.in.instr===SLLV){
            io.out_isu.rd_addr := rd
            io.out_isu.exu := ALU_ID
            io.out_isu.op :=  ALU_SLL_OP
            io.out_isu.shamt_rs_sel := true.B  
            io.out_isu.imm_rt_sel := true.B
        }
    } .otherwise{
        // TODO
    }

    io.out_gpr_read := io.in.instr(25, 16).asTypeOf(new GPRReadIntput)
}