package njumips
package core

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.utils._
import njumips.consts._

class SimDev extends BlackBox {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(Bool())
    val in = Flipped(new MemIO)
  })
}

class DeviceAccessor extends Module {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(Bool())
    val in = Flipped(new MemIO)
  })

  val dev = Module(new SimDev)
  dev.io.clock := io.clock
  dev.io.reset := io.reset

  val lfsr = LFSR16(io.in.asUInt.xorR)
  val delay = RegInit(0.U(4.W))
  if (conf.random_delay) {
    io.in.req <> dev.io.in.req
    when (io.in.req.fire()) { delay := lfsr }
    .otherwise {
      delay := Mux(delay === 0.U, 0.U, delay - 1.U)
    }

    val timeout = delay === 0.U
    dev.io.in.resp.ready := io.in.resp.ready && timeout
    io.in.resp.valid := dev.io.in.resp.valid && timeout
    io.in.resp.bits := dev.io.in.resp.bits
  } else {
    io.in <> dev.io.in
  }
}

class Divider extends Module {
  val io = IO(Flipped(new DividerIO))
  val dividend = io.data_dividend_bits
  val divisor = io.data_divisor_bits
  val quotient = (dividend / divisor).asUInt
  val remainder = (dividend % divisor).asUInt
  require(quotient.getWidth == 40)
  require(remainder.getWidth == 40)
  val pipe = Pipe(io.data_dividend_valid && io.data_divisor_valid,
    Cat(quotient, remainder), conf.div_stages)

  io.data_dividend_ready := Y
  io.data_divisor_ready := Y
  io.data_dout_valid := pipe.valid
  io.data_dout_bits := pipe.bits
}

class Multiplier extends Module {
  val io = IO(Flipped(new MultiplierIO))
  val a = io.data_a.asSInt
  val b = io.data_b.asSInt
  val pipe = Pipe(Y, (a * b).asUInt, conf.mul_stages)

  io.data_dout := pipe.bits
}

class verilator_top extends Module {
  val io = IO(new Bundle {
    val commit = new CommitIO
    val can_log_now = Input(Bool())
  })

  val core = Module(new Core)
  val imux = Module(new MemMux("imux"))
  val dmux = Module(new MemMux("dmux"))
  val dev = Module(new SimDev)
  val crossbar = Module(new CrossbarNx1(4))

  core.io.multiplier <> DontCare
  core.io.can_log_now := io.can_log_now
  core.io.divider <> DontCare
  dev.io.clock := clock
  dev.io.reset := reset

  imux.io.in <> core.io.imem
  dmux.io.in <> core.io.dmem

  imux.io.cached   <> crossbar.io.in(0)
  imux.io.uncached <> crossbar.io.in(1)
  dmux.io.cached   <> crossbar.io.in(2)
  dmux.io.uncached <> crossbar.io.in(3)

  crossbar.io.out <> dev.io.in

  core.io.commit <> io.commit
}

class AXI4_EMU_TOP extends Module {
  val io = IO(new Bundle {
    val commit = new CommitIO
  })

  val core = Module(new Core)
  val imux = Module(new MemMux("imux"))
  val dmux = Module(new MemMux("dmux"))
  val dev = Module(new SimDev)
  val crossbar = Module(new CrossbarNx1(4))
  val icache = Module(new ICache)
  val i2sram = Module(new AXI42SRAM)
  val dcache = Module(new DCache)
  val d2sram = Module(new AXI42SRAM)

  dev.io.clock := clock
  dev.io.reset := reset

  imux.io.in <> core.io.imem
  dmux.io.in <> core.io.dmem
  imux.io.cached <> icache.io.in
  icache.io.out <> i2sram.io.in
  dmux.io.cached <> dcache.io.in
  dcache.io.out <> d2sram.io.in

  icache.io.ex_flush <> core.io.ex_flush
  dcache.io.ex_flush <> core.io.ex_flush
  icache.io.br_flush <> core.io.br_flush
  dcache.io.br_flush <> core.io.br_flush

  i2sram.io.out    <> crossbar.io.in(0)
  imux.io.uncached <> crossbar.io.in(1)
  d2sram.io.out    <> crossbar.io.in(2)
  dmux.io.uncached <> crossbar.io.in(3)

  crossbar.io.out <> dev.io.in

  core.io.commit <> io.commit
}

class loongson_top extends Module {
  val io = IO(new Bundle {
    val imem = new AXI4IO(conf.xprlen)
    val dmem = new AXI4IO(conf.xprlen)
    val divider = new DividerIO
    val multiplier = new MultiplierIO
    val commit = new CommitIO
  })

  val core = Module(new Core)
  val imem2axi = Module(new MemIO2AXI(conf.xprlen))
  val dmem2axi = Module(new MemIO2AXI(conf.xprlen))

  core.io.can_log_now := N

  core.io.imem <> imem2axi.io.in
  core.io.dmem <> dmem2axi.io.in
  core.io.divider <> io.divider
  core.io.multiplier <> io.multiplier
  imem2axi.io.out <> io.imem
  dmem2axi.io.out <> io.dmem

  io.commit <> core.io.commit
}

class zedboard_top extends loongson_top {
}

object Main extends App {
  override def main(args:Array[String]):Unit = {
    val top = args(0)
    val chiselArgs = args.slice(1, args.length)
    chisel3.Driver.execute(chiselArgs, () => {
      val clazz = Class.forName("njumips.core."+top)
      val constructor = clazz.getConstructor()
      constructor.newInstance().asInstanceOf[Module]
    })
  }
}
