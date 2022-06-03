package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import firrtl.options.DoNotTerminateOnExit

class PRU extends Module{
    val io = IO(new Bundle{
        val isu_pru = Flipped(Decoupled(new ISU_PRU))
        val flush = Input(Bool())
        val exec_wb = Decoupled(new PRU_WB)

        val cp0_taglo = Input(UInt(conf.data_width.W))
        val cp0_taghi = Input(UInt(conf.data_width.W))
        val icache_cmd = new CacheCommandIO
        val dcache_cmd = new CacheCommandIO
        
        val cp0_index = Input(UInt(conf.data_width.W))
        val cp0_random = Input(UInt(conf.data_width.W))
        val cp0_entryhi = Input(new EntryHi)
        val cp0_entrylo_0 = Input(new EntryLo)
        val cp0_entrylo_1 = Input(new EntryLo)
        val cp0_status = Input(new cp0_Status_12)
        val cp0_cause = Input(new cp0_Cause_13)
        val cp0_badAddr = Input(new cp0_BadVaddr_8)
        val cp0_epc = Input(new cp0_Epc_14)
        val cp0_compare = Input(UInt(conf.data_width.W))
        val cp0_count = Input(UInt(conf.data_width.W))

        val tlb_entries = Input(Vec(conf.tlb_size, new TLBEntry))
        val tlb_req = Flipped(new TLBTranslatorReq)
        val tlb_resp = Flipped(new TLBTranslatorResp)
        val tlb_wr = new TLBEntryIO


        val out_index_sel_0 = Flipped(new CP0WriteInput)
        val out_cause_sel_0 = Flipped(new CP0WriteInput)
        val out_status_sel_0 = Flipped(new CP0WriteInput)
        val out_epc_sel_0 = Flipped(new CP0WriteInput)
        val out_badAddr_sel_0 = Flipped(new CP0WriteInput)
        val out_context_sel_0 = Flipped(new CP0WriteInput)
        val out_entryhi_sel_0 = Flipped(new CP0WriteInput)
        val out_entrylo0_sel_0 = Flipped(new CP0WriteInput)
        val out_entrylo1_sel_0 = Flipped(new CP0WriteInput)
        val out_compare_sel_0 = Flipped(new CP0WriteInput)
        val out_count_sel_0 = Flipped(new CP0WriteInput)
    })
    io.out_cause_sel_0 <> DontCare
    io.out_epc_sel_0 <> DontCare
    io.out_status_sel_0 <> DontCare
    io.out_badAddr_sel_0 <> DontCare
    io.out_context_sel_0 <> DontCare
    io.out_index_sel_0 <> DontCare
    io.out_cause_sel_0 <> DontCare
    io.out_status_sel_0 <> DontCare
    io.out_epc_sel_0 <> DontCare
    io.out_badAddr_sel_0 <> DontCare
    io.out_context_sel_0 <> DontCare
    io.out_entryhi_sel_0 <> DontCare
    io.out_entrylo0_sel_0 <> DontCare
    io.out_entrylo1_sel_0 <> DontCare
    io.out_compare_sel_0 <> DontCare
    io.out_count_sel_0 <> DontCare
    io.out_cause_sel_0.en := N
    io.out_epc_sel_0.en := N
    io.out_status_sel_0.en := N
    io.out_badAddr_sel_0.en := N
    io.out_context_sel_0.en := N
    io.out_index_sel_0.en := N
    io.out_cause_sel_0.en := N
    io.out_status_sel_0.en := N
    io.out_epc_sel_0.en := N
    io.out_badAddr_sel_0.en := N
    io.out_context_sel_0.en := N
    io.out_entryhi_sel_0.en := N
    io.out_entrylo0_sel_0.en := N
    io.out_entrylo1_sel_0.en := N
    io.out_count_sel_0.en := N
    io.out_compare_sel_0.en := N

    val isu_pru_prepared = RegInit(N)
    io.isu_pru.ready := io.exec_wb.fire() || !isu_pru_prepared
    
    val r = RegEnable(io.isu_pru.bits, io.isu_pru.fire())
    
    io.exec_wb.bits := DontCare
    io.tlb_wr <> DontCare
    io.dcache_cmd <> DontCare
    io.icache_cmd <> DontCare
    io.tlb_req <> DontCare
    io.exec_wb.bits.current_pc := r.current_pc
    io.exec_wb.bits.current_instr := r.current_instr
    io.exec_wb.bits.error.enable := N
    io.exec_wb.bits.needCommit := N
    io.exec_wb.bits.error.EPC := r.current_pc

    io.icache_cmd.en := false.B
    io.dcache_cmd.en := false.B
    io.exec_wb.bits.eret.en := N
    io.exec_wb.bits.mft.en := N
    io.exec_wb.bits.tlbp.en := N
    io.exec_wb.bits.tlbr.en := N

    io.tlb_wr.en := false.B
    when(io.exec_wb.fire()){
        //printf("@pru pru op is %d\n", r.pru_op)
    }
    
