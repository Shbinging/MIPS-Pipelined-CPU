package njumips
package core

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import njumips.utils._

class Core extends Module {
  val io = IO(new Bundle {
    val imem = new MemIO
    val dmem = new MemIO
    val commit = new CommitIO
    val icache_control = ValidIO(new CacheControl)
    val dcache_control = ValidIO(new CacheControl)
    val multiplier = new MultiplierIO
    val divider = new DividerIO
    val ex_flush = Output(Bool())
    val br_flush = Output(Bool())
    val can_log_now = Input(Bool())
  })

  io.imem.req.valid := N
  io.imem.req.bits := 0.U.asTypeOf(io.imem.req.bits)
  io.imem.resp.ready := N

  io.dmem.req.valid := N
  io.dmem.req.bits := 0.U.asTypeOf(io.dmem.req.bits)
  io.dmem.resp.ready := N

  io.icache_control <> DontCare
  io.dcache_control <> DontCare
  io.multiplier <> DontCare
  io.divider <> DontCare

  io.commit := 0.U.asTypeOf(io.commit)
  io.ex_flush := N
  io.br_flush := N
}
