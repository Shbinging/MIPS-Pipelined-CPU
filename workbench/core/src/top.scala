package njumips


import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import java.awt.MouseInfo
import firrtl.options.DoNotTerminateOnExit




class verilator_top extends Module {
    val io = IO(new Bundle {
        val commit = new CommitIO   // defined in interfaces.scala
        val can_log_now = Input(Bool())
    })

    val gprs = Module(new GPR)
    
    val cp0 = Module(new CP0)

    val instr_fetch = Module(new InstrFetch)
    val icache = Module(new L1Cache)
    
    val instr_decode = Module(new InstrDecode)
    
    val instr_shoot = Module(new ISU )

    val alu = Module(new ALU)
    val bru = Module(new BRU)
    
    val lsu = Module(new LSU)
    val dcache = Module(new L1Cache)
    
    val mdu = Module(new MDU)

    val write_back = Module(new WriteBack)

    val tlb = Module(new TLB)
    val i_tlb_translator = Module(new TLBTranslator)
    val d_tlb_translator = Module(new TLBTranslator)
    i_tlb_translator.io.tlb <> tlb.io.entries
    i_tlb_translator.io.req <> instr_fetch.io.tlb_req
    instr_fetch.io.tlb_resp <> i_tlb_translator.io.resp
    i_tlb_translator.io.asid := cp0.io.cp0_entryhi.asid
    
    // TODO
    d_tlb_translator.io.tlb <> tlb.io.entries
    d_tlb_translator.io.req <> lsu.io.tlb_req
    lsu.io.tlb_resp <> d_tlb_translator.io.resp
    d_tlb_translator.io.asid := cp0.io.cp0_entryhi.asid

    val mem_arbiter = Module(new Arbiter(new MemReq, 2))
    val mem = Module(new SimDev)
    val arbit_chosen = RegEnable(enable=mem.io.in.req.fire(), next=mem_arbiter.io.chosen)

    val pru = Module(new PRU)
    mem.io.clock := clock 
    mem.io.reset := reset.asBool()
    mem_arbiter.io.in <> VecInit(icache.io.out.req, dcache.io.out.req)
    mem.io.in.req <> mem_arbiter.io.out
    dcache.io.out.resp.valid := mem.io.in.resp.valid && (arbit_chosen===1.U)
    icache.io.out.resp.valid := mem.io.in.resp.valid && (arbit_chosen===0.U) 
    mem.io.in.resp.ready := dcache.io.out.resp.ready | icache.io.out.resp.ready
    dcache.io.out.resp.bits <> mem.io.in.resp.bits 
    icache.io.out.resp.bits <> mem.io.in.resp.bits

    val irq7 = RegNext(write_back.io.irq7)
    val commit = RegNext(write_back.io.commit)
    instr_fetch.io.flush := write_back.io.flush
    instr_decode.io.flush := write_back.io.flush 
    instr_shoot.io.flush := write_back.io.flush 
    
    pru.io.flush := write_back.io.flush
    bru.io.flush := write_back.io.flush
    mdu.io.flush := write_back.io.flush
    lsu.io.flush := write_back.io.flush
    alu.io.flush := write_back.io.flush

    instr_fetch.io.wb_if <> write_back.io.wb_if
    icache.io.in <> instr_fetch.io.icache
    
    instr_decode.io.if_id <> instr_fetch.io.if_id

    gprs.io.read_in <> instr_shoot.io.out_gpr_read
    
    instr_shoot.io.id_isu <> instr_decode.io.id_isu
    instr_shoot.io.gpr_data <> gprs.io.read_out
    instr_shoot.io.rb_isu <> write_back.io.gpr_wr
    instr_shoot.io.alu_pass <> alu.io.exec_pass
    instr_shoot.io.isu_pru <> pru.io.isu_pru
    alu.io.isu_alu <> instr_shoot.io.isu_alu
    
    bru.io.isu_bru <> instr_shoot.io.isu_bru
    
    lsu.io.isu_lsu <> instr_shoot.io.isu_lsu
    dcache.io.in <> lsu.io.dcache

