package njumips
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._


class InstrFetch extends Module{
    val io = IO(new Bundle{
        val wb_if = Flipped(Decoupled(new RB_IF))
        val flush = Input(Bool())
        val if_id = Decoupled(new IF_ID)
    })
    printf(p"wb_if_fire: ${io.wb_if.fire()}\n")
    val wb_if_reg = RegEnable(next=io.wb_if.bits, enable=io.wb_if.fire())
    val wb_if_reg_prepared = RegInit(false.B)
    val pc_reg = RegInit(conf.start_addr)
    printf(p"pc_reg: ${pc_reg}\n")

    val dev = Module(new SimDev)
    dev.io.clock := clock
    dev.io.reset := reset.asBool()
    
    io.wb_if.ready := !wb_if_reg_prepared || dev.io.in.req.fire()
    when(dev.io.in.resp.fire()){
        pc_reg := Mux(wb_if_reg_prepared&wb_if_reg.pc_w_en, wb_if_reg.pc_w_data, pc_reg+4.U)
    }
    printf(p"dev ready:${dev.io.in.req.ready}\n")    
    dev.io.in.req.bits.is_cached := DontCare
    dev.io.in.req.bits.strb := DontCare
    dev.io.in.req.bits.data := DontCare

    dev.io.in.req.bits.func := MX_RD
    dev.io.in.req.bits.addr := pc_reg
    dev.io.in.req.bits.len := 3.U   // 00, 01, 10, 11
    dev.io.in.req.valid := true.B
    
    when(io.flush || (!io.wb_if.fire() && dev.io.in.req.fire())){
        wb_if_reg_prepared := false.B
    } .elsewhen(!io.flush && io.wb_if.fire()){
        wb_if_reg_prepared := true.B
    }
    
    val if_id_instr = RegEnable(dev.io.in.resp.bits.data, dev.io.in.resp.fire())
    val if_id_instr_prepared = RegInit(false.B)
    val if_id_next_pc = RegEnable(pc_reg + 4.U, dev.io.in.req.fire())
    dev.io.in.resp.ready := io.if_id.ready || !if_id_instr_prepared

    when(io.flush || (!dev.io.in.resp.fire() && io.if_id.fire())){
        if_id_instr_prepared := false.B
    } .elsewhen(!io.flush && dev.io.in.resp.fire()){
        if_id_instr_prepared := true.B
    }

    io.if_id.valid := if_id_instr_prepared
    io.if_id.bits.instr := if_id_instr
    io.if_id.bits.pcNext := if_id_next_pc
}