    switch(r.pru_op){
        is(PRU_RI_OP){
            io.exec_wb.bits.error.enable := Y
            io.exec_wb.bits.error.excType := ET_RI
            io.exec_wb.bits.error.exeCode := EC_RI
            io.exec_wb.bits.error.EPC := r.current_pc
            io.exec_wb.bits.needCommit := Y
        }
        is(PRU_SYSCALL_OP){
            io.exec_wb.bits.error.enable := Y
            io.exec_wb.bits.error.excType := ET_Sys
            io.exec_wb.bits.error.exeCode := EC_Sys
            io.exec_wb.bits.error.EPC := r.current_pc
            io.exec_wb.bits.needCommit := Y
        }
        is(PRU_BREAK_OP){
            io.exec_wb.bits.error.enable := Y
            io.exec_wb.bits.error.excType := ET_Sys
            io.exec_wb.bits.error.exeCode := EC_Bp
            io.exec_wb.bits.error.EPC := r.current_pc
            io.exec_wb.bits.needCommit := Y
        }
        is(PRU_EXCEPT_OP){
            io.exec_wb.bits.error <> r.except_info
            io.exec_wb.bits.needCommit := Y
            io.exec_wb.bits.current_pc := r.current_pc
            io.exec_wb.bits.current_instr := r.current_instr
        }
        is(PRU_CACHE_OP){
            val address = (r.rs_data.asSInt() + r.current_instr(15, 0).asSInt()).asUInt()(31, 0)
            val target_cache = r.current_instr(17, 16)
            val operation = r.current_instr(20, 18)
            when(operation === "b010".U){
                // assume it is only used for setup
                //printf(p"reset cache: tag lo${io.cp0_taglo}, tag hi${io.cp0_taghi}")
            }
            io.tlb_req.va := address
            io.tlb_req.ref_type := MX_RD
            when(target_cache === "b00".U){         // ICache
                when(operation(2)===0.U){    // index
                    io.icache_cmd.en := true.B
                    io.icache_cmd.addr := address 
                    io.icache_cmd.code := operation
                } .otherwise{
                    when(io.tlb_resp.exception === ET_None){
                        io.icache_cmd.en := true.B
                        io.icache_cmd.addr := io.tlb_resp.pa
                        io.icache_cmd.code := operation
                    } .otherwise{
                        io.exec_wb.bits.error.enable := Y
                        io.exec_wb.bits.error.excType := io.tlb_resp.exception
                        io.exec_wb.bits.error.exeCode := EC_TLBL
                        io.exec_wb.bits.error.EPC := r.current_pc
                        io.exec_wb.bits.needCommit := N
                    }
                }
            } .elsewhen(target_cache === "b01".U){  // DCache
                when(operation(2)===0.U){    // index
                    io.dcache_cmd.en := true.B
                    io.dcache_cmd.addr := address 
                    io.dcache_cmd.code := operation
                } .otherwise{
                    when(io.tlb_resp.exception === ET_None){
                        io.dcache_cmd.en := true.B
                        io.dcache_cmd.addr := io.tlb_resp.pa
                        io.dcache_cmd.code := operation
                    } .otherwise{
                        io.exec_wb.bits.error.enable := Y
                        io.exec_wb.bits.error.excType := io.tlb_resp.exception
                        io.exec_wb.bits.error.exeCode := EC_TLBL
                        io.exec_wb.bits.error.EPC := r.current_pc
                        io.exec_wb.bits.needCommit := N
                    }
                }
            } .elsewhen(target_cache === "b11".U){  // L2 Cache
                //printf("L2 Cache Command, ignore it.\n")
            } .otherwise{
                //printf("Unexpected Cache!\n")
            }
        }
        is(PRU_TLBP_OP){
            val index = WireInit(f"h_8000_0000".U(32.W))
            for(i <- 0 to conf.tlb_size-1){
                when((io.tlb_entries(i).hi.vpn2 === io.cp0_entryhi.vpn2) && (
                        (io.tlb_entries(i).lo_0.global & io.tlb_entries(i).lo_1.global) ||
                        (io.tlb_entries(i).hi.asid === io.cp0_entryhi.asid)
                    )
                ){
                    index := i.U(32.W)
                }
            }
            io.exec_wb.bits.tlbp.en := true.B 
            io.exec_wb.bits.tlbp.index_data := index            
        }
        is(PRU_TLBR_OP){
            io.exec_wb.bits.tlbr.en := true.B
            io.exec_wb.bits.tlbr.entryhi := io.tlb_entries(io.cp0_index).hi 
            io.exec_wb.bits.tlbr.entrylo_0 := io.tlb_entries(io.cp0_index).lo_0
            io.exec_wb.bits.tlbr.entrylo_1 := io.tlb_entries(io.cp0_index).lo_1
        }
        is(PRU_TLBWI_OP){
            io.tlb_wr.en := true.B 
            io.tlb_wr.index := io.cp0_index
            io.tlb_wr.hi := io.cp0_entryhi
            io.tlb_wr.lo_0 := io.cp0_entrylo_0
            io.tlb_wr.lo_1 := io.cp0_entrylo_1
            when(io.cp0_entrylo_0.global=/=io.cp0_entrylo_1.global){
                io.tlb_wr.lo_0.global := 0.U(1.W)
                io.tlb_wr.lo_1.global := 0.U(1.W)
            }
        }
        is(PRU_TLBWR_OP){
            io.tlb_wr.en := true.B 
            io.tlb_wr.index := io.cp0_random
            io.tlb_wr.hi := io.cp0_entryhi
            io.tlb_wr.lo_0 := io.cp0_entrylo_0
            io.tlb_wr.lo_1 := io.cp0_entrylo_1
        }
        is(PRU_ERET_OP){
            io.exec_wb.bits.needCommit := Y
            io.exec_wb.bits.eret.en := Y
            io.exec_wb.bits.eret.w_pc_addr := io.cp0_epc.epc
        }
        is(PRU_MFC0_OP){
            io.exec_wb.bits.needCommit := Y
            io.exec_wb.bits.mft.en := Y
            io.exec_wb.bits.mft.destSel := N
            io.exec_wb.bits.mft.destAddr := r.rd_addr
            io.exec_wb.bits.mft.CP0Sel := r.rt_addr(2, 0)
            //assert(io.exec_wb.bits.mft.CP0Sel === 0.U)
            //XXX:sel = 0
           // printf("@pru r.rs_addr %d io.cp0_epc.asUInt %x\n", r.rs_addr, io.cp0_epc.epc)
            io.exec_wb.bits.mft.data := MuxLookup(r.rs_addr, 0.U, Array(
                index_cp0_index -> io.cp0_index.asUInt(),
                index_cp0_random -> io.cp0_random.asUInt(),
                index_cp0_entrylo0 -> io.cp0_entrylo_0.asUInt(),
                index_cp0_entrylo1 -> io.cp0_entrylo_1.asUInt(),
                index_cp0_badvaddr -> io.cp0_badAddr.asUInt(),
                index_cp0_entryhi -> io.cp0_entryhi.asUInt(),
                index_cp0_cause -> io.cp0_cause.asUInt(),
                index_cp0_epc -> io.cp0_epc.epc,
                index_cp0_status -> io.cp0_status.asUInt(),
                index_cp0_compare -> io.cp0_compare,
                index_cp0_count -> io.cp0_count
            ))
        }
        is(PRU_MTC0_OP){
            io.exec_wb.bits.needCommit := Y
            io.exec_wb.bits.mft.en := Y
            io.exec_wb.bits.mft.destSel := Y
            io.exec_wb.bits.mft.destAddr := r.rd_addr
            io.exec_wb.bits.mft.CP0Sel := r.rt_addr(2, 0)
            //printf("@pru destAddr %d data %x\n", r.rd_addr, r.rs_data)
            //assert(io.exec_wb.bits.mft.CP0Sel === 0.U)
            io.exec_wb.bits.mft.data := r.rs_data
            def mergeStatus(a:UInt) = ((a & "b1111_1010_0111_1000_1111_1111_0001_0111".U) | (io.cp0_status.asUInt() & "b0000_0101_1000_0000_0000_0000_1110_0000".U)).asTypeOf(new cp0_Status_12)
            def mergeCause(a:UInt) = ((a & "b0000_0000_1100_0000_0000_0011_0000_0000".U ).asUInt | (io.cp0_cause.asUInt() &"b1011_0000_0000_0000_1111_1100_0111_1100".U)).asTypeOf(new cp0_Cause_13)
            def canWrite() = !io.flush && isu_pru_prepared
            switch(r.rd_addr){
                    is(index_cp0_badvaddr){
                        io.out_badAddr_sel_0.en := canWrite()
                        io.out_badAddr_sel_0.data := r.rs_data
                    }
                    is(index_cp0_cause){
                        io.out_cause_sel_0.en := canWrite()
                        io.out_cause_sel_0.data := mergeCause(r.rs_data).asUInt//(reg_pru_wb.mft.data & "b0000_0000_1100_0000_0011_0000_0000".U )| (io.cp0_cause.asUInt() &"b1011_0000_0000_0000_1111_1100_0111_1100".U)
                    }
                    is(index_cp0_epc){
                        io.out_epc_sel_0.en := canWrite()
                        io.out_epc_sel_0.data := r.rs_data
                    }
                    is(index_cp0_status){
                        io.out_status_sel_0.en := canWrite()
                        io.out_status_sel_0.data := mergeStatus(r.rs_data).asUInt()
                    }
                    is(index_cp0_index){
                        io.out_index_sel_0.en := canWrite() 
                        io.out_index_sel_0.data := r.rs_data
                    }
                    is(index_cp0_compare){
                        io.out_cause_sel_0.en := canWrite()
                        io.out_cause_sel_0.data := io.cp0_cause.asUInt & "hffff_7fff".U
                        io.out_compare_sel_0.en := canWrite()
                        printf("@pru write compare en %d data %x\n", canWrite(), r.rs_data)
                        io.out_compare_sel_0.data := r.rs_data
                    }
                    is(index_cp0_count){
                        io.out_count_sel_0.en := canWrite()
                        io.out_count_sel_0.data := r.rs_data
                    }
                    is(index_cp0_entrylo0){
                        io.out_entrylo0_sel_0.en := canWrite() 
                        io.out_entrylo0_sel_0.data := r.rs_data & "h_03ff_ffff".U
                    }
                    is(index_cp0_entrylo1){
                        io.out_entrylo1_sel_0.en := canWrite() 
                        io.out_entrylo1_sel_0.data := r.rs_data & "h_03ff_ffff".U
                    }
                    is(index_cp0_entryhi){
                        io.out_entryhi_sel_0.en := canWrite() 
                        io.out_entryhi_sel_0.data := r.rs_data & "h_ffffe0ff".U
                    }
                }
        }
    }
    when (io.flush || (!io.isu_pru.fire() && io.exec_wb.fire())) {
        isu_pru_prepared := N
    } .elsewhen (!io.flush && io.isu_pru.fire()) {
        isu_pru_prepared := Y
    }
    io.exec_wb.valid := isu_pru_prepared && !io.flush
}

