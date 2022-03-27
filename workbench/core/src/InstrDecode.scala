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
    //io.id_isu.bits := DontCare
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
    io.id_isu.bits.instr_index := if_id_reg.instr(26, 0)
    io.id_isu.bits.pcNext := if_id_reg.pcNext

    // rd_addr, shamt_rs_sel, imm_rt_sel, sign_ext, exu, op 
    val decoded_instr = ListLookup(if_id_reg.instr, List(rd, RS_SEL, RT_SEL, ZERO_EXT_SEL, ALU_ID, ALU_ADD_OP),
        Array(
            LUI  -> List(rt, RS_SEL, IMM_SEL, DontCare, ALU_ID, ALU_LUI_OP), 
            ADD  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_ADD_OP),
            ADDU -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_ADDU_OP),
            SUB  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SUB_OP),
            SUBU -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SUBU_OP),
            SLT  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLT_OP),
            SLTU -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLTU_OP),
            AND  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_AND_OP),
            OR   -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_OR_OP),
            AND  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_XOR_OP),
            NOR  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_NOR_OP),  
            SLTI -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_SLT_OP),
            SLTIU-> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_SLTU_OP),
            SRA  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRA_OP),
            SRL  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRL_OP),
            SLL  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLL_OP),
            SRAV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRA_OP),
            SRLV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRL_OP),
            SLLV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLL_OP),
            
            ADDI -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_ADD_OP),
            ADDIU-> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_ADDU_OP),
            ANDI -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_AND_OP),
            ORI  -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_OR_OP),
            XORI -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_XOR_OP),
            // TODO: MOVN 
            // TODO: MOVZ 

            BEQ -> List(DontCare, RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_BEQ_OP),
            BNE -> List(DontCare, RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_BNE_OP),
            BLEZ -> List(DontCare, RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_BLEZ_OP),
            BGTZ -> List(DontCare, RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_BGTZ_OP),
            BLEZ -> List(DontCare, RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_BLEZ_OP),
            J -> List(DontCare, DontCare, DontCare, DontCare, BRU_ID, BRU_J_OP),
            JAL -> List(31.U(5.W), RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_JAL_OP),
            JR -> List(DontCare, RS_SEL, DontCare, DontCare, BRU_ID, BRU_JR_OP),
            JALR -> List(rd, RS_SEL, DontCare, DontCare, BRU_ID,  BRU_JALR_OP),
        )
    )
    io.id_isu.bits.rd_addr := decoded_instr(0)
    io.id_isu.bits.shamt_rs_sel := decoded_instr(1)
    io.id_isu.bits.imm_rt_sel := decoded_instr(2)
    io.id_isu.bits.sign_ext := decoded_instr(3)
    io.id_isu.bits.exu := decoded_instr(4)
    io.id_isu.bits.op := decoded_instr(5)
    
    io.id_isu.valid := if_id_fire  & ~reset.asBool()   // TODO: complete in 1 cycle

    when(io.id_isu.valid){
        printf(p"xxx${if_id_reg}")
    }

    io.out_gpr_read := if_id_reg.instr(25, 16).asTypeOf(new GPRReadIntput)
}