package njumips
package examples

import chisel3._
import chisel3.util._

import njumips.consts._

/* pipeline:
 * 
 * fire  WORKING fire   -> fire   WORKING  fire
 * empty WORKING fire   -> ready  WORKING  !valid
 * fire  WORKING !ready -> !ready BLOCKING valid
 *
 * */

class AtLeastNCyclesModule extends Module {
  val io = IO(new Bundle {
    val in = Flipped(DecoupledIO(Output(UInt(32.W))))
    val out = DecoupledIO(Output(UInt(32.W)))
  })

  val fu_valid = RegNext(next=io.in.valid, init=N)
  val fu_in = RegEnable(next=io.in.bits, enable=io.in.fire())
  // io.in.ready := random
  io.out.valid := fu_valid
  io.out.bits := fu_in
}

/* at least 1 cycle */
class PiplineUnitAtLeast1Cycle extends Module {
  val io = IO(new Bundle {
    val in = Flipped(DecoupledIO(Output(UInt(32.W))))
    val out = DecoupledIO(Output(UInt(32.W)))
    val flush = Input(Bool())
  })

  val m = Module(new AtLeastNCyclesModule)
  val fu_data = RegEnable(next=0.U, enable=m.io.in.fire())

  io.in.ready := m.io.in.ready

  m.io.in.valid := io.in.valid
  m.io.in.bits := io.in.bits
  io.in.ready := m.io.in.ready

  m.io.out.ready := io.out.ready
  io.out.valid := m.io.out.valid
  io.out.bits := m.io.out.bits
}

/* at least N cycle */
class PiplineUnitAtLeastNCycles extends Module {
  val io = IO(new Bundle {
    val in = Flipped(DecoupledIO(Output(UInt(32.W))))
    val out = DecoupledIO(Output(UInt(32.W)))
    val flush = Input(Bool())
  })

  val m = Module(new AtLeastNCyclesModule)

  val ncycles = 12
  val fu_datas = Module(new Queue(UInt(32.W), ncycles))
  fu_datas.reset := io.flush
  fu_datas.io.enq.valid := m.io.in.fire()
  fu_datas.io.enq.bits := Y
  fu_datas.io.deq.ready := m.io.out.fire()
  assert (fu_datas.io.enq.fire() === m.io.in.fire())

  io.in.ready := io.out.ready

  m.io.in.valid := io.in.valid
  m.io.in.bits := io.in.bits
  io.in.ready := m.io.in.ready

  m.io.out.ready := io.out.ready
  io.out.valid := m.io.out.valid
  io.out.bits := m.io.out.bits
  // ncycles is too large
}

/* 1 cycle */
class PiplineUnit1Cycle extends Module {
  val io = IO(new Bundle {
    val in = Flipped(DecoupledIO(Output(UInt(32.W))))
    val out = DecoupledIO(Output(UInt(32.W)))
    val flush = Input(Bool())
  })

  val fu_in = RegEnable(next=io.in.bits, enable=io.in.fire())
  val fu_valid = RegInit(N)

  io.in.ready := io.out.ready || !fu_valid

  io.out.valid := fu_valid
  io.out.bits := fu_in

  when (io.flush || (!io.in.fire() && io.out.fire())) {
    fu_valid := N
  } .elsewhen (!io.flush && io.in.fire()) {
    fu_valid := Y
  }
}

/* n cycle */
class PiplineUnitNCycles extends Module {
  val io = IO(new Bundle {
    val in = Flipped(DecoupledIO(Output(UInt(32.W))))
    val out = DecoupledIO(Output(UInt(32.W)))
    val flush = Input(Bool())
  })

  val ncycles = 12
  val fu_in = RegEnable(next=io.in.bits, enable=io.in.fire())
  val fu_valids = RegInit(0.U(ncycles.W))

  io.in.ready := io.out.ready || !fu_valids(0)

  io.out.valid := fu_valids(0)
  io.out.bits := Pipe(Y, fu_in, ncycles)

  when (io.flush) {
    fu_valids := 0.U
  } .elsewhen (!io.in.fire() && io.out.fire()) {
    fu_valids := Cat(N, fu_valids >> 1)
  } .elsewhen (io.in.fire()) {
    fu_valids := Cat(Y, fu_valids >> 1)
  }
}

