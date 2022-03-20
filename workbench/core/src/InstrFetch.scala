package njumips
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._


class InstrFetch extends Module{
    val io = IO(new Bundle{
        val wb_if = Flipped(Decoupled(new RB_IF))
        val pc = Output(UInt(conf.addr_width.W))
        val if_id = Decoupled(new IF_ID)
    })
    io.wb_if.ready := true.B    // unidir hand shaking

    val pc_reg = RegInit(conf.start_addr)
    io.pc := pc_reg

    when(io.wb_if.fire() && io.wb_if.bits.pc_w_en){
        pc_reg := io.wb_if.bits.pc_w_data
    }
    val wb_if_fire = RegNext(io.wb_if.fire() | reset.asBool())
    // val wb_if_reg = RegEnableUse(io.wb_if.bits, io.wb_if.fire())

    val dev = Module(new SimDev)
    dev.io.clock := clock
    dev.io.reset := reset.asBool()
    
    dev.io.in.req.bits.is_cached := DontCare
    dev.io.in.req.bits.strb := DontCare
    dev.io.in.req.bits.data := DontCare
    
    dev.io.in.req.bits.func := MX_RD
    dev.io.in.req.bits.addr := io.pc
    dev.io.in.req.bits.len := 3.U   // 00, 01, 10, 11
    dev.io.in.req.valid := true.B

    when(wb_if_fire){
        pc_reg := pc_reg+4.U
    }
    
    io.if_id.bits.instr := dev.io.in.resp.bits.data
    io.if_id.valid := wb_if_fire & ~reset.asBool()  // complete in 1 cycle
}