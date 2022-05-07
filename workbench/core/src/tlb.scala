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
        val in = new WrTLB
        val entries = Output(Vec(conf.tlb_size, new TLBEntry))
    })

    val tlb = Mem(conf.tlb_size, new TLBEntry)
    for(i <- 0 to conf.tlb_size-1){
        io.entries(i) := tlb(i)
    }
}

class TLBTranslator extends Module{
    val io = IO(new Bundle{
        val tlb = Input(Vec(conf.tlb_size, new TLBEntry))
        val asid = Input(UInt(8.W))
        val va = Input(UInt(conf.addr_width.W))
        val ref_type = Input(UInt(MX_SZ.W))
        val pa = Output(UInt(conf.addr_width.W))
        val tlb_invalid_exception = Output(Bool())
        val tlb_modified_execption = Output(Bool())
        val tlb_miss_exception = Output(Bool())
    })
    // FIX Page Size 4096 kB
    val found = Bool()
    val entry = Wire(new EntryLo)
    found := false.B
    io.tlb_invalid_exception := false.B 
    io.tlb_modified_execption := false.B
    io.tlb_miss_exception := false.B 

    for(i <- 0 to conf.tlb_size-1){
        when(
            (io.tlb(i).hi.vpn2 === (io.va>>12)) && (
                (io.tlb(i).lo_0.global & io.tlb(i).lo_1.global) ||
                (io.tlb(i).hi.asid === io.asid)
            )
        ){
            entry := Mux(io.va(13)===0.U, io.tlb(i).lo_0, io.tlb(i).lo_1)
            when(entry.valid === false.B){
                io.tlb_invalid_exception := true.B
            } .elsewhen(entry.dirty===false.B && io.ref_type===MX_WR){
                io.tlb_modified_execption := true.B
            }
            io.pa := Cat(entry.pfn(31, 12), io.va(11, 0))
            found := true.B
        }
    }

    when(!found){
        io.tlb_miss_exception := true.B
    }
}