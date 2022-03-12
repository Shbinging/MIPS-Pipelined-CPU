package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class ISU extends Module {
    val io = IO(new Bundle{
        val gpr_data = Flipped(new GPRReadOutput)
        val id_isu = Flipped(new ID_ISU)
        val isu_alu = new ISU_ALU
    })
    // TODO:
}