class ALU extends Module{
    val io = IO{new Bundle{
        val isu_alu = Flipped(Decoupled(new ISU_ALU))
        val exec_wb = Decoupled(new ALU_WB)
        val flush = Input(Bool())
        val exec_pass = new ALU_PASS
    }}
    io.exec_wb.bits.error := DontCare
    io.exec_wb.bits.error.enable := N
    val isu_alu_prepared = RegNext(false.B)
    val r = RegEnable(io.isu_alu.bits, io.isu_alu.fire())
    io.isu_alu.ready := io.exec_wb.fire() || !isu_alu_prepared
    val A_in = WireInit(r.operand_1)
    val B_in = WireInit(r.operand_2)
    val ALU_op = WireInit(r.alu_op)
    val ALU_out = Wire(UInt(32.W))  
    
    // for ins and ext
    val msb = r.imm(15, 11).asUInt
    val lsb = r.imm(10, 6).asUInt
    val a_mask = (("h_ffff_ffff".U(32.W))>>(31.U(5.W)-msb+lsb)).asUInt()(31, 0)
    val b_mask = (~((a_mask<<lsb))).asUInt()(31, 0)

    // for rot
    val rot = Cat(B_in, 0.U(32.W)) >> A_in

    // for clz clo
    val A_in_Count = Mux(r.alu_op===ALU_CLO_OP, ~A_in, A_in)
    val tocount = Wire(Vec(32, Bool()))
    val count = Wire(Vec(32, UInt(6.W)))
    for(i <- 31 to 0 by -1){
        if(i==31) tocount(i) := (A_in_Count(i)===0.U)
        else tocount(i) := tocount(i+1) & (A_in_Count(i)===0.U)
    }
    for(i <- 31 to 0 by -1){
        if(i==31) count(i) := Mux(tocount(i), 1.U, 0.U)
        else count(i) := Mux(tocount(i), count(i+1)+1.U, count(i+1))
    }

