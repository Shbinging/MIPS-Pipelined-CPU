package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import firrtl.options.DoNotTerminateOnExit

class WriteBack extends Module{
    val io = IO(new Bundle{
        val alu_wb = Flipped(Decoupled(new ALU_WB))
        val bru_wb = Flipped(Decoupled(new BRU_WB))
        val lsu_wb = Flipped(Decoupled(new LSU_WB))
        val mdu_wb = Flipped(Decoupled(new MDU_WB))
        val pru_wb = Flipped(Decoupled(new PRU_WB))
        val wb_if = Decoupled(new RB_IF)
        val commit = Output(new WB_COMMMIT)
        val flush = Output(Bool())
        val gpr_wr = Flipped(new GPRWriteInput)
        val cp0_status = Input(new cp0_Status_12)
        val cp0_cause = Input(new cp0_Cause_13)
        val cp0_context = Input(new cp0_Context_4)
        val cp0_entryhi = Input(new EntryHi)
        //val cp0_compare = Input(UInt(conf.data_width.W))
        //val cp0_count = Input(UInt(conf.data_width.W))
        val isIrq7 = Input(Bool())

        
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

        val irq7 = Output(Bool())
    })
    val time = RegInit(0.U(32.W))
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

    io.alu_wb.ready := true.B
    io.bru_wb.ready := true.B
    io.lsu_wb.ready := true.B
    io.mdu_wb.ready := true.B
    io.pru_wb.ready := true.B
    val alu_wb_fire = RegNext(io.alu_wb.fire())
    val bru_wb_fire = RegNext(io.bru_wb.fire())
    val lsu_wb_fire = RegNext(io.lsu_wb.fire())
    val mdu_wb_fire = RegNext(io.mdu_wb.fire()) // XXX: one cycle assumption
    val pru_wb_fire = RegNext(io.pru_wb.fire())
    val reg_alu_wb = RegEnable(io.alu_wb.bits, io.alu_wb.fire())
    val reg_bru_wb = RegEnable(io.bru_wb.bits, io.bru_wb.fire())
    val reg_lsu_wb = RegEnable(io.lsu_wb.bits, io.lsu_wb.fire())
    val reg_mdu_wb = RegEnable(io.mdu_wb.bits, io.mdu_wb.fire())
    val reg_pru_wb = RegEnable(io.pru_wb.bits, io.pru_wb.fire())
    val io_fire = Wire(Bool())
    io_fire := io.alu_wb.fire() || io.bru_wb.fire() || io.lsu_wb.fire() || io.mdu_wb.fire() || io.pru_wb.fire()
    when(io_fire){
        time := time + 1.U
        //printf(p"commit sum:${time}\n")
    }
    io.gpr_wr <> DontCare
    io.gpr_wr.w_en := 0.U
    io.wb_if.bits.pc_w_data <> DontCare
    io.wb_if.bits.pc_w_en := false.B
    
    val isBranch = RegEnable(io.bru_wb.bits.w_pc_en && io.bru_wb.fire(), N, io_fire)
    val isLastBranch = RegEnable(io.bru_wb.fire(), N, io_fire)
    val isSlot = RegNext(isLastBranch && io_fire, N)
    val needJump = RegNext(isBranch && io_fire, N)
    val isException = WireInit((alu_wb_fire && reg_alu_wb.error.enable) || (lsu_wb_fire && reg_lsu_wb.error.enable) || (mdu_wb_fire && reg_mdu_wb.error.enable) || (pru_wb_fire && reg_pru_wb.error.enable))

    io.commit := DontCare
    io.commit.commit := N
  
