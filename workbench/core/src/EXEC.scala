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
        (r.alu_op === ALU_SRA_OP) -> (B_in.asSInt() >> A_in(4, 0).asUInt()).asUInt(),
        (r.alu_op === ALU_SRL_OP) -> (B_in >> A_in(4, 0).asUInt()),
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
    when(isu_bur_fire){
        printf("BRU WORKING\n")
    }
    val r = RegEnableUse(io.isu_bru.bits, io.isu_bru.fire())
    val bruwb = Wire(new BRU_WB)
    bruwb := DontCare
    bruwb.w_en := false.B
    bruwb.w_pc_en := false.B 
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
        BRU_JALR_OP -> true.B
    ))
    when(needJump){
        printf("NEED JUMP\n")
        bruwb.w_pc_en := true.B
        bruwb.w_pc_addr := MuxLookup(r.bru_op, (r.pcNext.asSInt() + (r.offset << 2).asSInt()).asUInt(), Array(
            BRU_J_OP -> Cat(r.pcNext(31, 28), Cat(r.instr_index, "b00".U(2.W))),
            BRU_JAL_OP -> Cat(r.pcNext(31, 28), Cat(r.instr_index, "b00".U(2.W))),
            BRU_JR_OP -> r.rsData,
            BRU_JALR_OP -> r.rsData,
        ))
        // when(r.bru_op === BRU_JALR_OP){
        //     printf(p"BRANCH WRITE TO ${r.rd} ${r.bru_op}\n")
        //     bruwb.w_en := true.B
        //     bruwb.w_addr := r.rd
        //     bruwb.w_data := r.pcNext + 4.U
        // }
    }
    when(VecInit(BRU_BGEZAL_OP, BRU_BLTZAL_OP, BRU_JAL_OP, BRU_JALR_OP).contains(r.bru_op)){
        printf(p"BRANCH WRITE TO 31 ${r.bru_op}\n")
        bruwb.w_en := true.B
        bruwb.w_addr := Mux(r.bru_op===BRU_JALR_OP, r.rd, 31.U)
        bruwb.w_data := r.pcNext + 4.U
    }
//bruwb.w_pc_addr := 
    io.exec_wb.valid := isu_bur_fire
    when(isu_bur_fire){
        printf(p"BRU_OP: ${r}\n")
        printf(p"BRUWB: ${bruwb}\n")
    }
    io.exec_wb.bits <> bruwb
}


class Divider extends Module {
    val io = IO(Flipped(new DividerIO))
    val dividend = io.data_dividend_bits
    val divisor = io.data_divisor_bits
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


class MDU extends Module{
    val io = IO(new Bundle{
        val isu_mdu = Flipped(Decoupled(new ISU_MDU))
        val exec_wb = Decoupled(new MDU_WB)
    })
    val multiplier = Module(new Multiplier)
    val dividor = Module(new Divider)
    
    io.isu_mdu.ready := true.B  // always ready 
    val isu_mdu_fired = RegEnable(io.isu_mdu.fire() & ~reset.asBool(), io.isu_mdu.fire())
    val isu_mdu_reg = RegEnable(io.isu_mdu.bits, io.isu_mdu.fire())
    
    val mdu_wb_valid = RegInit(false.B)
    val mdu_wb_reg = Reg(new MDU_WB)

    val hi = RegInit(0.U(32.W))
    val lo = RegInit(0.U(32.W))
    
    val multiplier_delay_count = RegInit((conf.mul_stages).U(3.W))    // 8