    ALU_out := MuxCase(0.U, Array(
        (r.alu_op === ALU_ADDU_OP)-> (A_in + B_in),
        (r.alu_op === ALU_ADD_OP)-> (A_in.asSInt() + B_in.asSInt()).asUInt(),
        (r.alu_op === ALU_AND_OP)-> (A_in & B_in),
        (r.alu_op === ALU_LUI_OP)-> (B_in << 16),
        (r.alu_op === ALU_NOR_OP)-> (~(A_in | B_in)),
        (r.alu_op === ALU_OR_OP) -> (A_in | B_in),
        (r.alu_op === ALU_SLL_OP)-> (B_in << A_in(4, 0).asUInt()),
        (r.alu_op === ALU_SLTU_OP) -> (Mux((A_in < B_in).asBool(), 1.U, 0.U)),
        (r.alu_op === ALU_SLT_OP) -> (Mux((A_in.asSInt() < B_in.asSInt()).asBool(), 1.U, 0.U)),
        (r.alu_op === ALU_SRA_OP) -> (B_in.asSInt() >> A_in(4, 0).asUInt()).asUInt(),
        (r.alu_op === ALU_SRL_OP) -> (B_in >> A_in(4, 0).asUInt()),
        (r.alu_op === ALU_ROTR_OP) -> (rot(63, 32) + rot(31, 0)),
        (r.alu_op === ALU_SUBU_OP) -> (A_in - B_in),
        (r.alu_op === ALU_SUB_OP) -> (A_in.asSInt() - B_in.asSInt()).asUInt(),
        (r.alu_op === ALU_XOR_OP) -> (A_in ^ B_in),
        (r.alu_op === ALU_SEB_OP) -> Cat(Fill(24, B_in(7)), B_in(7, 0)),
        (r.alu_op === ALU_SEH_OP) -> Cat(Fill(16, B_in(15)), B_in(15, 0)),
        (r.alu_op === ALU_WSBH_OP)-> (Cat(Cat(B_in(23, 16), B_in(31, 24)), Cat(B_in(7, 0), B_in(15, 8)))),
        (r.alu_op === ALU_INS_OP) -> (((A_in&a_mask)<<lsb).asUInt()(31, 0) + (B_in&b_mask)),
        (r.alu_op === ALU_EXT_OP) -> ((A_in>>lsb) & ("h_ffff_ffff".U(32.W)>>(31.U - msb))),
        (r.alu_op === ALU_CLO_OP) -> count(0),
        (r.alu_op === ALU_CLZ_OP) -> count(0),
        (r.alu_op === ALU_MOVZ_OP)-> (A_in),
        (r.alu_op === ALU_MOVN_OP)-> (A_in)
    ))(31, 0)
    //io.out.ALU_out := ALU_out
    //io.out.Overflow_out := false.B //XXX:modify when need exception
    //io.exec_wb.bits.
    io.exec_wb.bits.ALU_out := ALU_out
    io.exec_wb.bits.Overflow_out := false.B //XXX:modify when need exception
    //OVERFLOW
    when(r.alu_op === ALU_ADD_OP){ 
        when(A_in(31) === B_in(31) && B_in(31) =/= ALU_out(31)){
            when(io.exec_wb.fire()){
                //printf("@%x %x+%x=%x add overflow!\n", r.current_pc, A_in, B_in, ALU_out)
            }
            io.exec_wb.bits.error.enable := Y
            io.exec_wb.bits.error.EPC := r.current_pc
            io.exec_wb.bits.error.excType := ET_Ov
            io.exec_wb.bits.error.exeCode := EC_Ov
        }
    }
    when(r.alu_op === ALU_SUB_OP){
        when(A_in(31) === (-(B_in.asSInt())).asUInt()(31) && A_in(31) =/= ALU_out(31)){
            when(io.exec_wb.fire()){
                //printf("@%x %x+%x=%x sub overflow!\n", r.current_pc, A_in, B_in, ALU_out)
            }
            io.exec_wb.bits.error.enable := Y
            io.exec_wb.bits.error.EPC := r.current_pc
            io.exec_wb.bits.error.excType := ET_Ov
            io.exec_wb.bits.error.exeCode := EC_Ov
        }
    }


    io.exec_wb.bits.w_addr := r.rd_addr // io.isu_alu.bits.rd_addr
    when(r.alu_op === ALU_MOVZ_OP){
        io.exec_wb.bits.w_en := isu_alu_prepared && (B_in===0.U)
        // printf(p"ALU_MOVZ: @ ${r.current_pc} ${A_in}, ${B_in}, ${io.exec_wb.bits.w_en}\n")
        // printf(p"ALU EXEC valid?: ${io.exec_wb.valid} ${isu_alu_prepared} && ${!io.flush}\n")
    } .elsewhen(r.alu_op === ALU_MOVN_OP){
        io.exec_wb.bits.w_en := isu_alu_prepared && (B_in=/=0.U)
    }.otherwise{
        io.exec_wb.bits.w_en := isu_alu_prepared    // XXX:
    }
    io.exec_wb.bits.current_pc := r.current_pc
    io.exec_wb.bits.current_instr := r.current_instr
    io.exec_wb.valid := isu_alu_prepared && !io.flush // 1 cycle 
    io.exec_pass.ALU_out := ALU_out
    io.exec_pass.w_addr := r.rd_addr

    when(r.alu_op === ALU_MOVZ_OP){
        io.exec_pass.w_en := isu_alu_prepared && (B_in===0.U)
        io.exec_pass.rm_dirty := (B_in=/=0.U)
    } .elsewhen(r.alu_op === ALU_MOVN_OP){
        io.exec_pass.w_en := isu_alu_prepared && (B_in=/=0.U)
        io.exec_pass.rm_dirty := (B_in===0.U)
    } .otherwise{
        io.exec_pass.w_en := isu_alu_prepared    
        io.exec_pass.rm_dirty := false.B
    }
    
    when (io.flush || (!io.isu_alu.fire() && io.exec_wb.fire())) {
        isu_alu_prepared := N
    } .elsewhen (!io.flush && io.isu_alu.fire()) {
        isu_alu_prepared := Y
    }
    when(isu_alu_prepared){
        //printf("ALU WORKING\n");
    }
}

class BRU extends Module{
    val io = IO{new Bundle{
        val isu_bru = Flipped(Decoupled(new ISU_BRU))
        val exec_wb = Decoupled(new BRU_WB)
        val flush = Input(Bool())
    }}    
    val isu_bur_fire = RegNext(false.B)
    val r = RegEnableUse(io.isu_bru.bits, io.isu_bru.fire())
    io.isu_bru.ready := io.exec_wb.fire() || !isu_bur_fire
    when(isu_bur_fire){
        //printf("BRU WORKING\n");
    }
    val bruwb = Wire(new BRU_WB)
    bruwb := DontCare
    bruwb.w_en := false.B
    bruwb.w_pc_en := false.B 
    bruwb.noSlot := false.B
    val needJump = MuxLookup(r.bru_op, false.B, Array(
        BRU_BEQ_OP -> (r.rsData === r.rtData).asBool(),
        BRU_BNE_OP -> (r.rsData =/= r.rtData).asBool(),
        BRU_BGEZ_OP -> (r.rsData.asSInt() >= 0.S).asBool(),
        BRU_BGTZ_OP -> (r.rsData.asSInt() > 0.S).asBool(),
        BRU_BLEZ_OP -> (r.rsData.asSInt() <= 0.S).asBool(),
        BRU_BLTZ_OP -> (r.rsData.asSInt() < 0.S).asBool(),
        BRU_BGEZAL_OP -> (r.rsData.asSInt() >= 0.S).asBool(),
        BRU_BLTZAL_OP -> (r.rsData.asSInt() < 0.S).asBool(),
        BRU_J_OP -> true.B,
        BRU_JAL_OP -> true.B,
        BRU_JR_OP -> true.B,
        BRU_JALR_OP -> true.B,
        BRU_ERET_OP -> true.B
    ))
    when(needJump){
        bruwb.w_pc_en := true.B
        bruwb.w_pc_addr := MuxLookup(r.bru_op, (r.pcNext.asSInt() + (r.offset << 2).asSInt()).asUInt(), Array(
            BRU_J_OP -> Cat(r.pcNext(31, 28), Cat(r.instr_index, "b00".U(2.W))),
            BRU_JAL_OP -> Cat(r.pcNext(31, 28), Cat(r.instr_index, "b00".U(2.W))),
            BRU_JR_OP -> r.rsData,
            BRU_JALR_OP -> r.rsData,
            BRU_ERET_OP -> r.rsData
        ))
        // when(r.bru_op === BRU_JALR_OP){
        //     printf(p"BRANCH WRITE TO ${r.rd} ${r.bru_op}\n")
        //     bruwb.w_en := true.B
        //     bruwb.w_addr := r.rd
        //     bruwb.w_data := r.pcNext + 4.U
        // }
    }
    when(r.bru_op === BRU_ERET_OP){
        bruwb.noSlot := Y
    }
    when(VecInit(BRU_BGEZAL_OP, BRU_BLTZAL_OP, BRU_JAL_OP, BRU_JALR_OP).contains(r.bru_op)){
        // when(r.bru_op === BRU_JAL_OP){
        //     printf("jal ok %d\n", bruwb.w_data);
        // }
        bruwb.w_en := true.B
        bruwb.w_addr := Mux(r.bru_op===BRU_JALR_OP, r.rd, 31.U)
        bruwb.w_data := r.pcNext + 4.U
    }
//bruwb.w_pc_addr := 

