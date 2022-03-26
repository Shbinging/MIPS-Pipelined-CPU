package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class WriteBack extends Module{
    val io = IO(new Bundle{
        val alu_wb = Flipped(Decoupled(new ALU_WB))
        val bru_wb = Flipped(DecoupledIO(new BRU_WB))
        val wb_if = Decoupled(new RB_IF)    
        val gpr_wr = Flipped(new GPRWriteInput)
    })
    io.alu_wb.ready := true.B
    io.bru_wb.ready := true.B
    val alu_wb_fire = RegNext(io.alu_wb.fire())
    val bru_wb_fire = RegNext(io.bru_wb.fire())
    val reg_alu_wb = RegEnableUse(io.alu_wb.bits, io.alu_wb.fire())
    val reg_bru_wb = RegEnable(io.bru_wb.bits, io.bru_wb.fire())
    printf(p"wb working\n\n")

    io.gpr_wr <> DontCare
    io.gpr_wr.w_en := 0.U
    
    // no branch
    io.wb_if.bits.pc_w_data <> DontCare
    io.wb_if.bits.pc_w_en := false.B

    when(alu_wb_fire){
        io.gpr_wr.addr := reg_alu_wb.w_addr
        io.gpr_wr.data := reg_alu_wb.ALU_out
        io.gpr_wr.w_en := "b1111".U
    }.elsewhen(bru_wb_fire){
        io.gpr_wr.addr := reg_bru_wb.w_addr
        io.gpr_wr.data := reg_bru_wb.w_data
        io.gpr_wr.w_en := Mux(reg_bru_wb.w_en, "b1111".U, 0.U)
        io.wb_if.bits.pc_w_data := reg_bru_wb.w_pc_addr
        io.wb_if.bits.pc_w_en := reg_bru_wb.w_pc_en
    }
    
    io.wb_if.valid := (alu_wb_fire | bru_wb_fire) & ~reset.asBool()
    //  when(io.wb_if.valid){
    //     printf(p"${reg_exec_wb}")
    // }
}