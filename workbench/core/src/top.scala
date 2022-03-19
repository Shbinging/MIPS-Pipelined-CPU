package njumips


import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._




class verilator_top extends Module {
    val io = IO(new Bundle {
        val commit = new CommitIO   // defined in interfaces.scala
        val can_log_now = Input(Bool())
        val vali = Output(Bool())
    })
    val gprs = Module(new GPR)
    
    val instr_fetch = Module(new InstrFetch)
    val instr_decode = Module(new InstrDecode)
    val instr_shoot = Module(new ISU )

    val alu = Module(new ALU)
    
    val write_back = Module(new WriteBack)
    io.vali := write_back.io.wb_if.valid
    // program_counter.io.in <> instr_fetch.io.pc_writer // ignore branches temporarily
    instr_fetch.io.wb_if <> write_back.io.wb_if
    
    instr_decode.io.if_id <> instr_fetch.io.if_id
    
    gprs.io.read_in <> instr_decode.io.out_gpr_read
    
    instr_shoot.io.id_isu <> instr_decode.io.id_isu
    instr_shoot.io.gpr_data <> gprs.io.read_out
    
    alu.io.isu_alu <> instr_shoot.io.isu_alu
    
    write_back.io.exec_wb <> alu.io.exec_wb
    write_back.io.alu_output <> alu.io.out
    
    gprs.io.write_in <> write_back.io.gpr_wr

    io.commit <> DontCare
    for(i <- 0 to 31){
        io.commit.gpr(i) := gprs.io.gpr_commit(i)
    }
    io.commit.pc := instr_fetch.io.pc

    val cycle = RegNext(instr_fetch.io.wb_if.fire() | reset.asBool())
    io.commit.valid := cycle
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
