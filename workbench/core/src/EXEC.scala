package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class ALU extends Module{
    val io = IO(new Bundle{
        val isu_alu = Flipped(new ISU_ALU)
        val out = new ALUOutput
    })

    // TODO: 
    //when(io.isu_commit_to_alu)
}