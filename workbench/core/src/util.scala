package njumips
import chisel3._
import chisel3.util._
object RegEnableIO{
    def apply[T <: Data](next: T, enable: Bool): T = {
        val r = Reg(Output(chiselTypeOf(next)))
        when (enable) { r := next }
        r
  }
}