    when (io.flush || (!io.isu_bru.fire() && io.exec_wb.fire())) {
        isu_bur_fire := N
    } .elsewhen (!io.flush && io.isu_bru.fire()) {
        isu_bur_fire := Y
    }
    bruwb.current_pc := r.current_pc
    bruwb.current_instr := r.current_instr
    io.exec_wb.bits <> bruwb
    io.exec_wb.valid := isu_bur_fire && !io.flush
}

class LSU extends Module{
    val io = IO{new Bundle{
        val isu_lsu = Flipped(Decoupled(new ISU_LSU))
        val tlb_req = Flipped(new TLBTranslatorReq)
        val tlb_resp = Flipped(new TLBTranslatorResp)
        val dcache = new CacheIO
        val exec_wb = Decoupled(new LSU_WB)
        val flush = Input(Bool())
    }}
    io.exec_wb.bits <> DontCare // FIXME
    //printf("io.exec_wb.valid %d io.isu_lsu.ready %d\n", io.exec_wb.valid, io.isu_lsu.ready)
    io.exec_wb.valid:=false.B
    val isu_lsu_fire = RegInit(false.B)
    io.isu_lsu.ready := io.exec_wb.fire() || !isu_lsu_fire
    val r = RegEnable(io.isu_lsu.bits, io.isu_lsu.fire())
    val state_reg = RegInit(LSU_DIE)

    // when(state_reg=/=LSU_DIE){
    //     printf("LSU WORKING\n")
    // }
    // printf("io.flush %d fire %d\n", io.flush, io.isu_lsu.fire())
    when (io.flush || (!io.isu_lsu.fire() && io.exec_wb.fire())) {
        isu_lsu_fire := N
    } .elsewhen (!io.flush && io.isu_lsu.fire()) {
        isu_lsu_fire := Y
        state_reg := LSU_DECODE
        // printf("state_reg %d\n", LSU_DECODE)
    }

