package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class InstrDecode extends Module{
    val io = IO(new Bundle{
        val if_id = Flipped(Decoupled(new IF_ID))
        val flush = Input(Bool())
        val out_gpr_read = Flipped(new GPRReadIntput)
        val id_isu = Decoupled(new ID_ISU)
    })
    val if_id_reg = RegEnable(next=io.if_id.bits, enable=io.if_id.fire())
    val if_id_reg_prepared = RegInit(false.B)
    io.if_id.ready := io.id_isu.ready || !if_id_reg_prepared

    val instr_op = if_id_reg.instr(31, 26)
    val funct = if_id_reg.instr(5, 0)
    val rt = if_id_reg.instr(20, 16)
    val rd = if_id_reg.instr(15, 11)

    io.id_isu.bits.imm := if_id_reg.instr(15, 0)
    io.id_isu.bits.shamt := if_id_reg.instr(10, 6)
    io.id_isu.bits.instr_index := if_id_reg.instr(26, 0)
    io.id_isu.bits.pcNext := if_id_reg.pcNext
    io.id_isu.bits.current_instr := if_id_reg.instr
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
            XOR  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_XOR_OP),
            AND  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_AND_OP),
            NOR  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_NOR_OP),  
            SLTI -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_SLT_OP),
            SLTIU-> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_SLTU_OP),
            SRA  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRA_OP),
            SRL  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRL_OP),
            SLL  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLL_OP),
            SRAV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRA_OP),
            SRLV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRL_OP),
            SLLV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLL_OP),
            ROTR -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_ROTR_OP),
            ROTRV-> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_ROTR_OP),
            ADDI -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_ADD_OP),
            ADDIU-> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_ADDU_OP),
            ANDI -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_AND_OP),
            ORI  -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_OR_OP),
            XORI -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_XOR_OP),
            SEB  -> List(rd, DontCare, RT_SEL, DontCare, ALU_ID, ALU_SEB_OP),
            SEH  -> List(rd, DontCare, RT_SEL, DontCare, ALU_ID, ALU_SEH_OP),
            WSBH -> List(rd, DontCare, RT_SEL, DontCare, ALU_ID, ALU_WSBH_OP),
            INS  -> List(rt, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_INS_OP),
            EXT  -> List(rt, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_EXT_OP),
            CLO  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_CLO_OP),
            CLZ  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_CLZ_OP),
            // TODO: MOVN 
            // TODO: MOVZ 

            BEQ    -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BEQ_OP),
            BNE    -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BNE_OP),
            BLTZ   -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BLTZ_OP),
            BGTZ   -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BGTZ_OP),
            BGEZ   -> List(DontCare, RS_SEL, DontCare, SIGN_EXT_SEL, BRU_ID, BRU_BGEZ_OP),
            BLEZ   -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BLEZ_OP),
            BLTZAL -> List(DontCare, RS_SEL, DontCare, SIGN_EXT_SEL, BRU_ID, BRU_BLTZAL_OP),
            BGEZAL -> List(DontCare, RS_SEL, DontCare, SIGN_EXT_SEL, BRU_ID, BRU_BGEZAL_OP),
            J      -> List(DontCare, DontCare, DontCare, DontCare, BRU_ID, BRU_J_OP),
            JAL    -> List(31.U(5.W), RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_JAL_OP),
            JR     -> List(DontCare, RS_SEL, DontCare, DontCare, BRU_ID, BRU_JR_OP),
            JALR   -> List(rd, RS_SEL, DontCare, DontCare, BRU_ID, BRU_JALR_OP),
            
            LW  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LW_OP),
            LH  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LH_OP),
            LHU -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LHU_OP),
            LB  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LB_OP),
            LBU -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LBU_OP), 
            LWL -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LWL_OP),
            LWR -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LWR_OP),
            SW  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SW_OP),
            SH  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SH_OP),
            SB  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SB_OP),
            SWL -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SWL_OP),
            SWR -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SWR_OP),     

            MFHI -> List(rd, DontCare, DontCare, DontCare, MDU_ID, MDU_MFHI_OP),
            MFLO -> List(rd, DontCare, DontCare, DontCare, MDU_ID, MDU_MFLO_OP),
            MTHI -> List(DontCare, RS_SEL, DontCare, DontCare, MDU_ID, MDU_MTHI_OP),
            MTLO -> List(DontCare, RS_SEL, DontCare, DontCare, MDU_ID, MDU_MTLO_OP),
            MUL  -> List(rd, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MUL_OP),
            MULT -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MULT_OP),
            MULTU-> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MULTU_OP),
            DIV  -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_DIV_OP),
            DIVU -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_DIVU_OP),
            MADD -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MADD_OP),
            MADDU-> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MADDU_OP),
            MSUB -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MSUB_OP),
            MSUBU-> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MSUBU_OP)
        )
    )
    io.id_isu.bits.rd_addr := decoded_instr(0)
    io.id_isu.bits.shamt_rs_sel := decoded_instr(1)
    io.id_isu.bits.imm_rt_sel := decoded_instr(2)
    io.id_isu.bits.sign_ext := decoded_instr(3)
    io.id_isu.bits.exu := decoded_instr(4)
    io.id_isu.bits.op := decoded_instr(5)
    
    io.out_gpr_read := if_id_reg.instr(25, 16).asTypeOf(new GPRReadIntput)

    io.id_isu.valid := if_id_reg_prepared
    when(io.flush || (!io.if_id.fire() && io.id_isu.fire())){
        if_id_reg_prepared := false.B
    } .elsewhen(!io.flush && io.if_id.fire()){
        if_id_reg_prepared := true.B
    }
}