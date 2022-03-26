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
        (r.alu_op === ALU_SLL_OP)-> (B_in << A_in(3, 0).asUInt()),
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
            BRU_J_OP -> Cat(r.pcNext(31, 28), (r.instr_index << 2)).asUInt(),
            BRU_JAL_OP -> Cat(r.pcNext(31, 28), (r.instr_index << 2)).asUInt(),
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
    io.exec_wb.bits <> bruwb
}