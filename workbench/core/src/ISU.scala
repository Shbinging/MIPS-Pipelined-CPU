package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class ISU extends Module {
    val io = IO(new Bundle{
        val gpr_data = Flipped(new GPRReadOutput) //input
        val id_isu = Flipped(Decoupled(new ID_ISU)) //input
        val flush = Input(Bool())

        val isu_alu = Decoupled(new ISU_ALU) //output
        val isu_bru = Decoupled(new ISU_BRU)
        val isu_lsu = Decoupled(new ISU_LSU)
        val isu_mdu = Decoupled(new ISU_MDU)
    })
    val reg_id_isu = RegEnable(next=io.id_isu.bits, enable=io.id_isu.fire())
    val reg_id_isu_prepared = RegInit(false.B)
    
    // XXX: OoO not allowed
    io.id_isu.ready := (io.isu_alu.ready && io.isu_bru.ready && io.isu_lsu.ready && io.isu_mdu.ready) || !reg_id_isu_prepared 

    io.isu_alu <> DontCare
    io.isu_alu.valid := false.B
    io.isu_bru <> DontCare
    io.isu_bru.valid := false.B
    io.isu_lsu <> DontCare
    io.isu_lsu.valid := false.B
    io.isu_mdu <> DontCare
    io.isu_mdu.valid := false.B
    switch(reg_id_isu.exu){
        is(ALU_ID){
            val iaBundle = Wire(new ISU_ALU)
            iaBundle := DontCare
            iaBundle.imm := reg_id_isu.imm
            iaBundle.operand_1 := Mux(reg_id_isu.shamt_rs_sel, io.gpr_data.rs_data, reg_id_isu.shamt)
            iaBundle.alu_op := reg_id_isu.op
            iaBundle.rd_addr := reg_id_isu.rd_addr
            iaBundle.current_instr := reg_id_isu.current_instr
            iaBundle.current_pc := reg_id_isu.pcNext-4.U
            io.isu_alu.valid := reg_id_isu_prepared
            io.isu_alu.bits.alu_op := reg_id_isu.op
            switch(reg_id_isu.imm_rt_sel){
                is(false.B){//imm
                    when(reg_id_isu.sign_ext){
                        iaBundle.operand_2 := Cat(Fill((16), reg_id_isu.imm(15)), reg_id_isu.imm).asUInt()
                    }.otherwise{
                        iaBundle.operand_2 := Cat(Fill((16), 0.U), reg_id_isu.imm).asUInt()
                    }
                }
                is (true.B){//reg
                    iaBundle.operand_2 := io.gpr_data.rt_data
                }
            }
            io.isu_alu.bits <> iaBundle
        }
        is(BRU_ID){
            val bruBundle = Wire(new ISU_BRU)
            bruBundle.bru_op := reg_id_isu.op
            bruBundle.offset := reg_id_isu.imm
            bruBundle.rd := reg_id_isu.rd_addr
            bruBundle.instr_index := reg_id_isu.instr_index
            bruBundle.rsData := io.gpr_data.rs_data //XXX:need handshake
            bruBundle.rtData := io.gpr_data.rt_data
            bruBundle.pcNext := reg_id_isu.pcNext
            bruBundle.current_instr := reg_id_isu.current_instr
            bruBundle.current_pc := reg_id_isu.pcNext - 4.U
            io.isu_bru.valid := reg_id_isu_prepared
            io.isu_bru.bits <> bruBundle
        }
        is(LSU_ID){
            io.isu_lsu.valid := reg_id_isu_prepared
            io.isu_lsu.bits.imm := reg_id_isu.imm
            io.isu_lsu.bits.rsData := io.gpr_data.rs_data
            io.isu_lsu.bits.rtData := io.gpr_data.rt_data
            io.isu_lsu.bits.rt := reg_id_isu.rd_addr
            io.isu_lsu.bits.lsu_op := reg_id_isu.op

            io.isu_lsu.bits.current_instr := reg_id_isu.current_instr
            io.isu_lsu.bits.current_pc := reg_id_isu.pcNext - 4.U
        }
        is(MDU_ID){
            io.isu_mdu.valid := reg_id_isu_prepared
            io.isu_mdu.bits.mdu_op := reg_id_isu.op
            io.isu_mdu.bits.rsData := io.gpr_data.rs_data
            io.isu_mdu.bits.rtData := io.gpr_data.rt_data
            io.isu_mdu.bits.rd := reg_id_isu.rd_addr

            io.isu_lsu.bits.current_instr := reg_id_isu.current_instr
            io.isu_lsu.bits.current_pc := reg_id_isu.pcNext - 4.U
        }
    }

    when(io.flush || (!io.id_isu.fire() && (io.isu_alu.fire() || io.isu_bru.fire() || io.isu_mdu.fire() || io.isu_lsu.fire()))){
        reg_id_isu_prepared := false.B
    } .elsewhen(!io.flush && io.id_isu.fire()){
        reg_id_isu_prepared := true.B
    }

}