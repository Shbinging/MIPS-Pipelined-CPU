package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._



class ALU extends Module{
    val io = IO{new Bundle{
        val isu_alu = Flipped(Decoupled(new ISU_ALU))
        val out = new ALUOutput
        val exec_wb = Decoupled(new EXEC_WB)
    }}
    io.isu_alu.ready := true.B
    val isu_alu_fire = RegNext(io.isu_alu.fire()  & ~reset.asBool())
    val r = RegEnableUse(io.isu_alu.bits, io.isu_alu.fire())
    val A_in = WireInit(r.operand_1)
    val B_in = WireInit(r.operand_2)
    val ALU_op = WireInit(r.alu_op)
    val ALU_out = Wire(UInt(32.W))  
    ALU_out := Mux1H(Array(
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

    io.out.ALU_out := ALU_out
    io.out.Overflow_out := false.B //XXX:modify when need exception
    
    io.exec_wb.bits.w_addr := io.isu_alu.bits.rd_addr
    io.exec_wb.bits.w_en := isu_alu_fire
    io.exec_wb.bits.exu_id := ALU_ID
    io.exec_wb.valid := isu_alu_fire  // 1 cycle 
    when (isu_alu_fire){
    printf("=====EXEC=====\n")
    printf(p"${io.isu_alu}\n")
    printf(p"${io.out}\n")
    printf(p"${io.exec_wb}\n")
    printf("==========\n")
    }
}