    def isCauseWrite() = pru_wb_fire && reg_pru_wb.mft.en && reg_pru_wb.mft.destSel && reg_pru_wb.mft.destAddr === index_cp0_cause
    def isStatusWrite () = pru_wb_fire && reg_pru_wb.mft.en && reg_pru_wb.mft.destSel && reg_pru_wb.mft.destAddr === index_cp0_status
    def mergeStatus(a:UInt) = ((a & "b1111_1010_0111_1000_1111_1111_0001_0111".U) | (io.cp0_status.asUInt() & "b0000_0101_1000_0000_0000_0000_1110_0000".U)).asTypeOf(new cp0_Status_12)
    def mergeCause(a:UInt) = ((a & "b0000_0000_1100_0000_0000_0011_0000_0000".U ).asUInt | (io.cp0_cause.asUInt() &"b1011_0000_0000_0000_1111_1100_0111_1100".U)).asTypeOf(new cp0_Cause_13)
    def getCauseValue1() = Mux(isCauseWrite(), reg_pru_wb.mft.data.asTypeOf(new cp0_Cause_13), io.cp0_cause)
    def getStatusValue1() = Mux(isStatusWrite(), reg_pru_wb.mft.data.asTypeOf(new cp0_Status_12), io.cp0_status)
    //def getStatusValue() = mergeStatus(getStatusValue1().asUInt())
    //def getCauseValue() = mergeCause(getCauseValue1().asUInt())
    def getStatusValue() = io.cp0_status
    def getCauseValue() = io.cp0_cause

    def canInterupt(idx:UInt) = !getStatusValue().ERL.asBool && !getStatusValue().EXL.asBool && getStatusValue().IE.asBool() &&  getStatusValue().IM(idx).asBool() && getCauseValue().IP(idx).asBool()
    def isSoftIntr0() = canInterupt(0.U)
    def isSoftIntr1() = canInterupt(1.U)
    def isSoftIntr() = pru_wb_fire && (isSoftIntr0() || isSoftIntr1()) && !isException
    def ismtCom() = pru_wb_fire && reg_pru_wb.mft.en && reg_pru_wb.mft.destSel && reg_pru_wb.mft.destAddr === index_cp0_compare
    def isIrq7() = !ismtCom() && !isException && (alu_wb_fire || pru_wb_fire || mdu_wb_fire || bru_wb_fire || lsu_wb_fire) && io.isIrq7 && !getStatusValue().ERL.asBool && !getStatusValue().EXL.asBool && getStatusValue().IE.asBool() &&  getStatusValue().IM(7.U).asBool() 

    io.irq7 := io.isIrq7
    printf("@wb isirq7 %d\n", isIrq7())
    def isEret() = pru_wb_fire && reg_pru_wb.eret.en

