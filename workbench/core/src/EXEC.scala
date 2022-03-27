package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._



class ALU extends Module{
    val io = IO{new Bundle{
        val isu_alu = Flipped(Decoupled(new ISU_ALU))
        val exec_wb = Decoupled(new ALU_WB)
    }}
    io.isu_alu.ready := true.B
    val isu_alu_fire = RegNext(io.isu_alu.fire()  & ~reset.asBool())
    val r = RegEnableUse(io.isu_alu.bits, io.isu_alu.fire())
    val A_in = WireInit(r.operand_1)
    val B_in = WireInit(r.operand_2)
    val ALU_op = WireInit(r.alu_op)
    val ALU_out = Wire(UInt(32.W))  
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
        (r.alu_op === ALU_SRA_OP) -> (B_in.asSInt() >> A_in(3, 0).asUInt()).asUInt(),
        (r.alu_op === ALU_SRL_OP) -> (B_in >> A_in(3, 0).asUInt()),
        (r.alu_op === ALU_SUBU_OP) -> (A_in - B_in),
        (r.alu_op === ALU_SUB_OP) -> (A_in.asSInt() - B_in.asSInt()).asUInt(),
        (r.alu_op === ALU_XOR_OP) -> (A_in ^ B_in),
    ))(31, 0)

    //io.out.ALU_out := ALU_out
    //io.out.Overflow_out := false.B //XXX:modify when need exception
    //io.exec_wb.bits.
    io.exec_wb.bits.ALU_out := ALU_out
    io.exec_wb.bits.Overflow_out := false.B //XXX:modify when need exception
    io.exec_wb.bits.w_addr := io.isu_alu.bits.rd_addr
    io.exec_wb.bits.w_en := isu_alu_fire
    io.exec_wb.valid := isu_alu_fire  // 1 cycle 
    when (isu_alu_fire){
    printf("=====EXEC=====\n")
    printf(p"${io.isu_alu}\n")
    //printf(p"${io.out}\n")
    printf(p"${io.exec_wb}\n")
    printf("==========\n")
    }
}

class BRU extends Module{
    val io = IO{new Bundle{
        val isu_bru = Flipped(Decoupled(new ISU_BRU))
        val exec_wb = Decoupled(new BRU_WB)
    }}
    io.isu_bru.ready := true.B  // XXX: always ready
    
    val isu_bur_fire = RegNext(io.isu_bru.fire()  & ~reset.asBool())
    val r = RegEnableUse(io.isu_bru.bits, io.isu_bru.fire())
    val bruwb = Wire(new BRU_WB)
    bruwb := DontCare
    bruwb.w_en := false.B
    bruwb.w_pc_en := false.B 
    val needJump = MuxLookup(r.bru_op, false.B, Array(
        BRU_BEQ_OP -> (r.rsData === r.rtData).asBool(),
        BRU_BNE_OP -> (r.rsData =/= r.rtData).asBool(),
        BRU_BGEZ_OP -> (r.rsData >= 0.U).asBool(),
        BRU_BGTZ_OP -> (r.rsData > 0.U).asBool(),
        BRU_BLEZ_OP -> (r.rsData <= 0.U).asBool(),
        BRU_BLTZ_OP -> (r.rsData < 0.U).asBool(),
        BRU_BGEZAL_OP -> (r.rsData >= 0.U).asBool(),
        BRU_BLTZAL_OP -> (r.rsData < 0.U).asBool(),
        BRU_J_OP -> true.B,
        BRU_JAL_OP -> true.B,
        BRU_JR_OP -> true.B,
        BRU_JALR_OP -> true.B
    ))
    when(needJump){
        bruwb.w_pc_en := true.B
        bruwb.w_pc_addr := MuxLookup(r.bru_op, (r.pcNext.asSInt() + (r.offset << 2).asSInt()).asUInt(), Array(
            BRU_J_OP -> Cat(r.pcNext(31, 28), Cat(r.instr_index, "b00".U(2.W))),
            BRU_JAL_OP -> Cat(r.pcNext(31, 28), Cat(r.instr_index, "b00".U(2.W))),
            BRU_JR_OP -> r.rsData,
            BRU_JALR_OP -> r.rsData,
        ))
        when(VecInit(BRU_BGEZAL_OP, BRU_BLTZAL_OP, BRU_JAL_OP).contains(r.bru_op)){
            bruwb.w_en := true.B
            bruwb.w_addr := 31.U
            bruwb.w_data := r.pcNext + 4.U
        }
        when(r.bru_op === BRU_JALR_OP){
            bruwb.w_en := true.B
            bruwb.w_addr := r.rd
            bruwb.w_data := r.pcNext + 4.U
        }
    }
//bruwb.w_pc_addr := 
    io.isu_bru.ready := true.B
    io.exec_wb.valid := isu_bur_fire
    when(isu_bur_fire){
        printf(p"BRU_OP: ${r}\n")
        printf(p"BRUWB: ${bruwb}\n")
    }
    io.exec_wb.bits <> bruwb
}