    when(isu_mdu_fired){
        // data_dividend_valid, data_divisor_valid, data_divident_bits, data_divisor_bits
        val dividor_input = MuxLookup(isu_mdu_reg.mdu_op, VecInit(false.B, false.B, DontCare, DontCare),
            Array(
                MDU_DIV_OP -> VecInit(true.B, true.B, isu_mdu_reg.rsData.asSInt(), isu_mdu_reg.rtData.asSInt()),
                MDU_DIVU_OP -> VecInit(true.B, true.B, isu_mdu_reg.rsData.asUInt(), isu_mdu_reg.rtData.asUInt())
            )
        )
        dividor.io.data_dividend_valid := dividor_input(0)
        dividor.io.data_divisor_valid := dividor_input(1)
        dividor.io.data_dividend_bits := dividor_input(2)
        dividor.io.data_divisor_bits := dividor_input(3)
        
        val multiplier_input = MuxLookup(isu_mdu_reg.mdu_op, VecInit((conf.mul_stages).U(3.W), DontCare, DontCare),
            Array(
                MDU_MULT_OP -> VecInit((conf.mul_stages-1).U(3.W), isu_mdu_reg.rsData.asSInt(), isu_mdu_reg.rtData.asSInt()),
                MDU_MULTU_OP -> VecInit((conf.mul_stages-1).U(3.W), isu_mdu_reg.rsData.asUInt(), isu_mdu_reg.rtData.asUInt()),
                MDU_MADD_OP -> VecInit((conf.mul_stages-1).U(3.W), isu_mdu_reg.rsData.asSInt(), isu_mdu_reg.rtData.asSInt()),
                MDU_MADDU_OP -> VecInit((conf.mul_stages-1).U(3.W), isu_mdu_reg.rsData.asUInt(), isu_mdu_reg.rtData.asUInt()),
                MDU_MSUB_OP -> VecInit((conf.mul_stages-1).U(3.W), isu_mdu_reg.rsData.asSInt(), isu_mdu_reg.rtData.asSInt()),
                MDU_MSUBU_OP -> VecInit((conf.mul_stages-1).U(3.W), isu_mdu_reg.rsData.asUInt(), isu_mdu_reg.rtData.asUInt())
            )
        )
        multiplier_delay_count := multiplier_input(0)
        multiplier.io.data_a := multiplier_input(1)
        multiplier.io.data_b := multiplier_input(2)
        
        mdu_wb_reg := MuxLookup(isu_mdu_reg.mdu_op, VecInit(false.B, DontCare, DontCare),
            Array(
                MDU_MFHI_OP -> VecInit(true.B, isu_mdu_reg.rd, hi),
                MDU_MFLO_OP -> VecInit(true.B, isu_mdu_reg.rd, lo)
            )
        ).asTypeOf(mdu_wb_reg)
        // mdu_wb_reg <> 
        // .w_en := mdu_wb(0)
        // mdu_wb_reg.w_addr := mdu_wb(1)
        // mdu_wb_reg.w_data := mdu_wb(2)

        hi := Mux(isu_mdu_reg.mdu_op===MDU_MTHI_OP, isu_mdu_reg.rsData, hi)
        lo := Mux(isu_mdu_reg.mdu_op===MDU_MTLO_OP, isu_mdu_reg.rsData, lo)

        when(VecInit(MDU_MTHI_OP, MDU_MTLO_OP, MDU_MFHI_OP, MDU_MFLO_OP).contains(isu_mdu_reg.mdu_op)){
            mdu_wb_valid := true.B
        } .otherwise{
            mdu_wb_valid := false.B
        }
    }
    when(multiplier_delay_count === 0.U(3.W)){  // Multiplier OK
        mdu_wb_reg.w_en := false.B 
        mdu_wb_valid := true.B
        hi := multiplier.io.data_dout(63, 32)
        lo := multiplier.io.data_dout(31, 0)
    }
    when(dividor.io.data_dout_valid){
        mdu_wb_reg.w_en := false.B 
        mdu_wb_valid := true.B
        hi := dividor.io.data_dout_bits(71, 40)
        lo := dividor.io.data_dout_bits(31, 0)  
    }

    when(multiplier_delay_count =/= conf.mul_stages.U(3.W)){
        multiplier_delay_count := multiplier_delay_count - 1.U
    }


    io.exec_wb.valid := mdu_wb_valid
    io.exec_wb.bits <> mdu_wb_reg
    when(io.exec_wb.fire()){
        mdu_wb_valid := false.B
    }
}