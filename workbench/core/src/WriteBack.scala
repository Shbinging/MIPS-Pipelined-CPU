package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

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

        val cp0_write_out = Flipped(new CP0WriteInputWB)
        
        val out_index_sel_0 = Flipped(new CP0WriteInput)
        val out_cause_sel_0 = Flipped(new CP0WriteInput)
        val out_status_sel_0 = Flipped(new CP0WriteInput)
        val out_epc_sel_0 = Flipped(new CP0WriteInput)
        val out_badAddr_sel_0 = Flipped(new CP0WriteInput)
        val out_context_sel_0 = Flipped(new CP0WriteInput)
        val out_entryhi_sel_0 = Flipped(new CP0WriteInput)
        val out_entrylo0_sel_0 = Flipped(new CP0WriteInput)
        val out_entrylo1_sel_0 = Flipped(new CP0WriteInput)
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
    io.cp0_write_out <> DontCare
    val isBranch = RegEnable(io.bru_wb.bits.w_pc_en && io.bru_wb.fire(), N, io_fire)
    val isLastBranch = RegEnable(io.bru_wb.fire(), N, io_fire)
    val isSlot = RegNext(isLastBranch && io_fire, N)
    val needJump = RegNext(isBranch && io_fire, N)
    val isException = WireInit((alu_wb_fire && reg_alu_wb.error.enable) || (lsu_wb_fire && reg_lsu_wb.error.enable) || (mdu_wb_fire && reg_mdu_wb.error.enable) || (pru_wb_fire && reg_pru_wb.error.enable))
    io.cp0_write_out.enableOther := N
    io.cp0_write_out.enableEXL := N
    io.commit := DontCare
    io.commit.commit := N
    
    when(isException){//exception
        printf("exception! \n")
        io.flush := Y
        io.wb_if.valid := Y
        isBranch := N
        needJump := N
        //TODO::exception fill
        val exception = Wire(new exceptionInfo)
        exception := DontCare
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
            when(reg_pru_wb.needCommit){
                io.commit.commit := Y
                io.commit.commit_instr := reg_pru_wb.current_instr
                io.commit.commit_pc := reg_pru_wb.current_pc
            }
        }
        
        io.cp0_write_out.enableEXL := Y
        io.cp0_write_out.enableOther := Y
        // io.cp0_write_out.Cause := io.cp0_read_in.Cause
        // io.cp0_write_out.Status := io.cp0_read_in.Status
        val vecOff = WireInit(0.U(32.W))
        when(io.cp0_status.EXL === 0.U){
                when(isSlot){
                    io.cp0_write_out.epc := exception.EPC - 4.U
                    io.cp0_write_out.BD := 1.U
                    // io.cp0_write_out.Cause.BD := 1.U
                }.otherwise{
                    io.cp0_write_out.epc := exception.EPC
                    io.cp0_write_out.BD := 0.U 
                    // io.cp0_write_out.Cause.BD := 0.U
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
            vecOff := 0x180.U
        }
        io.cp0_write_out.enableVaddress := N
        when(exception.excType === ET_ADDR_ERR){
            io.cp0_write_out.enableVaddress := Y
            io.cp0_write_out.vAddr := exception.badVaddr
        }
        when(VecInit(ET_TLB_Inv, ET_TLB_Miss, ET_TLB_Mod, ET_TLB_REFILL).contains(exception.excType)){
            io.out_badAddr_sel_0.en := Y
            io.out_badAddr_sel_0.data := exception.badVaddr

            io.out_context_sel_0.en := Y
            val context = WireInit(io.cp0_context)
            context.BadVPN2 := exception.badVaddr >> 13
            io.out_context_sel_0.data := context.asUInt()

            io.out_entryhi_sel_0.en := Y
            val entryhi = WireInit(io.cp0_entryhi)
            entryhi.vpn2 := exception.badVaddr >> 13
            io.out_entryhi_sel_0.data := entryhi.asUInt
            //entryhi.asid := excep
        }


        io.cp0_write_out.ExcCode := exception.exeCode
        printf("@wb excCode %x vecOff %x\n", exception.exeCode, vecOff)
        io.cp0_write_out.EXL := 1.U
        io.wb_if.bits.pc_w_en := Y

        when(io.cp0_status.BEV === 1.U){
            io.wb_if.bits.pc_w_data :=  0xbfc00200L.asUInt(32.W) + vecOff
        }.otherwise{
            io.wb_if.bits.pc_w_data := 0x80000000L.asUInt(32.W) + vecOff
        }
        //io.wb_if.bits.pc_w_data :=  0xbfc00200L.asUInt(32.W) + vecOff
    }.elsewhen(pru_wb_fire && reg_pru_wb.eret.en){//jump without delay(ERET)
        printf("@wb eret\n")
        io.flush := Y
        io.wb_if.valid := Y
        isBranch := N
        needJump := N
        io.cp0_write_out.enableEXL := Y
        io.cp0_write_out.EXL := 0.U
        io.wb_if.bits.pc_w_data := reg_pru_wb.eret.w_pc_addr
        io.wb_if.bits.pc_w_en := Y
        io.commit.commit_pc := reg_pru_wb.current_pc
        io.commit.commit_instr := reg_pru_wb.current_instr
        io.commit.commit := true.B
    }.otherwise{
    when(needJump){
        io.flush := Y
        io.wb_if.bits.pc_w_data := reg_bru_wb.w_pc_addr
        io.wb_if.bits.pc_w_en := reg_bru_wb.w_pc_en
        io.wb_if.valid := Y
    }.otherwise{
        io.wb_if.valid := N
        io.flush := N
    }

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
        when(reg_pru_wb.mft.en){
            when(reg_pru_wb.mft.destSel){//cp0
                switch(reg_pru_wb.mft.destAddr){
                    is(index_cp0_badvaddr){
                        io.out_badAddr_sel_0.en := Y
                        io.out_badAddr_sel_0.data := reg_pru_wb.mft.data
                    }
                    is(index_cp0_cause){
                        io.out_cause_sel_0.en := Y
                        io.out_cause_sel_0.data := reg_pru_wb.mft.data
                    }
                    is(index_cp0_epc){
                        io.out_epc_sel_0.en := Y
                        io.out_epc_sel_0.data := reg_pru_wb.mft.data
                    }
                    is(index_cp0_status){
                        io.out_status_sel_0.en := Y
                        io.out_status_sel_0.data := reg_pru_wb.mft.data
                    }
                }
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
            io.out_entryhi_sel_0.data := reg_pru_wb.tlbr.entryhi.asUInt
            io.out_entrylo0_sel_0.en := Y 
            io.out_entrylo0_sel_0.data := reg_pru_wb.tlbr.entrylo_0.asUInt
            io.out_entrylo1_sel_0.en := Y 
            io.out_entrylo1_sel_0.data := reg_pru_wb.tlbr.entrylo_1.asUInt
        }
    }
    }
    printf("is commit %d\n", io.commit.commit)
    //  when(io.wb_if.valid){
    //     printf(p"${reg_exec_wb}")
    // }
}