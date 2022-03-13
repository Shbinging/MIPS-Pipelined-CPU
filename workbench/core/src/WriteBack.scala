package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import src.RegEnableUse

class WriteBack extends Module{
    val io = IO(new Bundle{
        val exec_wb = Flipped(new EXEC_WB)
        val alu_output = Flipped(new ALUOutput)
        val pc_wr = Flipped(new PCInput)    // XXX: ignore temporarily
        val gpr_wr = Flipped(new GPRWriteInput)
        val cycle_done = Output(Bool())
    })
   
    // TODO:
    val reg_alu_output = RegEnableUse(io.alu_output, io.exec_wb.exec_commit)
    val reg_exec_wb = RegEnableUse(io.exec_wb, io.exec_wb.exec_commit)

    io.gpr_wr <> DontCare
    io.gpr_wr.w_en := 0.U
    io.cycle_done := false.B
    io.pc_wr <> DontCare
    io.pc_wr.w_en := false.B
    

    when(reg_exec_wb.exec_commit){
        when(!reg_alu_output.Overflow_out){
            io.gpr_wr.addr := reg_exec_wb.w_addr
            io.gpr_wr.data := reg_alu_output.ALU_out
            io.gpr_wr.w_en := 1.U
        }
        io.cycle_done := true.B
    }
    
    withReset(reset.asBool()){
        io.cycle_done := true.B
    } 
}