    mdu.io.isu_mdu <> instr_shoot.io.isu_mdu

    write_back.io.alu_wb <> alu.io.exec_wb
    write_back.io.bru_wb <> bru.io.exec_wb
    write_back.io.lsu_wb <> lsu.io.exec_wb
    write_back.io.mdu_wb <> mdu.io.exec_wb
    write_back.io.pru_wb <> pru.io.exec_wb

    //write_back.io.cp0_write_out <> cp0.io.cp0_write_in
    write_back.io.cp0_status := cp0.io.cp0_status
    write_back.io.cp0_cause := cp0.io.cp0_cause
    write_back.io.cp0_context := cp0.io.cp0_context
    write_back.io.cp0_entryhi := cp0.io.cp0_entryhi
    write_back.io.cp0_status := cp0.io.cp0_status
    write_back.io.isIrq7 := cp0.io.irq7

    gprs.io.write_in <> write_back.io.gpr_wr

    pru.io.cp0_taglo := cp0.io.cp0_taglo
    pru.io.cp0_taghi := cp0.io.cp0_taghi
    pru.io.cp0_index := cp0.io.cp0_index
    pru.io.cp0_random := cp0.io.cp0_random
    pru.io.cp0_entryhi := cp0.io.cp0_entryhi
    pru.io.cp0_entrylo_0 := cp0.io.cp0_entrylo_0
    pru.io.cp0_entrylo_1 := cp0.io.cp0_entrylo_1

    pru.io.cp0_badAddr := cp0.io.cp0_badAddr
    pru.io.cp0_cause := cp0.io.cp0_cause
    pru.io.cp0_epc := cp0.io.cp0_epc
    pru.io.cp0_status := cp0.io.cp0_status

    pru.io.tlb_entries := tlb.io.entries
    pru.io.cp0_compare := cp0.io.cp0_compare_0
    pru.io.cp0_count := cp0.io.cp0_count_0

    tlb.io.in := pru.io.tlb_wr

    icache.io.cache_cmd := pru.io.icache_cmd
    dcache.io.cache_cmd := pru.io.dcache_cmd

    cp0.io.in_badAddr_sel_0 <> write_back.io.out_badAddr_sel_0
    cp0.io.in_cause_sel_0 <> write_back.io.out_cause_sel_0
    cp0.io.in_index_sel_0 := write_back.io.out_index_sel_0
    cp0.io.in_random_sel_0 := DontCare // write_back.io.out_random_sel_0 
    cp0.io.in_status_sel_0 := write_back.io.out_status_sel_0
    cp0.io.in_epc_sel_0 := write_back.io.out_epc_sel_0
    cp0.io.in_badAddr_sel_0 := write_back.io.out_badAddr_sel_0
    cp0.io.in_context_sel_0 := write_back.io.out_context_sel_0
    cp0.io.in_entryhi_sel_0 := write_back.io.out_entryhi_sel_0
    cp0.io.in_entrylo0_sel_0 := write_back.io.out_entrylo0_sel_0
    cp0.io.in_entrylo1_sel_0 := write_back.io.out_entrylo1_sel_0
    cp0.io.in_cp0_count_0 := write_back.io.out_count_sel_0
    cp0.io.in_cp0_compare_0 := write_back.io.out_compare_sel_0
    

    cp0.io.in_taghi_sel_0.en := N 
    cp0.io.in_taghi_sel_0.data <> DontCare
    cp0.io.in_taglo_sel_0.en := N 
    cp0.io.in_taglo_sel_0.data <> DontCare
    io.commit <> DontCare
    for(i <- 0 to 31){
        io.commit.gpr(i) := gprs.io.gpr_commit(i)
    }
    io.commit.pc := commit.commit_pc
    io.commit.valid := commit.commit
    io.commit.instr := commit.commit_instr
    io.commit.cp0_count := cp0.io.cp0_count_0
    printf("@top ip7 %d\n", io.commit.ip7)
    io.commit.ip7 := irq7
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
