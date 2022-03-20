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
    })
    io.id_isu.ready := true.B   // unidir hand shake 
    val id_isu_fire = RegNext(io.id_isu.fire())
    val reg_id_isu = RegEnableUse(io.id_isu.bits, io.id_isu.fire())
    printf(p"isu working: ${id_isu_fire}\n")

    io.isu_alu <> DontCare
    
    switch(reg_id_isu.exu){
        is(ALU_ID){
            val iaBundle = Wire(new ISU_ALU)
            iaBundle := DontCare
            iaBundle.operand_1 := Mux(reg_id_isu.shamt_rs_sel, reg_id_isu.shamt, io.gpr_data.rs_data)
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
    }

}