    val current_pc = Wire(UInt(32.W))
    current_pc := DontCare
    when(alu_wb_fire){
        current_pc := reg_alu_wb.current_pc
    }
    when(lsu_wb_fire){
        current_pc := reg_lsu_wb.current_pc
    }
    when(pru_wb_fire){
        current_pc := reg_pru_wb.current_pc
    }
    when(mdu_wb_fire){
        current_pc := reg_mdu_wb.current_pc
    }
    when(bru_wb_fire){
        current_pc := reg_bru_wb.current_pc
    }
    when(needJump){
        current_pc:= reg_bru_wb.w_pc_addr - 4.U
    }
    when(isException || isSoftIntr() || isIrq7()){//exception
        val cause_cur = WireInit(getCauseValue())
        val status_cur = WireInit(getStatusValue())
        printf("exception! \n")
        printf("@wb cause_cur %x %x\n", getCauseValue().asUInt(), getCauseValue1().asUInt() & "b0000_0000_1100_0000_0000_0011_0000_0000".U)
        io.flush := Y
        io.wb_if.valid := Y
        isBranch := N
        needJump := N
        //TODO::exception fill
        val exception = Wire(new exceptionInfo)
        exception := DontCare
        when(isIrq7()){
            printf("@wb irq7 epc is %x\n", current_pc)
            // when(bru_wb_fire){
            //     exception.EPC := current_pc
            // }.otherwise{
                exception.EPC := current_pc + 4.U
            //}
            exception.enable := Y
            exception.excType := ET_Int
            exception.exeCode := EC_Int
            cause_cur.IP := getCauseValue().IP | "b1000_0000".U
        }
        when(isSoftIntr0()){
            printf("@wb int0\n")
            exception.EPC := reg_pru_wb.current_pc + 4.U
            exception.enable := Y
            exception.excType := ET_Int
            exception.exeCode := EC_Int
            io.commit.commit := Y
            io.commit.commit_instr := reg_pru_wb.current_instr
            io.commit.commit_pc := reg_pru_wb.current_pc
            cause_cur.IP := getCauseValue().IP | 1.U
        }
        when(isSoftIntr1()){
            printf("@wb int1\n");
            exception.EPC := reg_pru_wb.current_pc + 4.U
            exception.enable := Y
            exception.excType := ET_Int
            exception.exeCode := EC_Int
            io.commit.commit := Y
            io.commit.commit_instr := reg_pru_wb.current_instr
            io.commit.commit_pc := reg_pru_wb.current_pc
            cause_cur.IP := getCauseValue().IP | 2.U
        }
        when((alu_wb_fire && reg_alu_wb.error.enable)){
            exception := reg_alu_wb.error
            io.commit.commit := Y
            io.commit.commit_instr := reg_alu_wb.current_instr
            io.commit.commit_pc := reg_alu_wb.current_pc
        }
        when((lsu_wb_fire && reg_lsu_wb.error.enable)){
            printf(p"@wb lsu_wb error ${lsu_wb_fire}, ${reg_lsu_wb.error}\n")
            exception := reg_lsu_wb.error
            io.commit.commit := Y
            io.commit.commit_instr := reg_lsu_wb.current_instr
            io.commit.commit_pc := reg_lsu_wb.current_pc
        }
        when((pru_wb_fire && reg_pru_wb.error.enable)){
            exception := reg_pru_wb.error
            printf("@wb pru exception pc %x\n", reg_pru_wb.error.EPC)
            when(reg_pru_wb.needCommit){
                printf("@wb pru commit_pc %x\n", reg_pru_wb.current_pc)
                io.commit.commit := Y
                io.commit.commit_instr := reg_pru_wb.current_instr
                io.commit.commit_pc := reg_pru_wb.current_pc
            }
        }
        
        

        // io.cp0_write_out.Cause := io.cp0_read_in.Cause
        // io.cp0_write_out.Status := io.cp0_read_in.Status
        val vecOff = WireInit(0.U(32.W))
        when(io.cp0_status.EXL === 0.U){
                io.out_epc_sel_0.en := Y
                
                when((isSlot && !isIrq7()) || (isIrq7() && bru_wb_fire)){
                    io.out_epc_sel_0.data := exception.EPC - 4.U
                    //printf("@wb exception.epc %x epc %x\n", exception.EPC, io.out_epc_sel_0.data)
                    cause_cur.BD := 1.U
                }.otherwise{
                    io.out_epc_sel_0.data := exception.EPC
                    cause_cur.BD := 0.U
                }
                //TODO:: check type
                when (exception.excType === ET_TLB_REFILL){
                    vecOff := 0x0.U
                }.elsewhen(exception.exeCode === EC_Int && io.cp0_cause.IV === 1.U){
                    vecOff := 0x200.U
                }.otherwise{
                    vecOff := 0x180.U
                }
        }.otherwise{
            printf(p"${reg_pru_wb.current_pc} ??? EXL, EXCTYPE :${exception.excType}\n")
            vecOff := 0x180.U
            // io.out_epc_sel_0.en := Y
            // io.out_epc_sel_0.data := exception.EPC
        }
        
        when(exception.excType === ET_ADDR_ERR){
            io.out_badAddr_sel_0.en := Y
            io.out_badAddr_sel_0.data := exception.badVaddr
        }
        when(VecInit(ET_TLB_Inv, ET_TLB_Mod, ET_TLB_REFILL).contains(exception.excType)){
            io.out_badAddr_sel_0.en := Y
            io.out_badAddr_sel_0.data := exception.badVaddr

            io.out_context_sel_0.en := Y
            val context = WireInit(io.cp0_context)
            context.BadVPN2 := exception.badVaddr >> 13
            io.out_context_sel_0.data := context.asUInt()

            io.out_entryhi_sel_0.en := Y
            val entryhi = WireInit(io.cp0_entryhi)
            entryhi.vpn2 := exception.badVaddr >> 13
            io.out_entryhi_sel_0.data := entryhi.asUInt & "h_ffffe0ff".U(conf.data_width.W)
            //entryhi.asid := excep
        }


        cause_cur.ExcCode := exception.exeCode
        printf("@wb excCode %x vecOff %x\n", exception.exeCode, vecOff)
        status_cur.EXL := 1.U
        io.wb_if.bits.pc_w_en := Y

        when(io.cp0_status.BEV === 1.U){
            io.wb_if.bits.pc_w_data :=  0xbfc00200L.asUInt(32.W) + vecOff
        }.otherwise{
            io.wb_if.bits.pc_w_data := 0x80000000L.asUInt(32.W) + vecOff
        }
        io.out_cause_sel_0.en := Y
        io.out_cause_sel_0.data := cause_cur.asUInt()
        io.out_status_sel_0.en := Y
        io.out_status_sel_0.data := status_cur.asUInt()
        //io.out_epc_sel_0.en := Y
        //io.out_epc_sel_0.data := exception.EPC
    }.elsewhen(isEret()){//jump without delay(ERET)
        printf("@wb eret %x\n", reg_pru_wb.current_pc)
        io.flush := Y
        io.wb_if.valid := Y
        isBranch := N
        needJump := N

        io.wb_if.bits.pc_w_data := reg_pru_wb.eret.w_pc_addr
        io.wb_if.bits.pc_w_en := Y
        // XXX: is it correct? check the manual ERET 
        when(io.cp0_status.ERL.asBool()){
            io.out_status_sel_0.en := Y 
            io.out_status_sel_0.data := io.cp0_status.asUInt & "h_ffff_fffb".U 
        } .otherwise{
            io.out_status_sel_0.en := Y 
            io.out_status_sel_0.data := io.cp0_status.asUInt & "h_ffff_fffd".U
        }

        io.commit.commit_pc := reg_pru_wb.current_pc
        io.commit.commit_instr := reg_pru_wb.current_instr
        io.commit.commit := true.B
    }.otherwise{
    when(needJump){
        io.flush := Y
        io.wb_if.bits.pc_w_data := reg_bru_wb.w_pc_addr
        printf("@wb jump to %x! \n", io.wb_if.bits.pc_w_data)
        io.wb_if.bits.pc_w_en := reg_bru_wb.w_pc_en
        io.wb_if.valid := Y
    }.otherwise{
        io.wb_if.valid := N
        io.flush := N
    }
    }
    when(!isException && !isSoftIntr() && !isEret()){
    io.commit <> DontCare
    io.commit.commit := false.B
    when(alu_wb_fire){
        io.gpr_wr.addr := reg_alu_wb.w_addr
        io.gpr_wr.data := reg_alu_wb.ALU_out
        io.gpr_wr.w_en := Mux(reg_alu_wb.w_en, "b1111".U, "b0000".U)
        io.commit.commit_pc := reg_alu_wb.current_pc
        io.commit.commit_instr := reg_alu_wb.current_instr
        io.commit.commit := true.B
        printf("alu wb %x\n", io.commit.commit_pc);
        // printf(p"${time}: alu wb\n")
    }.elsewhen(bru_wb_fire){
        io.commit.commit_pc := reg_bru_wb.current_pc
        io.commit.commit_instr := reg_bru_wb.current_instr
        io.commit.commit := true.B
        io.gpr_wr.addr := reg_bru_wb.w_addr
        io.gpr_wr.data := reg_bru_wb.w_data
        io.gpr_wr.w_en := Mux(reg_bru_wb.w_en, "b1111".U, 0.U)
        printf("bru wb %x\n", io.commit.commit_pc);
    }.elsewhen(lsu_wb_fire){
        io.gpr_wr.addr := reg_lsu_wb.w_addr
        io.gpr_wr.data := reg_lsu_wb.w_data
        io.gpr_wr.w_en := reg_lsu_wb.w_en // Mux(reg_lsu_wb.w_en, "b1111".U, 0.U)
        io.commit.commit_pc := reg_lsu_wb.current_pc
        io.commit.commit_instr := reg_lsu_wb.current_instr
        io.commit.commit := true.B
        // io.wb_if.bits.pc_w_en := 0.U
        printf("lsu wb %x\n", io.commit.commit_pc);
    }.elsewhen(mdu_wb_fire){
        io.gpr_wr.addr := reg_mdu_wb.w_addr
        io.gpr_wr.data := reg_mdu_wb.w_data
        io.gpr_wr.w_en := Mux(reg_mdu_wb.w_en, "b1111".U, 0.U)
        //printf(p"w_en: ${io.gpr_wr.w_en} w_data: ${io.gpr_wr.data}\n")
        // printf("commit %x\n", reg_mdu_wb.current_pc)
        io.commit.commit_pc := reg_mdu_wb.current_pc
        io.commit.commit_instr := reg_mdu_wb.current_instr
        io.commit.commit := true.B
        printf("mdu wb %x\n", io.commit.commit_pc);
    }.elsewhen(pru_wb_fire){
        io.commit.commit_pc := reg_pru_wb.current_pc
        io.commit.commit_instr := reg_pru_wb.current_instr
        io.commit.commit := Y
        printf("pru wb %x\n", io.commit.commit_pc)
        when(reg_pru_wb.mft.en){
            when(reg_pru_wb.mft.destSel){//cp0
                // switch(reg_pru_wb.mft.destAddr){
                //     is(index_cp0_badvaddr){
                //         io.out_badAddr_sel_0.en := Y
                //         io.out_badAddr_sel_0.data := reg_pru_wb.mft.data
                //     }
                //     is(index_cp0_cause){
                //         printf("@wb cp0_cause %x\n", reg_pru_wb.mft.data)
                //         io.out_cause_sel_0.en := Y
                //         io.out_cause_sel_0.data := mergeCause(reg_pru_wb.mft.data).asUInt//(reg_pru_wb.mft.data & "b0000_0000_1100_0000_0011_0000_0000".U )| (io.cp0_cause.asUInt() &"b1011_0000_0000_0000_1111_1100_0111_1100".U)
                //     }
                //     is(index_cp0_epc){
                //         io.out_epc_sel_0.en := Y
                //         io.out_epc_sel_0.data := reg_pru_wb.mft.data
                //     }
                //     is(index_cp0_status){
                //         printf(p"write to cp0 status ${reg_pru_wb.mft.data}")
                //         io.out_status_sel_0.en := Y
                //         io.out_status_sel_0.data := mergeStatus(reg_pru_wb.mft.data).asUInt()
                //     }
                //     is(index_cp0_index){
                //         io.out_index_sel_0.en := Y 
                //         io.out_index_sel_0.data := reg_pru_wb.mft.data
                //     }
                //     is(index_cp0_compare){
                //         io.out_compare_sel_0.en := Y
                //         io.out_compare_sel_0.data := reg_pru_wb.mft.data
                //     }
                //     is(index_cp0_count){
                //         io.out_count_sel_0.en := Y
                //         io.out_count_sel_0.data := reg_pru_wb.mft.data
                //     }
                //     // is(index_cp0_random){
                //     //     io.out_random_sel_0.en := Y 
                //     //     io.out_random_sel_0.data := reg_pru_wb.mft.data
                //     // }
                //     is(index_cp0_entrylo0){
                //         io.out_entrylo0_sel_0.en := Y 
                //         io.out_entrylo0_sel_0.data := reg_pru_wb.mft.data
                //     }
                //     is(index_cp0_entrylo1){
                //         io.out_entrylo1_sel_0.en := Y 
                //         io.out_entrylo1_sel_0.data := reg_pru_wb.mft.data 
                //     }
                //     is(index_cp0_entryhi){
                //         io.out_entryhi_sel_0.en := Y 
                //         io.out_entryhi_sel_0.data := reg_pru_wb.mft.data
                //     }
                //}
            }.otherwise{//to general
                io.gpr_wr.w_en := "b1111".U
                io.gpr_wr.data := reg_pru_wb.mft.data
                io.gpr_wr.addr := reg_pru_wb.mft.destAddr
            }
        } .elsewhen(reg_pru_wb.tlbp.en){
            io.out_index_sel_0.en := Y 
            io.out_index_sel_0.data := reg_pru_wb.tlbp.index_data 
        } .elsewhen(reg_pru_wb.tlbr.en){
            io.out_entryhi_sel_0.en := Y 
            io.out_entryhi_sel_0.data := reg_pru_wb.tlbr.entryhi.asUInt & "h_ffffe0ff".U(conf.data_width.W)
            io.out_entrylo0_sel_0.en := Y 
            io.out_entrylo0_sel_0.data := reg_pru_wb.tlbr.entrylo_0.asUInt  & "h_03ff_ffff".U(conf.data_width.W)
            io.out_entrylo1_sel_0.en := Y 
            io.out_entrylo1_sel_0.data := reg_pru_wb.tlbr.entrylo_1.asUInt  & "h_03ff_ffff".U(conf.data_width.W)
        }
    }
    }
    when(io.flush){
        printf("@wb flush!\n")
    }
    printf("is commit %d\n", io.commit.commit)
    //  when(io.wb_if.valid){
    //     printf(p"${reg_exec_wb}")
    // }
}