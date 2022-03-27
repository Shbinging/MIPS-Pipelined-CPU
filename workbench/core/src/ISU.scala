package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class ISU extends Module {
    val io = IO(new Bundle{
        val gpr_data = Flipped(new GPRReadOutput) //input
        val id_isu = Flipped(Decoupled(new ID_ISU)) //input
        
        val isu_alu = Decoupled(new ISU_ALU) //output
        val isu_bru = Decoupled(new ISU_BRU)
        val isu_lsu = Decoupled(new ISU_LSU)
    })
    io.id_isu.ready := true.B   // unidir hand shake 
    val id_isu_fire = RegNext(io.id_isu.fire())
    val reg_id_isu = RegEnableUse(io.id_isu.bits, io.id_isu.fire())
    printf(p"isu working: ${id_isu_fire} ${reg_id_isu.exu}\n")

    io.isu_alu <> DontCare
    io.isu_alu.valid := false.B
    io.isu_bru <> DontCare
    io.isu_bru.valid := false.B
    io.isu_lsu <> DontCare
    io.isu_lsu.valid := false.B
    switch(reg_id_isu.exu){
        is(ALU_ID){
            val iaBundle = Wire(new ISU_ALU)
            iaBundle := DontCare
            iaBundle.operand_1 := Mux(reg_id_isu.shamt_rs_sel, io.gpr_data.rs_data, reg_id_isu.shamt)
            iaBundle.alu_op := reg_id_isu.op
            iaBundle.rd_addr := reg_id_isu.rd_addr
            io.isu_alu.valid := id_isu_fire  & ~reset.asBool()    // XXX: 
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
            io.isu_bru.valid := id_isu_fire & ~reset.asBool()
            io.isu_bru.bits <> bruBundle
        }
        is(LSU_ID){
            io.isu_lsu.valid := id_isu_fire & ~reset.asBool()
            io.isu_lsu.bits.imm := reg_id_isu.imm
            io.isu_lsu.bits.rsData := io.gpr_data.rs_data
            io.isu_lsu.bits.rtData := io.gpr_data.rt_data
            io.isu_lsu.bits.rt := reg_id_isu.rd_addr
            io.isu_lsu.bits.lsu_op := reg_id_isu.op
        }
    }
}