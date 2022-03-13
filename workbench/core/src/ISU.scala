package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import src.RegEnableUse
class ISU extends Module {
    val io = IO(new Bundle{
        val gpr_data = new GPRReadIntput //input
        val id_isu = Flipped(new ID_ISU) //input
        
        val isu_alu = new ISU_ALU //output

        val gpr_req = Flipped(new GPRReadIntput) //require gpr output
        val gpr_res = Flipped(new GPRReadOutput) //get from gpr input
    })

    val reg_id_isu = RegEnableUse(io.id_isu, io.id_isu.id_commit)

    io.gpr_req <> io.gpr_data

    io.isu_alu <> DontCare
    io.isu_alu.isu_commit_to_alu := false.B
    
    when(reg_id_isu.id_commit){
        switch(reg_id_isu.exu){
            is(0.U){//alu TODO:: one clock to handshake
                val iaBundle = Wire(new ISU_ALU)
                iaBundle.operand_1 := io.gpr_res.rs_data
                iaBundle.isu_commit_to_alu := true.B
                iaBundle.alu_op := reg_id_isu.exu
                iaBundle.rd_addr := reg_id_isu.rd_addr
                io.isu_alu.isu_commit_to_alu := true.B 
                io.isu_alu.alu_op := reg_id_isu.op
                switch(reg_id_isu.imm_rt_sel){
                    is(false.B){//imm
                        when(reg_id_isu.sign_ext){
                            iaBundle.operand_2 := Cat(Fill((16), reg_id_isu.imm(15)), reg_id_isu.imm).asUInt()
                        }.otherwise{
                            iaBundle.operand_2 := Cat(Fill((16), 0.U), reg_id_isu.imm).asUInt()
                        }
                    }
                    is (true.B){//reg
                        iaBundle.operand_2 := io.gpr_res.rt_data
                    }
                }
                io.isu_alu <> iaBundle
            }
        }
    }
}