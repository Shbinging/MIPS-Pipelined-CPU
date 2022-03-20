package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class WriteBack extends Module{
    val io = IO(new Bundle{
        val exec_wb = Flipped(Decoupled(new EXEC_WB))
        val alu_output = Flipped(new ALUOutput)
        val wb_if = Decoupled(new RB_IF)    // XXX: ignore temporarily
        val gpr_wr = Flipped(new GPRWriteInput)
    })
    io.exec_wb.ready := true.B
    val exec_wb_fire = RegNext(io.exec_wb.fire())
    val reg_alu_output = RegEnableUse(io.alu_output, io.exec_wb.fire() && (io.exec_wb.bits.exu_id===ALU_ID))
    val reg_exec_wb = RegEnableUse(io.exec_wb.bits, io.exec_wb.fire())

    printf(p"wb working: ${exec_wb_fire}\n\n")

    io.gpr_wr <> DontCare
    io.gpr_wr.w_en := 0.U
    
    // no branch
    io.wb_if.bits.pc_w_data <> DontCare
    io.wb_if.bits.pc_w_en := false.B
    

    when(!reg_alu_output.Overflow_out){
        io.gpr_wr.addr := reg_exec_wb.w_addr
        io.gpr_wr.data := reg_alu_output.ALU_out
        io.gpr_wr.w_en := 1.U
    }
    
    io.wb_if.valid := exec_wb_fire & ~reset.asBool()
}