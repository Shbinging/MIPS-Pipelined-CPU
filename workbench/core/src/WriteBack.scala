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
        val wb_if = Decoupled(new RB_IF)
        val commit = Output(new WB_COMMMIT)
        val flush = Output(Bool())
        val gpr_wr = Flipped(new GPRWriteInput)
        val cp0_status = Input(new cp0_Status_12)
        val cp0_write_out = Flipped(new CP0WriteInput)
    })
    val time = RegInit(0.U(32.W))


    io.alu_wb.ready := true.B
    io.bru_wb.ready := true.B
    io.lsu_wb.ready := true.B
    io.mdu_wb.ready := true.B
    val alu_wb_fire = RegNext(io.alu_wb.fire())
    val bru_wb_fire = RegNext(io.bru_wb.fire())
    val lsu_wb_fire = RegNext(io.lsu_wb.fire())
    val mdu_wb_fire = RegNext(io.mdu_wb.fire()) // XXX: one cycle assumption
    val reg_alu_wb = RegEnable(io.alu_wb.bits, io.alu_wb.fire())
    val reg_bru_wb = RegEnable(io.bru_wb.bits, io.bru_wb.fire())
    val reg_lsu_wb = RegEnable(io.lsu_wb.bits, io.lsu_wb.fire())
    val reg_mdu_wb = RegEnable(io.mdu_wb.bits, io.mdu_wb.fire())
    val io_fire = Wire(Bool())
    io_fire := io.alu_wb.fire() || io.bru_wb.fire() || io.lsu_wb.fire() || io.mdu_wb.fire()
    when(io_fire){
        time := time + 1.U
        printf(p"commit sum:${time}\n")
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
    val isException = WireInit((alu_wb_fire && reg_alu_wb.error.enable) || (lsu_wb_fire && reg_lsu_wb.error.enable) || (mdu_wb_fire && reg_mdu_wb.error.enable))
    io.cp0_write_out.enableOther := N
    io.cp0_write_out.enableEXL := N
    io.commit := DontCare
    when(isException){//exception
        io.flush := Y
        io.wb_if.valid := Y
        isBranch := N
        needJump := N
        //TODO::exception fill
        val exception = Wire(new exceptionInfo)
        exception := DontCare
        when((alu_wb_fire && reg_alu_wb.error.enable)){
            exception := reg_alu_wb.error
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
                vecOff := 0x180.U
        }
        io.cp0_write_out.ExcCode := exception.exeCode
        io.cp0_write_out.EXL := 1.U
        io.wb_if.bits.pc_w_en := Y

        when(io.cp0_status.BEV === 1.U){
            io.wb_if.bits.pc_w_data :=  0xbfc00200L.asUInt(32.W) + vecOff
        }.otherwise{
            io.wb_if.bits.pc_w_data := 0x80000000L.asUInt(32.W) + vecOff
        }
    }.elsewhen(bru_wb_fire && reg_bru_wb.noSlot){//jump without delay(ERET)
        io.flush := Y
        io.wb_if.valid := Y
        isBranch := N
        needJump := N
        io.cp0_write_out.enableEXL := Y
        io.cp0_write_out.EXL := 0.U
        io.wb_if.bits.pc_w_data := reg_bru_wb.w_pc_addr
        io.wb_if.bits.pc_w_en := reg_bru_wb.w_pc_en
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
    }
    }
    //  when(io.wb_if.valid){
    //     printf(p"${reg_exec_wb}")
    // }
}