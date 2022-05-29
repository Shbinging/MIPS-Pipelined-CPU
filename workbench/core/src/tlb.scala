package njumips
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._


class TLBEntry extends Bundle{
    val hi = new EntryHi
    val lo_0 = new EntryLo
    val lo_1 = new EntryLo
}

class TLB extends Module{
    val io = IO(new Bundle{
        val in = Flipped(new TLBEntryIO)
        val entries = Output(Vec(conf.tlb_size, new TLBEntry))
    })

    val tlb = Mem(conf.tlb_size, new TLBEntry)
    for(i <- 0 to conf.tlb_size-1){
        io.entries(i) := tlb(i)
    }
    for(i <- 0 to conf.tlb_size-1){
        when(reset.asBool()){
            tlb(i).lo_0.valid := false.B 
            tlb(i).lo_1.valid := false.B
        }
    }
    when(io.in.en){
        //printf(p"writing tlb ${io.in.index}")
        tlb(io.in.index).hi := io.in.hi 
        tlb(io.in.index).lo_0 := io.in.lo_0 
        tlb(io.in.index).lo_1 := io.in.lo_1
    }
}

class TLBTranslator extends Module{
    val io = IO(new Bundle{
        val tlb = Input(Vec(conf.tlb_size, new TLBEntry))
        val asid = Input(UInt(8.W))
        val req = new TLBTranslatorReq
        val resp = new TLBTranslatorResp
    })
    // FIX Page Size 4096 kB
    //printf("===CPU===\n")
    for(i <- 1 to 1){//conf.tlb_size-1
      //  printf(p"${i}: vpn ${io.tlb(i).hi.vpn2} asid: ${io.tlb(i).hi.asid}\n")
       // printf(p"p0, pfn ${io.tlb(i).lo_0.pfn} c ${io.tlb(i).lo_0.coherence} v ${io.tlb(i).lo_0.valid} d ${io.tlb(i).lo_0.dirty}\n")
        //printf(p"p1, pfn ${io.tlb(i).lo_1.pfn} c ${io.tlb(i).lo_1.coherence} v ${io.tlb(i).lo_1.valid} d ${io.tlb(i).lo_1.dirty}\n")
    }
    //printf("=========\n")

    io.resp.exception := ET_None
    io.resp.pa := 0.U
    
    // unmapped addr
    when(io.req.va >= "h_8000_0000".U && io.req.va < "h_c000_0000".U){ 
        // uncached
        when(io.req.va >= "h_a000_0000".U){
            io.resp.pa := io.req.va - "h_a000_0000".U 
            io.resp.cached := false.B
        } .otherwise{
            io.resp.pa := io.req.va - "h_8000_0000".U 
            io.resp.cached := true.B
        }
    } .otherwise{
        // mapped
        val found = Wire(Bool())
        // val entry = Wire(new EntryLo)
        found := false.B
        io.resp.cached := true.B
        for(i <- 0 to conf.tlb_size-1){
            when(
                (io.tlb(i).hi.vpn2 === (io.req.va>>13)) && (
                    (io.tlb(i).lo_0.global & io.tlb(i).lo_1.global) ||
                    (io.tlb(i).hi.asid === io.asid)
                )
            ){
                val entry = Mux(io.req.va(12), io.tlb(i).lo_1, io.tlb(i).lo_0)
                when(entry.valid === false.B){
                    io.resp.exception := ET_TLB_Inv
                } .elsewhen(entry.dirty===false.B && io.req.ref_type===MX_WR){
                    io.resp.exception := ET_TLB_Mod
                }
                io.resp.pa := Cat(entry.pfn(19, 0), io.req.va(11, 0))
                found := true.B
                //printf(p"Found at ${i}: ${io.req.va}->${io.resp.pa}\n")
            }
        } 

        when(!found){
           // printf("not found\n")
            io.resp.exception := ET_TLB_REFILL
        }
    }
}