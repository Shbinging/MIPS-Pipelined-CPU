package njumips

class IF_ID_Reg extends Bundle{
    val id_en = Output(Bool())
    val instr = Output(UInt(conf.data_width.W))
}

class InstrFetch extends Module{
    val io = IO(new Bundle{
        val en = Input(Bool())
        val pc = Input(conf.addr_width.W)
        val pc_writer = Flipped(new PCInput)
        val if_id = new IF_ID
    })

    val dev = Module(new SimDev)

    val pc_writer_reg = new Bundle{
        val w_en = Input(io.en)
        val w_data = RegEnable(io.pc+4.U, io.en)
    }
    val if_id_reg = new Bundle{
        val if_commit = RegNext(io.en)
        val instr = RegEnable(dev.io.in.resp.bits.data, io.en)
    }

    dev.io.clock := clock
    dev.io.reset := reset
    
    dev.io.in.req.bits.is_cached := DontCare
    dev.io.in.req.bits.strb := DontCare
    dev.io.in.req.bits.data := DontCare
    dev.io.in.req.func := MX_RD

    when(io.en){
        dev.io.in.req.bits.is_aligned := true.B 
        dev.io.in.req.bits.addr := io.pc_io.pc
        dev.io.in.req.bits.len := 4.U 
        dev.io.in.req.valid := true.B
    } .otherwise{
        dev.io <> DontCare
    }

    io.pc_writer <> pc_writer
    io.if_id <> if_id_reg
}