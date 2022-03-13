package src

import chisel3.Bundle
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import njumips.GPRReadIntput
import njumips.GPRReadOutput
object RegEnableUse{
    def apply[T <: Data](next: T, enable: Bool): T = {
        val r = Reg(Output(chiselTypeOf(next)))
        when (enable) { r := next }
        r
  }
}
class tryy extends Module{
    val io = IO(new Bundle{
        val aa = new GPRReadIntput
        val bb = Flipped(new GPRReadIntput)
     }
    )
    val r = Wire(new GPRReadIntput)
    r.rs_addr := 5.U
    r.rt_addr := 5.U
    io.bb <> r
}