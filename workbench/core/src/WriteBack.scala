package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class WriteBack extends Module{
    val io = IO(new Bundle{
        val alu_wb = Flipped(Decoupled(new ALU_WB))
        val bru_wb = Flipped(Decoupled(new BRU_WB))
        val lsu_wb = Flipped(Decoupled(new LSU_WB))
        val mdu_wb = Flipped(Decoupled(new MDU_WB))
        val wb_if = Decoupled(new RB_IF)
        val commit = Output(new WB_COMMMIT)
        val flush = Output(Bool())
        val gpr_wr = Flipped(new GPRWriteInput)
    })
    io.alu_wb.ready := true.B
    io.bru_wb.ready := true.B
    io.lsu_wb.ready := true.B
    io.mdu_wb.ready := true.B
    val alu_wb_fire = RegNext(io.alu_wb.fire())
    val bru_wb_fire = RegNext(false.B)
    val lsu_wb_fire = RegNext(io.lsu_wb.fire())
    val mdu_wb_fire = RegNext(io.mdu_wb.fire()) // XXX: one cycle assumption
    val reg_alu_wb = RegEnable(io.alu_wb.bits, io.alu_wb.fire())
    val reg_bru_wb = RegEnable(io.bru_wb.bits, io.bru_wb.fire())
    val reg_lsu_wb = RegEnable(io.lsu_wb.bits, io.lsu_wb.fire())
    val reg_mdu_wb = RegEnable(io.mdu_wb.bits, io.mdu_wb.fire())
    // printf(p"wb working\n\n")

    io.gpr_wr <> DontCare
    io.gpr_wr.w_en := 0.U
    
    val delay_slot = RegEnable(bru_wb_fire & reg_bru_wb.w_pc_en, io.commit.commit)
    io.flush := delay_slot  // 
    printf(p"flush: ${io.flush}\n")
    // no branch
    io.wb_if.bits.pc_w_data <> DontCare
    io.wb_if.bits.pc_w_en := false.B
    io.commit <> DontCare
    io.commit.commit := false.B
    when(alu_wb_fire){
        io.gpr_wr.addr := reg_alu_wb.w_addr
        io.gpr_wr.data := reg_alu_wb.ALU_out
        io.gpr_wr.w_en := "b1111".U
        io.commit.commit_pc := reg_alu_wb.current_pc
        io.commit.commit_instr := reg_alu_wb.current_instr
        io.commit.commit := true.B
    }.elsewhen(bru_wb_fire){
        io.gpr_wr.addr := reg_bru_wb.w_addr
        io.gpr_wr.data := reg_bru_wb.w_data
        io.gpr_wr.w_en := Mux(reg_bru_wb.w_en, "b1111".U, 0.U)
        io.wb_if.bits.pc_w_data := reg_bru_wb.w_pc_addr
        io.wb_if.bits.pc_w_en := reg_bru_wb.w_pc_en
        io.commit.commit_pc := reg_bru_wb.current_pc
        io.commit.commit_instr := reg_bru_wb.current_instr
        io.commit.commit := io.wb_if.fire()
    }.elsewhen(lsu_wb_fire){
        io.gpr_wr.addr := reg_lsu_wb.w_addr
        io.gpr_wr.data := reg_lsu_wb.w_data
        io.gpr_wr.w_en := reg_lsu_wb.w_en // Mux(reg_lsu_wb.w_en, "b1111".U, 0.U)
        io.wb_if.bits.pc_w_en := false.B
        io.commit.commit_pc := reg_lsu_wb.current_pc
        io.commit.commit_instr := reg_lsu_wb.current_instr
        io.commit.commit := true.B
        // io.wb_if.bits.pc_w_en := 0.U
    }.elsewhen(mdu_wb_fire){
        io.gpr_wr.addr := reg_mdu_wb.w_addr
        io.gpr_wr.data := reg_mdu_wb.w_data
        io.gpr_wr.w_en := Mux(reg_mdu_wb.w_en, "b1111".U, 0.U)
        //printf(p"w_en: ${io.gpr_wr.w_en} w_data: ${io.gpr_wr.data}\n")
        io.wb_if.bits.pc_w_data := false.B
        io.commit.commit_pc := reg_mdu_wb.current_pc
        io.commit.commit_instr := reg_mdu_wb.current_instr
        io.commit.commit := true.B
    }

    when(delay_slot || (!io.bru_wb.fire() && io.wb_if.fire())){
        bru_wb_fire := false.B
    }.elsewhen(!delay_slot && io.bru_wb.fire()){
        bru_wb_fire := true.B
    }
    io.wb_if.valid := bru_wb_fire
    //  when(io.wb_if.valid){
    //     printf(p"${reg_exec_wb}")
    // }
}