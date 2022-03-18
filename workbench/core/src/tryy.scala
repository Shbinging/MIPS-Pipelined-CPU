package src

import chisel3.Bundle
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import njumips.GPRReadIntput
import njumips.GPRReadOutput
import scala.runtime.BoxedUnit
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

class trybundle extends Bundle{
    val data = UInt(32.W)
}

class tryyy extends Module{
    val io = IO(new Bundle{
        val input_data = Input(new trybundle)
        val output_data = Output(new trybundle)
    })
    val reg = Reg(new trybundle)
    reg <> io.input_data
    io.output_data <> reg
}

class try_decopuled extends Module{
    val io = IO(new Bundle{
        val in = Flipped(DecoupledIO(new trybundle))
        val out = Output(UInt(32.W))
    })

    io.in.ready := true.B
    when(io.in.fire()){
        io.out := io.in.bits.data
    } .otherwise{
        io.out := 0.U
    }
}

class go_decoupled extends Module{
    val io = IO(new Bundle{
        val in = Input(UInt(32.W))
        val out = DecoupledIO(Output(new trybundle))
    })

    io.out.bits.data := io.in
    when(io.in >= 10.U){
        io.out.valid := true.B
    } .otherwise{
        io.out.valid := false.B
    }
}

class DecoupleTop extends Module{
    val io = IO(new Bundle{
        val in = Input(UInt(32.W))
        val out = Output(UInt(32.W))
    })
    val feeder = Module(new go_decoupled)
    val feedee = Module(new try_decopuled)
    feeder.io.in := io.in 
    io.out := feedee.io.out 
    feedee.io.in <> feeder.io.out
}