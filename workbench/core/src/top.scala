package njumips


import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._




class verilator_top extends Module {
  val io = IO(new Bundle {
    val commit = new CommitIO   // defined in interfaces.scala
    val can_log_now = Input(Bool())
  })

  val program_counter = Module(new ProgramCounter)
  val gprs = Module(new GPR)
  
  val instr_fetch = Module(new InstrFetch)
  val instr_decode = Module(new InstrDecode)
  val instr_shoot = Module(new ISU )

  val alu = Module(new ALU)
  
  val write_back = Module(new WriteBack)
  
  program_counter.io.in <> instr_fetch.io.pc_writer // ignore branches temporarily
  
  instr_fetch.io.pc := program_counter.io.out
  
  instr_fetch.io.if_id <> instr_decode.io.in
  instr_decode.io.out_gpr_read <> gprs.io.read_in
  
  //instr_shoot.io.id_isu <> instr_decode.io.id_isu
  instr_shoot.io.gpr_data <> gprs.io.read_out
  
  alu.io.isu_alu <> instr_shoot.io.isu_alu
  
  write_back.io.alu_output <> alu.io.out
  gprs.io.write_in <> write_back.io.gpr_wr
}



object TopMain extends App {
  override def main(args:Array[String]):Unit = {
    val top = args(0)
    val chiselArgs = args.slice(1, args.length)
    chisel3.Driver.execute(chiselArgs, () => {
      val clazz = Class.forName("njumips."+top)
      val constructor = clazz.getConstructor()
      constructor.newInstance().asInstanceOf[Module]
    })
  }
}
