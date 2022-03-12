package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class WriteBack extends Module{
    val io = IO(new Bundle{
        val alu_output = Flipped(new ALUOutput)
        val pc_wr = Flipped(new PCInput)    // XXX: ignore temporarily
        val gpr_wr = Flipped(new GPRWriteInput)
        val cycle_done = Output(Bool())
    })
    val cycle_done_reg = RegNext(reset | alu_output.exec_wr.exec_commit)
    // TODO:

    io.pc_wr <> DontCare
    
    when(reset){
        cycle_done := true.B
    }
}