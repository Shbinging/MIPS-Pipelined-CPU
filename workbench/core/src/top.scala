package njumips


import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import java.awt.MouseInfo




class verilator_top extends Module {
    val io = IO(new Bundle {
        val commit = new CommitIO   // defined in interfaces.scala
        val can_log_now = Input(Bool())
    })

    val gprs = Module(new GPR)
    val instr_fetch = Module(new InstrFetch)
    val instr_decode = Module(new InstrDecode)
    val instr_shoot = Module(new ISU )

    val alu = Module(new ALU)
    val bru = Module(new BRU)
    val lsu = Module(new LSU)
    val mdu = Module(new MDU)

    val write_back = Module(new WriteBack)

    

    val commit = RegNext(write_back.io.commit)
    instr_fetch.io.flush := write_back.io.flush
    instr_decode.io.flush := write_back.io.flush 
    instr_shoot.io.flush := write_back.io.flush 

    // program_counter.io.in <> instr_fetch.io.pc_writer // ignore branches temporarily
    instr_fetch.io.wb_if <> write_back.io.wb_if
    
    instr_decode.io.if_id <> instr_fetch.io.if_id
    
    gprs.io.read_in <> instr_decode.io.out_gpr_read
    
    instr_shoot.io.id_isu <> instr_decode.io.id_isu
    instr_shoot.io.gpr_data <> gprs.io.read_out
    
    alu.io.isu_alu <> instr_shoot.io.isu_alu
    bru.io.isu_bru <> instr_shoot.io.isu_bru
    lsu.io.isu_lsu <> instr_shoot.io.isu_lsu
    mdu.io.isu_mdu <> instr_shoot.io.isu_mdu

    write_back.io.alu_wb <> alu.io.exec_wb
    write_back.io.bru_wb <> bru.io.exec_wb
    write_back.io.lsu_wb <> lsu.io.exec_wb
    write_back.io.mdu_wb <> mdu.io.exec_wb

    gprs.io.write_in <> write_back.io.gpr_wr
    //printf(p"write_back io gpr wr: ${write_back.io.gpr_wr}\n")
    io.commit <> DontCare
    for(i <- 0 to 31){
        io.commit.gpr(i) := gprs.io.gpr_commit(i)
    }
    io.commit.pc := commit.commit_pc
    io.commit.valid := commit.commit
    io.commit.instr := commit.commit_instr

    //printf(p"Commit! ${io.commit}\n")
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
