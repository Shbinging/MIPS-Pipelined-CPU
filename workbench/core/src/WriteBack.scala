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
        val rb_if = Decouple(new RB_IF)    // XXX: ignore temporarily
        val gpr_wr = Flipped(new GPRWriteInput)
    })
   
    // TODO:
    val exec_wb_fire = RegNext(io.exec_wb.fire())
    val reg_alu_output = RegEnableUse(io.alu_output, io.exec_wb.fire() && (io.exec_wb.exu_id===ALU_ID))
    val reg_exec_wb = RegEnableUse(io.exec_wb.bits, io.exec_wb.fire())

    io.gpr_wr <> DontCare
    io.gpr_wr.w_en := 0.U
    
    // no branch
    io.rb_if.pc_w_data <> DontCare
    io.rb_if.pc_w_en := false.B
    

    when(!reg_alu_output.Overflow_out){
        io.gpr_wr.addr := reg_exec_wb.w_addr
        io.gpr_wr.data := reg_alu_output.ALU_out
        io.gpr_wr.w_en := 1.U
    }
    
    io.rb_if.valid := exec_wb_fire

    withReset(reset.asBool()){
        io.cycle_done := true.B
    } 
}