package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class InstrDecode extends Module{
    val io = IO(new Bundle{
        val if_id = Flipped(Decoupled(new IF_ID))
        val flush = Input(Bool())
        val id_isu = Decoupled(new ID_ISU)
    })
    val if_id_reg = RegEnable(next=io.if_id.bits, enable=io.if_id.fire())
    val if_id_reg_prepared = RegInit(false.B)
    io.if_id.ready := io.id_isu.ready || !if_id_reg_prepared

    val instr_op = if_id_reg.instr(31, 26)
    val funct = if_id_reg.instr(5, 0)
    val rs = if_id_reg.instr(25, 21)
    val rt = if_id_reg.instr(20, 16)
    val rd = if_id_reg.instr(15, 11)

    io.id_isu.bits.imm := if_id_reg.instr(15, 0)
    io.id_isu.bits.shamt := if_id_reg.instr(10, 6)
    io.id_isu.bits.instr_index := if_id_reg.instr(26, 0)
    io.id_isu.bits.pcNext := if_id_reg.pcNext
    io.id_isu.bits.current_instr := if_id_reg.instr
    // rd_addr, shamt_rs_sel, imm_rt_sel, sign_ext, exu, op 
    val decoded_instr = ListLookup(if_id_reg.instr, List(rd, RS_SEL, RT_SEL, ZERO_EXT_SEL, ALU_ID, ALU_ADD_OP, 0.U, 0.U, 0.U),
        Array(
            LUI  -> List(rt, RS_SEL, IMM_SEL, DontCare, ALU_ID, ALU_LUI_OP, rt, rs, 0.U), 
            ADD  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_ADD_OP, rd, rs, rt),
            ADDU -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_ADDU_OP, rd, rs, rt),
            SUB  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SUB_OP, rd, rs, rt),
            SUBU -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SUBU_OP, rd, rs, rt),
            SLT  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLT_OP, rd, rs, rt),
            SLTU -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLTU_OP, rd, rs, rt),
            AND  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_AND_OP, rd, rs, rt),
            OR   -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_OR_OP, rd, rs, rt),
            XOR  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_XOR_OP, rd, rs, rt),
            AND  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_AND_OP, rd, rs, rt),
            NOR  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_NOR_OP, rd, rs, rt),  
            SLTI -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_SLT_OP, rt, rs, 0.U),
            SLTIU-> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_SLTU_OP, rt, rs, 0.U),
            SRA  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRA_OP, rd, 0.U, rt),
            SRL  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRL_OP, rd, 0.U, rt),
            SLL  -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLL_OP, rd, 0.U, rt),
            SRAV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRA_OP, rd, rs, rt),
            SRLV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SRL_OP, rd, rs, rt),
            SLLV -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_SLL_OP, rd, rs, rt),
            ROTR -> List(rd, SHAMT_SEL, RT_SEL, DontCare, ALU_ID, ALU_ROTR_OP, rd, 0.U, rt),
            ROTRV-> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_ROTR_OP, rd, rs, rt),
            ADDI -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_ADD_OP, rt, rs, 0.U),
            ADDIU-> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, ALU_ID, ALU_ADDU_OP, rt, rs, 0.U),
            ANDI -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_AND_OP, rt, rs, 0.U),
            ORI  -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_OR_OP, rt, rs, 0.U),
            XORI -> List(rt, RS_SEL, IMM_SEL, ZERO_EXT_SEL, ALU_ID, ALU_XOR_OP, rt, rs, 0.U),
            SEB  -> List(rd, DontCare, RT_SEL, DontCare, ALU_ID, ALU_SEB_OP, rd, 0.U, rt),
            SEH  -> List(rd, DontCare, RT_SEL, DontCare, ALU_ID, ALU_SEH_OP, rd, 0.U, rt),
            WSBH -> List(rd, DontCare, RT_SEL, DontCare, ALU_ID, ALU_WSBH_OP, rd, 0.U, rt),
            INS  -> List(rt, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_INS_OP, rt, rs, rt),
            EXT  -> List(rt, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_EXT_OP, rt, rs, rt),
            CLO  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_CLO_OP, rd, rs, rt),
            CLZ  -> List(rd, RS_SEL, RT_SEL, DontCare, ALU_ID, ALU_CLZ_OP, rd, rs, rt),
            // TODO: MOVN 
            // TODO: MOVZ 

            BEQ    -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BEQ_OP, 0.U, rs, rt),
            BNE    -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BNE_OP, 0.U, rs, rt),
            BLTZ   -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BLTZ_OP, 0.U, rs, rt),
            BGTZ   -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BGTZ_OP, 0.U, rs, rt),
            BGEZ   -> List(DontCare, RS_SEL, DontCare, SIGN_EXT_SEL, BRU_ID, BRU_BGEZ_OP, 0.U, rs, 0.U),
            BLEZ   -> List(DontCare, RS_SEL, RT_SEL, SIGN_EXT_SEL, BRU_ID, BRU_BLEZ_OP, 0.U, rs, rt),
            BLTZAL -> List(DontCare, RS_SEL, DontCare, SIGN_EXT_SEL, BRU_ID, BRU_BLTZAL_OP, 0.U, rs, 0.U),
            BGEZAL -> List(DontCare, RS_SEL, DontCare, SIGN_EXT_SEL, BRU_ID, BRU_BGEZAL_OP, 0.U, rs, 0.U),
            J      -> List(DontCare, DontCare, DontCare, DontCare, BRU_ID, BRU_J_OP, 0.U, 0.U, 0.U),
            JAL    -> List(31.U(5.W), RS_SEL, RT_SEL, DontCare, BRU_ID, BRU_JAL_OP, 31.U, rs, rt),
            JR     -> List(DontCare, RS_SEL, DontCare, DontCare, BRU_ID, BRU_JR_OP, 0.U, rs, 0.U),
            JALR   -> List(rd, RS_SEL, DontCare, DontCare, BRU_ID, BRU_JALR_OP, rd, rs, 0.U),
            
            LW  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LW_OP, rt, rs, 0.U),
            LH  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LH_OP, rt, rs, 0.U),
            LHU -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LHU_OP, rt, rs, 0.U),
            LB  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LB_OP, rt, rs, 0.U),
            LBU -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LBU_OP, rt, rs, 0.U), 
            LWL -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LWL_OP, rt, rs, 0.U),
            LWR -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_LWR_OP, rt, rs, 0.U),
            SW  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SW_OP, 0.U, rs, rt),
            SH  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SH_OP, 0.U, rs, rt),
            SB  -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SB_OP, 0.U, rs, rt),
            SWL -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SWL_OP, 0.U, rs, rt),
            SWR -> List(rt, RS_SEL, IMM_SEL, SIGN_EXT_SEL, LSU_ID, LSU_SWR_OP, 0.U, rs, rt),     

            MFHI -> List(rd, DontCare, DontCare, DontCare, MDU_ID, MDU_MFHI_OP, rd, 0.U, 0.U),
            MFLO -> List(rd, DontCare, DontCare, DontCare, MDU_ID, MDU_MFLO_OP, rd, 0.U, 0.U),
            MTHI -> List(DontCare, RS_SEL, DontCare, DontCare, MDU_ID, MDU_MTHI_OP, 0.U, rs, 0.U),
            MTLO -> List(DontCare, RS_SEL, DontCare, DontCare, MDU_ID, MDU_MTLO_OP, 0.U, rs, 0.U),
            MUL  -> List(rd, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MUL_OP, rd, rs, rt),
            MULT -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MULT_OP, 0.U, rs, rt),
            MULTU-> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MULTU_OP, 0.U, rs, rt),
            DIV  -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_DIV_OP, 0.U, rs, rt),
            DIVU -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_DIVU_OP, 0.U, rs, rt),
            MADD -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MADD_OP, 0.U, rs, rt),
            MADDU-> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MADDU_OP, 0.U, rs, rt),
            MSUB -> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MSUB_OP, 0.U, rs, rt),
            MSUBU-> List(DontCare, RS_SEL, RT_SEL, DontCare, MDU_ID, MDU_MSUBU_OP, 0.U, rs, rt)
        )
    )
    io.id_isu.bits.rd_addr := decoded_instr(0)
    io.id_isu.bits.shamt_rs_sel := decoded_instr(1)
    io.id_isu.bits.imm_rt_sel := decoded_instr(2)
    io.id_isu.bits.sign_ext := decoded_instr(3)
    io.id_isu.bits.exu := decoded_instr(4)
    io.id_isu.bits.op := decoded_instr(5)
    io.id_isu.bits.write := decoded_instr(6)
    io.id_isu.bits.read1 := decoded_instr(7)
    io.id_isu.bits.read2 := decoded_instr(8)
    
    io.id_isu.valid := if_id_reg_prepared && !io.flush
    when(io.flush || (!io.if_id.fire() && io.id_isu.fire())){
        if_id_reg_prepared := false.B
    } .elsewhen(!io.flush && io.if_id.fire()){
        if_id_reg_prepared := true.B
    }
    when(io.id_isu.fire()){
        //printf(p"out_gpr_read  ${io.out_gpr_read.rs_addr} ${io.out_gpr_read.rt_addr} ${io.id_isu.bits.rd_addr}\n")
    }
}