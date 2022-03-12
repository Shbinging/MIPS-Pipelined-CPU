package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class InstrDecode extends Module{
    val io = IO(new Bundle{
        val in = Flipped(new IF_ID)
        val out_gpr_read = Flipped(new GPRReadIntput)
        val out_isu = new ID_ISU
    })

    val out_gpr_read_reg = Bundle{
        val rs_addr = RegEnable(io.in.instr(25, 21), io.in.id_en)
        val rt_addr = RegEnable(io.in.instr(20, 16), io.in.id_en)
    }
    val out_isu_reg = Bundle{
        val id_commit = RegNext(io.in.id_en & (~reset))
        val rd_addr = Reg(REG_SZ.W)
        val imm = RegEnable(io.in.instruction(15, 0), io.in.id_en)
        val sign_ext = Reg(Bool())
        val exu = Reg(EX_ID_WIDTH.W)
        val op = Reg(OPCODE_WIDTH.W)
    }
    val instr_op = io.in.instr(31, 25)
    val funct = io.in.instr(5, 0)

    when(io.in.id_en){
        // TODO: decode the instr here
    } .otherwise{
        // TODO
    }

    io.out_gpr_read <> out_gpr_read_reg
    io.out_isu <> out_isu_reg
}