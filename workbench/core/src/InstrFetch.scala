package njumips
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._


class InstrFetch extends Module{
    val io = IO(new Bundle{
        val wb_if = Flipped(Decoupled(new RB_IF))
        val tlb_req = Flipped(new TLBTranslatorReq)
        val tlb_resp = Flipped(new TLBTranslatorResp)
        val icache = new MemIO
        val flush = Input(Bool())
        val if_id = Decoupled(new IF_ID)
    })
    val pc_reg = RegInit(conf.start_addr)
    // val dev = Module(new SimDev)
    val timeClock = RegInit(1.U(32.W))
    timeClock := timeClock + 1.U
    printf("==============\n");
    printf(p"time:${timeClock}\n")
    // dev.io.clock := clock
    // dev.io.reset := reset.asBool()
    //printf(p"wb_if ${io.wb_if}\n")
    io.wb_if.ready := true.B
    val flush = RegInit(N)
    val if_id_instr_prepared = RegInit(false.B)
    when(io.wb_if.fire()){
        printf("branch!\n")
        pc_reg := io.wb_if.bits.pc_w_data
        when(io.icache.req.ready){
            flush := N
        }.otherwise{
            flush := Y
        }
    } .elsewhen(io.icache.req.fire()){
        pc_reg := pc_reg + 4.U
    } 
    when(io.icache.req.fire()){
        printf("commit read %x\n", pc_reg)
    }
    // printf(p"dev ready:${io.icache.req.ready}\n")    
    io.icache.req.bits.strb := DontCare
    io.icache.req.bits.data := DontCare

    val request_pc = RegEnable(pc_reg, io.icache.req.fire())
    
    io.tlb_req.pa := pc_reg
    io.tlb_req.ref_type := MX_RD
    
    io.icache.req.bits.is_cached := io.tlb_resp.cached
    io.icache.req.bits.func := MX_RD
    io.icache.req.bits.addr := io.tlb_resp.pa // pc_reg
    io.icache.req.bits.len := 3.U   // 00, 01, 10, 11
    io.icache.req.valid := true.B && !io.flush && (io.tlb.exception =/= ET_None)
    
    io.icache.resp.ready := io.if_id.ready || !if_id_instr_prepared
    
    val if_id_instr = Reg(UInt(conf.data_width.W))
    val exception = Reg(UInt(ET_WIDTH.W))
    exception := io.tlb_resp.exception
    when(io.tlb_resp.exception =/= ET_None){
        if_id_instr := 0.U
    }.elsewhen(io.icache.resp.fire()){
        if_id_instr := io.icache.resp.bits.data
    }
    val if_id_next_pc = RegEnable(request_pc + 4.U, io.icache.resp.fire())

    when(io.flush || (!(io.icache.resp.fire() || io.tlb_resp.exception =/= ET_None) && io.if_id.fire())){
        if_id_instr_prepared := N
    } .elsewhen(!io.flush && (io.icache.resp.fire() || io.tlb_resp.exception =/= ET_None)){
        when(flush){
            printf("flush wrong instr %x\n", io.icache.resp.bits.data)
            flush := N
            if_id_instr_prepared := N
        }.otherwise{
            if_id_instr_prepared := Y
        }
    }
    io.if_id.valid := if_id_instr_prepared && !io.flush
    io.if_id.bits.instr := if_id_instr
    io.if_id.bits.pcNext :=  if_id_next_pc
    io.if_id.bits.exception := exception
    when(io.if_id.fire()){
        printf("start executing: %x\n", request_pc - 4.U)
    }
}