class LSU extends Module{
    val io = IO{new Bundle{
        val isu_lsu = Flipped(Decoupled(new ISU_LSU))
        val exec_wb = Decoupled(new LSU_WB)
    }}
    io.exec_wb.bits := DontCare
    io.isu_lsu.ready := true.B
    val isu_lsu_fire = RegNext(io.isu_lsu.fire()  & ~reset.asBool())
    val r = RegEnable(io.isu_lsu.bits, io.isu_lsu.fire())
    val dev = Module(new SimDev)
    dev.io.clock := clock
    dev.io.reset := reset.asBool()
    
    dev.io.in.req.bits.is_cached := DontCare
    dev.io.in.req.bits.strb := DontCare
    dev.io.in.req.bits.data := DontCare
    dev.io.in.req.valid := isu_lsu_fire //XXX:need modify when cycling
    dev.io.in.resp.ready := true.B // & ~reset.asBool()

    val vAddr = WireInit((r.imm.asSInt() + r.rsData.asSInt()).asUInt())
    val shiftPos = VecInit(0.U, 8.U, 16.U, 24.U, 32.U)
    val index = vAddr(1, 0).asUInt()
    dev.io.in.req.bits.addr := vAddr
    val resp_fire = RegNext(dev.io.in.resp.fire().asBool())
    val resp_data_reg = RegEnable(dev.io.in.resp.bits, dev.io.in.resp.fire())
    when(VecInit(LSU_LBU_OP, LSU_LB_OP, LSU_LH_OP, LSU_LWL_OP, LSU_LWR_OP, LSU_LW_OP).contains(r.lsu_op)){
        val decoded_instr = ListLookup(r.lsu_op, List(MX_RD, 0.U), Array(
            BitPat(LSU_LBU_OP)->List(MX_RD, 0.U),
            BitPat(LSU_LB_OP)->List(MX_RD, 0.U),
            BitPat(LSU_LH_OP)->List(MX_RD, 1.U),
            BitPat(LSU_LWL_OP)->List(MX_RD, 3.U),
            BitPat(LSU_LWR_OP)->List(MX_RD, 3.U),
            BitPat(LSU_LW_OP)->List(MX_RD, 3.U),
        ))
        dev.io.in.req.bits.func := decoded_instr(0)
        dev.io.in.req.bits.len := decoded_instr(1)
        when(resp_fire){
            io.exec_wb.bits.w_en := "b1111".U
            io.exec_wb.bits.w_addr := r.rt
            io.exec_wb.bits.w_data := MuxLookup(r.lsu_op, 0.U, Seq(
                LSU_LB_OP->((resp_data_reg.data & 0xff.U).asTypeOf(SInt(32.W))).asUInt(),
                LSU_LBU_OP->(resp_data_reg.data & 0xff.U).asUInt(),
                LSU_LH_OP -> ((resp_data_reg.data &0xffff.U).asTypeOf(SInt(32.W))).asUInt(),
                LSU_LHU_OP -> (resp_data_reg.data &0xffff.U).asUInt(),
                LSU_LW_OP -> ((resp_data_reg.data).asTypeOf(SInt(32.W))).asUInt(),
                LSU_LWL_OP -> ((resp_data_reg.data) << shiftPos(3.U - index)),
                LSU_LWR_OP -> ((resp_data_reg.data) >> shiftPos(index)),
            )).asTypeOf(UInt(32.W))
            when(r.lsu_op === LSU_LWL_OP){
                    val shiftMask1 = VecInit("b1000".U, "b1100".U, "b1110".U, "b1111".U)
                    io.exec_wb.bits.w_en := shiftMask1(index)
            }
            when(r.lsu_op === LSU_LWR_OP){
                val shiftMask2 = VecInit("b1111".U, "b0111".U, "b0011".U, "b0001".U)
                io.exec_wb.bits.w_en := shiftMask2(index)
            }   
        }.otherwise{
        dev.io.in.req.bits.func := MX_WR
        dev.io.in.req.bits.data := MuxLookup(r.lsu_op, 0.U,Array(
            LSU_SB_OP -> r.rtData(7, 0),
            LSU_SH_OP -> r.rtData(15, 0),
            LSU_SW_OP -> r.rtData,
            LSU_SWL_OP -> (r.rtData >> shiftPos(3.U - index)),
            LSU_SWR_OP -> (r.rsData >> shiftPos(index))
        ))
        dev.io.in.req.bits.len := 3.U
        when(r.lsu_op === LSU_SWL_OP){
            dev.io.in.req.bits.len := index
        }
        when(r.lsu_op === LSU_SWR_OP){
            dev.io.in.req.bits.len := 3.U - index
            dev.io.in.req.bits.addr := vAddr + index
        }
        io.exec_wb.bits.w_en := 0.U
    }
}


    io.exec_wb.valid := resp_fire & ~reset.asBool()
}