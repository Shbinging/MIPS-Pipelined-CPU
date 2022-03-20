package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class InstrDecode extends Module{
    val io = IO(new Bundle{
        val if_id = Flipped(Decoupled(new IF_ID))
        val out_gpr_read = Flipped(new GPRReadIntput)
        val id_isu = Decoupled(new ID_ISU)
    })
    io.if_id.ready := true.B   // unidir handshake 
    val if_id_fire = RegNext(io.if_id.fire())
    val if_id_reg = RegNext(io.if_id.bits)// RegEnableUse(io.if_id.bits, if_id_fire)
    printf(p"decode working: ${if_id_fire}\n")


    val instr_op = if_id_reg.instr(31, 26)
    val funct = if_id_reg.instr(5, 0)
    val rt = if_id_reg.instr(20, 16)
    val rd = if_id_reg.instr(15, 11)

    io.id_isu.bits.imm := if_id_reg.instr(15, 0)
    io.id_isu.bits.shamt := if_id_reg.instr(10, 6)

    // rd_addr, shamt_rs_sel, sign_ext, exu, op, imm_rt_sel
    val decoded_instr = ListLookup(if_id_reg.instr, List(rd, false.B, false.B, ALU_ID, ALU_ADD_OP, false.B),
        Array(
            LUI  -> List(rt, true.B, DontCare, ALU_ID, ALU_LUI_OP, false.B, 20.U), 
            ADD  -> List(rd, true.B, DontCare, ALU_ID, ALU_ADD_OP, true.B, 20.U),
            ADDU -> List(rd, true.B, DontCare, ALU_ID, ALU_ADDU_OP, true.B, 20.U),
            SUB  -> List(rd, true.B, DontCare, ALU_ID, ALU_SUB_OP, true.B, 20.U),
            SUBU -> List(rd, true.B, DontCare, ALU_ID, ALU_SUBU_OP, true.B, 20.U),
            SLT  -> List(rd, true.B, DontCare, ALU_ID, ALU_SLT_OP, true.B, 20.U),
            SLTU -> List(rd, true.B, DontCare, ALU_ID, ALU_SLTU_OP, true.B, 20.U),
            AND  -> List(rd, true.B, DontCare, ALU_ID, ALU_AND_OP, true.B, 20.U),
            OR   -> List(rd, true.B, DontCare, ALU_ID, ALU_OR_OP, true.B, 20.U),
            AND  -> List(rd, true.B, DontCare, ALU_ID, ALU_XOR_OP, true.B, 20.U),
            NOR  -> List(rd, true.B, DontCare, ALU_ID, ALU_NOR_OP, true.B, 20.U),  
            SLTI -> List(rt, true.B, true.B, ALU_ID, ALU_SLT_OP, false.B, 20.U),
            SLTIU-> List(rt, true.B, true.B, ALU_ID, ALU_SLTU_OP, false.B, 20.U),
            SRA  -> List(rd, false.B, DontCare, ALU_ID, ALU_SRA_OP, true.B, 20.U),
            SRL  -> List(rd, false.B, DontCare, ALU_ID, ALU_SRL_OP, true.B, 20.U),
            SLL  -> List(rd, false.B, DontCare, ALU_ID, ALU_SLL_OP, true.B, 20.U),
            SRAV -> List(rd, true.B, DontCare, ALU_ID, ALU_SRA_OP, true.B, 20.U),
            SRLV -> List(rd, true.B, DontCare, ALU_ID, ALU_SRL_OP, true.B, 20.U),
            SLLV -> List(rd, true.B, DontCare, ALU_ID, ALU_SLL_OP, true.B, 20.U),
            
            ADDI -> List(rt, true.B, true.B, ALU_ID, ALU_ADD_OP, false.B, 20.U),
            ADDIU-> List(rt, true.B, true.B, ALU_ID, ALU_ADDU_OP, false.B, 20.U),
            ANDI -> List(rt, true.B, false.B, ALU_ID, ALU_AND_OP, false.B, 20.U),
            ORI  -> List(rt, true.B, false.B, ALU_ID, ALU_OR_OP, false.B, 20.U),
            XORI -> List(rt, true.B, false.B, ALU_ID, ALU_XOR_OP, false.B, 20.U),
        )
    )
    io.id_isu.bits.rd_addr := decoded_instr(0)
    io.id_isu.bits.shamt_rs_sel := decoded_instr(1)
    io.id_isu.bits.sign_ext := decoded_instr(2)
    io.id_isu.bits.exu := decoded_instr(3)
    io.id_isu.bits.op := decoded_instr(4)
    io.id_isu.bits.imm_rt_sel := decoded_instr(5)
    io.id_isu.valid := if_id_fire  & ~reset.asBool()   // complete in 1 cycle

    when(io.id_isu.valid){
        printf(p"xxx${if_id_reg}")
    }

    io.out_gpr_read := if_id_reg.instr(25, 16).asTypeOf(new GPRReadIntput)
}