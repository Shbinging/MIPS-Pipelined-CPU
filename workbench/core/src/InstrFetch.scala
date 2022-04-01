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
        val cycledone = Output(Bool())
    })
    val count = RegInit(1.U(32.W))
    count := count + 1.U
    
    io.wb_if.ready := true.B    // unidir hand shaking

    val commit_pc = RegInit(conf.start_addr)
    io.pc := commit_pc

    val pc_reg = RegInit(conf.start_addr)
    
    val wb_pc = RegInit(conf.start_addr)
    val wb_pc_en = RegInit(false.B)
    when(io.wb_if.fire() && io.wb_if.bits.pc_w_en){
        wb_pc_en := io.wb_if.bits.pc_w_en
        wb_pc := io.wb_if.bits.pc_w_data
        // pc_reg := io.wb_if.bits.pc_w_data
        // printf(p"writing pc: ${pc_reg}\n")
    }
    val wb_if_fire = RegNext(io.wb_if.fire() | reset.asBool())
    io.cycledone := wb_if_fire
    // printf(p"fetch working: ${wb_if_fire}\n")

    val dev = Module(new SimDev)
    dev.io.clock := clock
    dev.io.reset := reset.asBool()
    
    dev.io.in.req.bits.is_cached := DontCare
    dev.io.in.req.bits.strb := DontCare
    dev.io.in.req.bits.data := DontCare
    
    dev.io.in.req.bits.func := MX_RD
    dev.io.in.req.bits.addr := pc_reg
    dev.io.in.req.bits.len := 3.U   // 00, 01, 10, 11
    dev.io.in.req.valid := wb_if_fire
    
    dev.io.in.resp.ready := true.B // & ~reset.asBool()

    val instr_reg = RegEnable(dev.io.in.resp.bits.data, dev.io.in.resp.fire())
    val instr_fired = RegNext(dev.io.in.resp.fire())

    when(instr_fired){
        commit_pc := pc_reg
        when(wb_pc_en){
            pc_reg := wb_pc
            wb_pc_en := false.B
        } .otherwise{
            pc_reg := pc_reg+4.U
        }
    }
    
    io.if_id.bits.instr := instr_reg
    io.if_id.bits.pcNext := pc_reg+4.U
    io.if_id.valid := instr_fired // wb_if_fire & ~reset.asBool()  // complete in 1 cycle
    // printf(p"#${count} io_if_id_fire: (reset ${reset.asBool()}) - ${io.if_id.fire()}\n")
}