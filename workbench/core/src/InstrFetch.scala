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
        val icache = new CacheIO
        val flush = Input(Bool())
        val if_id = Decoupled(new IF_ID)

        val pc_w = Output(UInt(32.W))
    })
    val timeClock = RegInit(1.U(32.W))
    timeClock := timeClock + 1.U
    printf("==============\n");
    printf(p"time:${timeClock}\n")

    val pc_reg = RegInit(conf.start_addr)
    io.pc_w := pc_reg

    val flush = RegInit(N)
    val if_id_instr_prepared = RegInit(false.B)
    io.wb_if.ready := true.B
    when(io.wb_if.fire()){
        //printf("branch!\n")
        pc_reg := io.wb_if.bits.pc_w_data
        //printf("@IF pc_reg %x\n", pc_reg)
        when(io.icache.req.ready){
            flush := N
        }.otherwise{
            flush := Y
        }
    } .elsewhen(io.icache.req.fire()){
        pc_reg := pc_reg + 4.U
    } 

    def isAddrValid(x:UInt) = x(1, 0) === 0.U
    val request_pc = RegEnable(pc_reg, io.icache.req.fire())

    io.tlb_req.va := pc_reg
    io.tlb_req.ref_type := MX_RD

    io.icache.req.bits.strb := DontCare
    io.icache.req.bits.data := DontCare
    io.icache.req.bits.is_cached := io.tlb_resp.cached
    io.icache.req.bits.func := MX_RD
    io.icache.req.bits.addr := io.tlb_resp.pa // pc_reg
    io.icache.req.bits.len := 3.U   // 00, 01, 10, 11
    io.icache.req.bits.exception := io.tlb_resp.exception
    io.icache.req.valid := true.B && !io.flush && isAddrValid(pc_reg)
    
    io.icache.resp.ready := io.if_id.ready || !if_id_instr_prepared
    
    val if_id_instr = RegEnable(io.icache.resp.bits.data, io.icache.resp.fire())
    val if_id_next_pc = RegEnable(request_pc + 4.U, io.icache.resp.fire())
    val exception = RegEnable(io.icache.resp.bits.exception, io.icache.resp.fire())
    

    when(io.flush || (!io.icache.resp.fire() && io.if_id.fire())){
        if_id_instr_prepared := N
    } .elsewhen(!io.flush && io.icache.resp.fire()){
        when(flush){
           // printf("flush wrong instr %x\n", io.icache.resp.bits.data)
            flush := N
            if_id_instr_prepared := N
        }.otherwise{
            if_id_instr_prepared := Y
        }
    }
    io.if_id.valid := (if_id_instr_prepared || !isAddrValid(pc_reg)) && !io.flush
    io.if_id.bits.instr := Mux(exception === ET_None, if_id_instr, 0.U)
    
    io.if_id.bits.pcNext :=  if_id_next_pc
    
    io.if_id.bits.except_info.enable := exception =/= ET_None
    io.if_id.bits.except_info.EPC := if_id_next_pc - 4.U 
    io.if_id.bits.except_info.badVaddr := if_id_next_pc - 4.U 
    io.if_id.bits.except_info.exeCode := EC_TLBL
    io.if_id.bits.except_info.excType := exception

    //FIXME::当地址不满足时候到底怎么处理，只要处理地址不对齐吗？
    when(!isAddrValid(pc_reg)){
        io.if_id.bits.except_info.enable := Y
        io.if_id.bits.except_info.EPC := pc_reg
        io.if_id.bits.except_info.badVaddr := pc_reg
        io.if_id.bits.except_info.exeCode := EC_AdEL
        io.if_id.bits.except_info.excType := ET_ADDR_ERR
        io.if_id.bits.instr := 0.U
        io.if_id.bits.pcNext := pc_reg + 4.U
    }
    //io.if_id.bits.exception := exception
    
    when(io.if_id.fire()){
        printf("start executing: %x\n", request_pc - 4.U)
    }
}