	val back_reg = Reg(new Bundle{
		val w_en = UInt(4.W)
    	val w_addr = UInt(REG_SZ.W)
		val w_data = UInt(conf.data_width.W)
	})
	val read_reg = Reg(new Bundle{
		val addr = UInt(conf.data_width.W)
		val len = UInt(2.W)
		val en = Bool()
	})
	val exec_reg = Reg(new Bundle{
		val preRead = Bool()
		val func = UInt(4.W)
		val preReadData = UInt(conf.data_width.W)//need calc
        val preReadDataFull = UInt(conf.data_width.W)
	})
	val write_reg = Reg(new Bundle{
		val addr = UInt(conf.data_width.W)
		val strb = UInt(4.W)
		val en = Bool()
		val w_data = UInt(conf.data_width.W)
	})
    val except_reg = Reg(new exceptionInfo)
	// val dev = Module(new SimDev)
	// dev.io.clock := clock
    // dev.io.reset := reset.asBool() 
    io.tlb_req <> DontCare
    io.dcache.req.bits <> DontCare
    io.dcache.req.valid := false.B
    io.dcache.resp.ready := false.B
	io.exec_wb.valid := false.B
    io.exec_wb.bits.error := DontCare
    io.exec_wb.bits.error.enable := N
    val isLoadException = RegInit(N)
    val isStoreException = RegInit(N)
	switch(state_reg){
		is(LSU_DIE){
            // printf("state:LSU DIE\n")
			io.exec_wb.valid:=false.B
            isLoadException := false.B
            isStoreException := false.B
		}
		is(LSU_DECODE){
            //printf("@lsu LSU_DECODE\n");
			val vAddr = Wire(UInt(32.W))
            isLoadException := false.B
            isStoreException := false.B
			vAddr := (r.imm.asTypeOf(SInt(32.W)) + r.rsData.asSInt()).asUInt()
            val offset = Wire(UInt(2.W))
            offset := vAddr(1, 0)
            //printf("%x %x %x\n", r.imm, r.rsData, vAddr)
			val rt = Wire(UInt(5.W))
			rt := r.rt
			val decoded_instr = ListLookup(r.lsu_op, List("b1111".U(4.W), rt, vAddr, 0.U, true.B, true.B, LSU_FUNC_B, DontCare, DontCare, false.B), Array(
					//read
					BitPat(LSU_LB_OP)->List("b1111".U(4.W), rt, vAddr, 0.U, true.B, true.B, LSU_FUNC_B, DontCare, DontCare, false.B),
					BitPat(LSU_LBU_OP)->List("b1111".U(4.W), rt, vAddr, 0.U, true.B, true.B, LSU_FUNC_BU, DontCare, DontCare, false.B),
					BitPat(LSU_LH_OP)->List("b1111".U(4.W), rt, vAddr, 1.U, true.B, true.B, LSU_FUNC_H, DontCare, DontCare, false.B),
					BitPat(LSU_LHU_OP)->List("b1111".U(4.W), rt, vAddr, 1.U, true.B, true.B, LSU_FUNC_HU, DontCare, DontCare, false.B),
					BitPat(LSU_LW_OP)->List("b1111".U(4.W), rt, vAddr, 3.U, true.B, true.B, LSU_FUNC_WU, DontCare, DontCare, false.B),
					BitPat(LSU_LWL_OP)->List("b1111".U(4.W), rt, vAddr, 3.U, true.B, true.B, LSU_FUNC_RWL, vAddr, DontCare, false.B),
					BitPat(LSU_LWR_OP)->List("b1111".U(4.W), rt, vAddr, 3.U, true.B, true.B, LSU_FUNC_RWR, vAddr, DontCare, false.B),
					//write
					BitPat(LSU_SB_OP)->List(0.U(4.W), DontCare, DontCare, DontCare, false.B, false.B, DontCare, vAddr, "b1".U << offset, true.B),
					BitPat(LSU_SH_OP)->List(0.U(4.W), DontCare, DontCare, DontCare, false.B, false.B, DontCare, vAddr, "b11".U << offset, true.B),
					BitPat(LSU_SW_OP)->List(0.U(4.W), DontCare, DontCare, DontCare, false.B, false.B, DontCare, vAddr, "b1111".U, true.B),
					BitPat(LSU_SWL_OP)->List(0.U(4.W), DontCare, vAddr, 3.U, true.B, true.B, LSU_FUNC_WWL, vAddr, "b1111".U, true.B),
					BitPat(LSU_SWR_OP)->List(0.U(4.W), DontCare, vAddr, 3.U, true.B, true.B, LSU_FUNC_WWR, vAddr, "b1111".U, true.B),
				)
			)
			back_reg.w_en := decoded_instr(0)
			back_reg.w_addr := decoded_instr(1)
			read_reg.addr := decoded_instr(2)
			read_reg.len := decoded_instr(3)
			read_reg.en := decoded_instr(4)
			exec_reg.preRead := decoded_instr(5)
			exec_reg.func := decoded_instr(6)
			write_reg.addr := decoded_instr(7)
			write_reg.strb := decoded_instr(8)
			write_reg.en := decoded_instr(9)
			state_reg := LSU_READ
            // when ((vAddr & 3.U) =/= 0.U){//address exception
            //     io.exec_wb.valid := true.B
			//     io.exec_wb.bits.w_addr := DontCare
			//     io.exec_wb.bits.w_en := 0.U
			//     io.exec_wb.bits.w_data := DontCare
			//     state_reg := LSU_DIE
            // }
            when(r.lsu_op === LSU_LW_OP || r.lsu_op === LSU_SW_OP){
                when((vAddr & 3.U) =/= 0.U){
                    isLoadException := Y
                    state_reg := LSU_BACK
                }
            }
            when(r.lsu_op === LSU_LH_OP || r.lsu_op === LSU_LHU_OP || r.lsu_op === LSU_SH_OP){
                when((vAddr & 1.U) =/= 0.U){
                    isLoadException := Y
                    state_reg := LSU_BACK
                }
            }
            when(io.flush){
                state_reg := LSU_DIE
            }
		}
		is(LSU_READ){
            //printf("@lsu LSU_READ\n");
            // printf(p"lsu_load ${read_reg.addr & (~3.U(32.W))}\n")
			when(!read_reg.en){
				state_reg := LSU_CALC
			}.otherwise{
                // val tlb_req = Flipped(new TLBTranslatorReq)
                // val tlb_resp = Flipped(new TLBTranslatorResp)
                io.tlb_req.va := read_reg.addr & (~3.U(32.W))
                //printf(p"*!* ${io.tlb_req.va}\n")
                io.tlb_req.ref_type := MX_RD
                io.dcache.req.valid := true.B
                io.dcache.req.bits.is_cached := io.tlb_resp.cached
                //printf(p"${r.current_pc} load from ${io.tlb_resp.pa}\n")
				io.dcache.req.bits.addr := io.tlb_resp.pa // read_reg.addr & (~3.U(32.W))
                io.dcache.req.bits.exception := io.tlb_resp.exception
				io.dcache.req.bits.func := MX_RD
				io.dcache.req.bits.len := 3.U
				io.dcache.resp.ready := true.B
                //printf("LOAD at %x\n", io.dcache.req.bits.addr)
			}
			when(io.dcache.resp.fire()){
                when(io.dcache.resp.bits.exception === ET_None){
				    state_reg := LSU_CALC
				    exec_reg.preReadData := io.dcache.resp.bits.data >> (read_reg.addr(1, 0) << 3)
                    exec_reg.preReadDataFull := io.dcache.resp.bits.data
                    except_reg.enable := false.B
                } .otherwise{
                    state_reg := LSU_BACK
                    except_reg.enable := true.B 
                    except_reg.EPC := r.current_pc
                    except_reg.badVaddr := read_reg.addr & (~3.U(32.W))
                     when(io.dcache.resp.bits.exception === ET_TLB_Mod){
                            except_reg.exeCode := EC_Mod
                        }.otherwise{
                        except_reg.exeCode := EC_TLBL
                        }  
                    //except_reg.exeCode := EC_TLBL   // FIXME: what it should be
                    except_reg.excType := io.dcache.resp.bits.exception
                }
            	io.dcache.req.valid := false.B
			}
            when(io.flush){
                state_reg := LSU_DIE
            }
		}
		is (LSU_CALC){
            //printf("@lsu  LSU_CALC\n");
			when(exec_reg.preRead){
				val shiftMask1 = VecInit(0x00ffffff.U, 0x0000ffff.U, 0x000000ff.U, 0x0.U)
				val shiftMask2 = VecInit(0x0.U, 0xff000000L.U, 0xffff0000L.U, 0xffffff00L.U)
				val index = WireInit(write_reg.addr(1, 0).asUInt())
                val len_rwl = index.asUInt + 1.U(3.W)
                val len_rwr = 4.U(3.W) - index.asUInt
                def time8(x:UInt) = (x.asUInt<<3.U)

				write_reg.w_data := Mux1H(Seq(
					(exec_reg.func === LSU_FUNC_B)->(exec_reg.preReadData(7, 0).asTypeOf(SInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_BU)->(exec_reg.preReadData(7, 0).asTypeOf(UInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_H)->(exec_reg.preReadData(15, 0).asTypeOf(SInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_HU)->(exec_reg.preReadData(15, 0).asTypeOf(UInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_W)->(exec_reg.preReadData.asTypeOf(SInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_WU)->(exec_reg.preReadData.asTypeOf(UInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_RWL)-> ((exec_reg.preReadDataFull<<(time8(4.U-len_rwl))) | ((r.rtData << time8(len_rwl))(31, 0) >> time8(len_rwl))),
					(exec_reg.func === LSU_FUNC_RWR)-> (((exec_reg.preReadDataFull<<(time8(index)))(31, 0) >>(time8(index))) | ((r.rtData >> (time8(len_rwr)))<<(time8(len_rwr)))(31, 0)),   // XXX
					(exec_reg.func === LSU_FUNC_WWL) ->((r.rtData >> ((~index) << 3)) | (exec_reg.preReadData & shiftMask2(~index))).asTypeOf(UInt(32.W)),
					(exec_reg.func === LSU_FUNC_WWR) ->((r.rtData << (index << 3)) | (exec_reg.preReadData & shiftMask1(~index))).asTypeOf(UInt(32.W))
				)
				).asTypeOf(UInt(32.W))
                //printf("EXEC PREREAD: %x %x, %x\n", exec_reg.preReadDataFull, time8(len_rwl), ((r.rtData << time8(len_rwl))(31, 0) >> time8(len_rwl)))
			}.otherwise{
				write_reg.w_data := r.rtData
			}
			state_reg := LSU_WRITE
            when(io.flush){
                state_reg := LSU_DIE
            }
		}
		is (LSU_WRITE){
            //printf("@lsu  LSU WRITE\n")
            when(io.flush){
                state_reg := LSU_DIE
            }.otherwise{
			    when(!write_reg.en){
				    back_reg.w_data := write_reg.w_data
				    state_reg := LSU_BACK
			    }.otherwise{
                    printf("@lsu write addr is %x at pc %x\n", write_reg.addr & (~3.U(32.W)), r.current_pc)
                    io.tlb_req.va := write_reg.addr & (~3.U(32.W))
                    io.tlb_req.ref_type := MX_WR
                    io.dcache.req.valid := true.B
                    io.dcache.req.bits.is_cached := io.tlb_resp.cached
                   // printf(p" ${r.current_pc} store into ${io.tlb_resp.pa}\n")
		    		io.dcache.req.bits.addr := io.tlb_resp.pa 
                    io.dcache.req.bits.exception := io.tlb_resp.exception
                    io.dcache.req.bits.data := write_reg.w_data << (write_reg.addr(1, 0) << 3.U)
				    io.dcache.req.bits.func := MX_WR
				    io.dcache.req.bits.strb := write_reg.strb
                //printf("strb %x\n", write_reg.strb)
				    io.dcache.resp.ready := true.B
				    back_reg.w_data := DontCare
			    }
			    when(io.dcache.resp.fire()){
                    when(io.dcache.resp.bits.exception === ET_None){
				        state_reg := LSU_BACK
                        except_reg.enable := false.B
                    } .otherwise{
                        state_reg := LSU_BACK
                        except_reg.enable := true.B 
                        except_reg.EPC := r.current_pc
                        except_reg.badVaddr := write_reg.addr & (~3.U(32.W))
                        printf("@lsu tlb store exception!")
                        when(io.dcache.resp.bits.exception === ET_TLB_Mod){
                            except_reg.exeCode := EC_Mod
                        }.otherwise{
                        except_reg.exeCode := EC_TLBS
                        }   
                        // FIXME: what it should be
                        except_reg.excType := io.dcache.resp.bits.exception
                    }
                    io.dcache.req.valid := false.B
			    }
            }
		}
		is (LSU_BACK){
            //printf("@lsu LSU_BACK\n");
            //printf("lsu ok\n");
            when(!io.flush){
                when(isLoadException){
                    io.exec_wb.bits.error.enable := Y
                    io.exec_wb.bits.error.EPC := r.current_pc
                    io.exec_wb.bits.error.excType := ET_ADDR_ERR
                    when(VecInit(LSU_SW_OP, LSU_SH_OP).contains(r.lsu_op)){
                        //printf("@lsu error store address!\n")
                        io.exec_wb.bits.error.exeCode := EC_AdES
                    }.otherwise{
                        //printf("@lsu error load address!\n")
                        io.exec_wb.bits.error.exeCode := EC_AdEL
                    }
                    io.exec_wb.bits.error.badVaddr := (r.imm.asTypeOf(SInt(32.W)) + r.rsData.asSInt()).asUInt()
                    isLoadException := N
                }.otherwise{
                    io.exec_wb.bits.error <> except_reg
                }
                //printf("@lsu back value is %x\n", io.exec_wb.bits.w_data)
			    io.exec_wb.valid := isu_lsu_fire && !io.flush
			    io.exec_wb.bits.w_addr := back_reg.w_addr
			    io.exec_wb.bits.w_en := back_reg.w_en
			    io.exec_wb.bits.w_data := back_reg.w_data
            }
            when(!(!io.flush && io.isu_lsu.fire())){
                state_reg := LSU_DIE
            }
		}
	}

    io.exec_wb.bits.current_pc := r.current_pc
    io.exec_wb.bits.current_instr := r.current_instr
}


class Divider extends Module {
    val io = IO(Flipped(new DividerIO))
    val dividend = (io.data_dividend_bits).asSInt
    val divisor = (io.data_divisor_bits).asSInt
    val quotient = (dividend / divisor).asUInt
    val remainder = (dividend % divisor).asUInt
    require(quotient.getWidth == 40)
    require(remainder.getWidth == 40)
    val pipe = Pipe(io.data_dividend_valid && io.data_divisor_valid,
    Cat(quotient, remainder), conf.div_stages)

    io.data_dividend_ready := Y
    io.data_divisor_ready := Y
    io.data_dout_valid := pipe.valid
    io.data_dout_bits := pipe.bits
}

class Multiplier extends Module {
    val io = IO(Flipped(new MultiplierIO))
    val a = io.data_a.asSInt
    val b = io.data_b.asSInt
    val pipe = Pipe(Y, (a * b).asUInt, conf.mul_stages)

    io.data_dout := pipe.bits
}

//FIXME io.flush时候，暂停当前过程
class MDU extends Module{
    val io = IO(new Bundle{
        val isu_mdu = Flipped(Decoupled(new ISU_MDU))
        val exec_wb = Decoupled(new MDU_WB)
        val multiplier = new MultiplierIO
        val dividor = new DividerIO
        val flush = Input(Bool())
    })
    io.exec_wb.bits.error := DontCare
    // val multiplier = Module(new Multiplier)
    // val dividor = Module(new Divider)
    
    // isu_mdu_fired 
    val state = RegInit(0.U)
    val isu_mdu_reg = RegEnable(io.isu_mdu.bits, io.isu_mdu.fire())
    io.isu_mdu.ready := io.exec_wb.fire() || (state===0.U)
    //printf("io.isu_mdu.ready %d isu_mdu_fired %d \n", io.isu_mdu.ready, isu_mdu_fired);
    when (io.flush || (!io.isu_mdu.fire() && io.exec_wb.fire())) {
        state := 0.U
    } .elsewhen (!io.flush && io.isu_mdu.fire()) {
        //printf("mdu is working!\n");
        state := 1.U
    }
    when(state=/=0.U){
        //printf("MDU WORKING\n")
    }
    // when(io.isu_mdu.fire()){
    //     printf(p"#### ${io.exec_wb.fire()} or ${!isu_mdu_fired}\n")
    //     printf(p"${io.isu_mdu}\n")
    // }

    // val mdu_wb_valid = RegInit(false.B)
    val mdu_wb_reg = Reg(new MDU_WB)

    val hi = RegInit(0.U(32.W))
    val lo = RegInit(0.U(32.W))
    // printf(p"LO: ${lo}\n")
    val multiplier_delay_count = RegInit((conf.mul_stages).U(3.W))    // 7
    // printf(p"dividor ready: ${dividor_ready}\n")
    // printf(p"isu mud reg: ${isu_mdu_reg}\n")
    io.dividor <> DontCare
    io.dividor.data_dividend_valid := false.B
    io.dividor.data_divisor_valid := false.B
    io.multiplier <> DontCare
    when(state===1.U & !io.flush){
        //printf("start working on ")
        // data_dividend_valid, data_divisor_valid, data_divident_bits, data_divisor_bits
        // mdu_wb_valid := false.B
        when(VecInit(MDU_DIV_OP, MDU_DIVU_OP).contains(isu_mdu_reg.mdu_op)){
            //printf("div\n")
            // div
            io.dividor.data_dividend_valid := true.B
            io.dividor.data_divisor_valid := true.B
            io.dividor.data_dividend_bits := Cat(
                Mux(isu_mdu_reg.mdu_op===MDU_DIV_OP, Fill(8, isu_mdu_reg.rsData(31)), 0.U(8.W)),
                isu_mdu_reg.rsData
            )
            io.dividor.data_divisor_bits := Cat(
                Mux(isu_mdu_reg.mdu_op===MDU_DIV_OP, Fill(8, isu_mdu_reg.rtData(31)), 0.U(8.W)),
                isu_mdu_reg.rtData
            )
            state := 3.U
        } .elsewhen(VecInit(MDU_MUL_OP, MDU_MULT_OP, MDU_MULTU_OP, MDU_MADD_OP, MDU_MADDU_OP, MDU_MSUB_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op)){
            //printf("mul\n")
            // mul
            multiplier_delay_count := (conf.mul_stages-1).U(3.W)
            io.multiplier.data_a := Cat(
                Mux(VecInit(MDU_MULTU_OP, MDU_MADDU_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op), 0.U(1.W), isu_mdu_reg.rsData(31)),
                isu_mdu_reg.rsData
            )
            io.multiplier.data_b := Cat(
                Mux(VecInit(MDU_MULTU_OP, MDU_MADDU_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op), 0.U(1.W), isu_mdu_reg.rtData(31)),
                isu_mdu_reg.rtData
            )
            state := 2.U
        } .otherwise{
            //printf(p"mf/mt hi:${hi}, lo:${lo}\n")
            // mfhi, mflo
            mdu_wb_reg.w_en := false.B
            when(VecInit(MDU_MFHI_OP, MDU_MFLO_OP).contains(isu_mdu_reg.mdu_op)){
                mdu_wb_reg.w_en := true.B
                mdu_wb_reg.w_addr := isu_mdu_reg.rd 
                mdu_wb_reg.w_data := Mux(isu_mdu_reg.mdu_op===MDU_MFHI_OP, hi, lo)
            }
            hi := Mux(isu_mdu_reg.mdu_op===MDU_MTHI_OP, isu_mdu_reg.rsData, hi)
            lo := Mux(isu_mdu_reg.mdu_op===MDU_MTLO_OP, isu_mdu_reg.rsData, lo)
            state := 4.U
        }
    } .elsewhen(state===2.U){
        //printf("multipling \n")
        multiplier_delay_count := multiplier_delay_count - 1.U
        when(multiplier_delay_count === 0.U(3.W)){  // Multiplier OK
            //printf("touch! delay 0\n")
            multiplier_delay_count := conf.mul_stages.U(3.W)
            mdu_wb_reg.w_en := Mux(isu_mdu_reg.mdu_op===MDU_MUL_OP, true.B, false.B)
            state := 4.U
            when(VecInit(MDU_MADD_OP, MDU_MADDU_OP).contains(isu_mdu_reg.mdu_op)){
                val hi_lo = Cat(hi, lo) + io.multiplier.data_dout(63, 0)
                hi := hi_lo(63, 32)
                lo := hi_lo(31, 0)
            } .elsewhen(VecInit(MDU_MSUB_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op)){
                val hi_lo = Cat(hi, lo) - io.multiplier.data_dout(63, 0)
                hi := hi_lo(63, 32)
                lo := hi_lo(31, 0)
            } .elsewhen(isu_mdu_reg.mdu_op===MDU_MUL_OP){
                mdu_wb_reg.w_addr := isu_mdu_reg.rd
                mdu_wb_reg.w_data := io.multiplier.data_dout(31, 0)
            }.otherwise{
                hi := io.multiplier.data_dout(63, 32)
                lo := io.multiplier.data_dout(31, 0)
            }
        }
    } .elsewhen(state===3.U){
        //printf("dividing!\n")
        when(io.dividor.data_dout_valid){ //VecInit(MDU_DIV_OP, MDU_DIVU_OP).contains(isu_mdu_reg.mdu_op)
            //printf("touch! dividor done 0\n")
            mdu_wb_reg.w_en := false.B 
            // mdu_wb_valid := true.B
            lo := io.dividor.data_dout_bits(71, 40)
            hi := io.dividor.data_dout_bits(31, 0) 
            state := 4.U 
        }
    }

    //printf("count:%d\n", multiplier_delay_count)
    mdu_wb_reg.current_pc := isu_mdu_reg.current_pc
    //printf("pc %x\n", isu_mdu_reg.current_pc)
    mdu_wb_reg.current_instr := isu_mdu_reg.current_instr
    io.exec_wb.valid := (state===4.U) && !io.flush
    io.exec_wb.bits <> mdu_wb_reg
    when(io.exec_wb.valid){
        //printf(p"mdu->wb-bits: ${io.exec_wb.bits}\n reg ${mdu_wb_reg}\n")
    }
}
