package njumips

import chisel3._
import chisel3.util._
import njumips.configs._

trait MemConsts {
  // MX
  val MX_SZ = 1
  val MX_X  = 0.U(MX_SZ.W)
  val MX_RD = 0.U(MX_SZ.W)
  val MX_WR = 1.U(MX_SZ.W)

  val ML_SZ = 2
  val ML_X  = 0.U(ML_SZ.W)
  val ML_1  = 0.U(ML_SZ.W)
  val ML_2  = 1.U(ML_SZ.W)
  val ML_3  = 2.U(ML_SZ.W)
  val ML_4  = 3.U(ML_SZ.W)
}

trait CP0Consts {
  // exception type
  val ET_WIDTH = 5
  val ET_None       =   0.U(ET_WIDTH.W)
  val ET_EJTAG      =   1.U(ET_WIDTH.W)
  val ET_RESET      =   2.U(ET_WIDTH.W)
  val ET_SOFT_RST   =   3.U(ET_WIDTH.W)
  val ET_NMI        =   4.U(ET_WIDTH.W)
  val ET_MCheck     =   5.U(ET_WIDTH.W)
  val ET_ADDR_ERR   =   6.U(ET_WIDTH.W)
  val ET_TLB_REFILL =   7.U(ET_WIDTH.W)
  val ET_TLB_Inv    =   8.U(ET_WIDTH.W)
  val ET_TLB_Mod    =   9.U(ET_WIDTH.W)
  val ET_CACHE_ERR  =  10.U(ET_WIDTH.W)
  val ET_BUS_ERR    =  11.U(ET_WIDTH.W)
  val ET_Ov         =  12.U(ET_WIDTH.W)
  val ET_Tr         =  13.U(ET_WIDTH.W)
  val ET_Sys        =  14.U(ET_WIDTH.W)
  val ET_Bp         =  15.U(ET_WIDTH.W)
  val ET_RI         =  16.U(ET_WIDTH.W)
  val ET_CpU        =  17.U(ET_WIDTH.W)
  val ET_FPE        =  18.U(ET_WIDTH.W)
  val ET_C3E        =  18.U(ET_WIDTH.W)
  val ET_WATCH      =  20.U(ET_WIDTH.W)
  val ET_Int        =  21.U(ET_WIDTH.W)
  val ET_Eret       =  22.U(ET_WIDTH.W)

  val EC_WIDTH = 5
  val EC_Int  =  0.U(EC_WIDTH.W)  // - Interrupt
  val EC_Mod  =  1.U(EC_WIDTH.W)  // * TLB modification
  val EC_TLBL =  2.U(EC_WIDTH.W)  // * TLB load
  val EC_TLBS =  3.U(EC_WIDTH.W)  // * TLB store
  val EC_AdEL =  4.U(EC_WIDTH.W)  // * Address Load
  val EC_AdES =  5.U(EC_WIDTH.W)  // * Address Store
  val EC_IBE  =  6.U(EC_WIDTH.W)  //   Bus error(IF)
  val EC_DBE  =  7.U(EC_WIDTH.W)  //   Bus error(data)
  val EC_Sys  =  8.U(EC_WIDTH.W)  // * Syscall
  val EC_Bp   =  9.U(EC_WIDTH.W)  //   Break Point
  val EC_RI   = 10.U(EC_WIDTH.W)  // * Reserved instruction
  val EC_CpU  = 11.U(EC_WIDTH.W)  // * Coprocessor unusable
  val EC_Ov   = 12.U(EC_WIDTH.W)  // * Arithmetic overflow
  val EC_Tr   = 13.U(EC_WIDTH.W)  // * Trap

  val cp0_status = 44.U(6.W)
  val cp0_cause = 45.U(6.W)
  val cp0_epc = 46.U(6.W)
  val cp0_badvaddr = 40.U(6.W)
}

trait CacheConsts {
  val CACHE_OP_SZ = 3

  val I_INDEX_INVALIDATE = "b000".U(CACHE_OP_SZ.W)
  val I_INDEX_LOAD_TAG   = "b001".U(CACHE_OP_SZ.W)
  val I_INDEX_STORE_TAG  = "b010".U(CACHE_OP_SZ.W)
  val I_HIT_INVALIDATE   = "b100".U(CACHE_OP_SZ.W)
  val I_FILL             = "b101".U(CACHE_OP_SZ.W)
  val I_FETCH_AND_LOCK   = "b111".U(CACHE_OP_SZ.W)

  val D_INDEX_WB_INV     = "b000".U(CACHE_OP_SZ.W)
  val D_INDEX_LOAD_TAG   = "b001".U(CACHE_OP_SZ.W)
  val D_INDEX_STORE_TAG  = "b010".U(CACHE_OP_SZ.W)
  val D_HIT_INVALIDATE   = "b100".U(CACHE_OP_SZ.W)
  val D_HIT_WB_INV       = "b101".U(CACHE_OP_SZ.W)
  val D_WB               = "b110".U(CACHE_OP_SZ.W)
  val D_FETCH_AND_LOCK   = "b111".U(CACHE_OP_SZ.W)

  val S_INDEX_WB_INV     = "b000".U(CACHE_OP_SZ.W)
  val S_INDEX_LOAD_TAG   = "b001".U(CACHE_OP_SZ.W)
  val S_INDEX_STORE_TAG  = "b010".U(CACHE_OP_SZ.W)
  val S_HIT_INVALIDATE   = "b100".U(CACHE_OP_SZ.W)
  val S_HIT_WB_INV       = "b101".U(CACHE_OP_SZ.W)
  val S_WB               = "b110".U(CACHE_OP_SZ.W)
  val S_FETCH_AND_LOCK   = "b111".U(CACHE_OP_SZ.W)

  val CONTROL_ICACHE = "b00".U(2.W)
  val CONTROL_DCACHE = "b01".U(2.W)
  val CONTROL_SCACHE = "b10".U(2.W)
  val CONTROL_TCACHE = "b11".U(2.W)
}

trait InstrConsts {
  val REG_SZ    = 6;
  val IMM_SZ    = 16;
  val SHAMT_SZ  = 5;
}

trait DecodeSelectors {
    val SHAMT_SEL = false.B
    val RS_SEL = true.B
    
    val IMM_SEL=false.B 
    val RT_SEL=true.B

    val ZERO_EXT_SEL = false.B
    val SIGN_EXT_SEL = true.B
}

trait ExecUnitIndices {
  val EX_ID_WIDTH = 2
  val ALU_ID      = 0.U(EX_ID_WIDTH.W)
  val BRU_ID      = 1.U(EX_ID_WIDTH.W)
  val LSU_ID      = 2.U(EX_ID_WIDTH.W)
  val MDU_ID      = 3.U(EX_ID_WIDTH.W)

  val OPCODE_WIDTH = 5
  val ALU_ADDU_OP = "b00000".U(OPCODE_WIDTH.W)
  val ALU_SUBU_OP = "b00001".U(OPCODE_WIDTH.W)
  val ALU_LUI_OP = "b00010".U(OPCODE_WIDTH.W)
  val ALU_NOR_OP = "b00011".U(OPCODE_WIDTH.W)
  val ALU_AND_OP = "b00100".U(OPCODE_WIDTH.W)
  val ALU_SLT_OP = "b00101".U(OPCODE_WIDTH.W)
  val ALU_OR_OP = "b00110".U(OPCODE_WIDTH.W)
  val ALU_SLTU_OP = "b00111".U(OPCODE_WIDTH.W)
  val ALU_SLL_OP = "b01000".U(OPCODE_WIDTH.W)
  val ALU_XOR_OP = "b01001".U(OPCODE_WIDTH.W)
  val ALU_SRL_OP = "b01010".U(OPCODE_WIDTH.W)
  val ALU_SRA_OP = "b01011".U(OPCODE_WIDTH.W)
  val ALU_ROTR_OP= "b01100".U(OPCODE_WIDTH.W)
  val ALU_ADD_OP = "b01110".U(OPCODE_WIDTH.W)
  val ALU_SUB_OP = "b01111".U(OPCODE_WIDTH.W)
  val ALU_SEB_OP = "b10000".U(OPCODE_WIDTH.W)
  val ALU_SEH_OP = "b10001".U(OPCODE_WIDTH.W)
  val ALU_WSBH_OP= "b10010".U(OPCODE_WIDTH.W)
  val ALU_INS_OP = "b10011".U(OPCODE_WIDTH.W)
  val ALU_EXT_OP = "b10100".U(OPCODE_WIDTH.W)
  val ALU_CLO_OP = "b10101".U(OPCODE_WIDTH.W)
  val ALU_CLZ_OP = "b10110".U(OPCODE_WIDTH.W)
  val ALU_MOVZ_OP= "b10111".U(OPCODE_WIDTH.W)
  val ALU_MOVN_OP= "b11000".U(OPCODE_WIDTH.W)

  //XXX:3-27
  val BRU_BEQ_OP = "b0000".U(OPCODE_WIDTH.W)
  val BRU_BNE_OP= "b0001".U(OPCODE_WIDTH.W)
  val BRU_BGEZ_OP= "b0010".U(OPCODE_WIDTH.W) 
  val BRU_BGTZ_OP= "b0011".U(OPCODE_WIDTH.W) 
  val BRU_BLEZ_OP= "b0100".U(OPCODE_WIDTH.W) 
  val BRU_BLTZ_OP= "b0101".U(OPCODE_WIDTH.W) 
  val BRU_BGEZAL_OP= "b0110".U(OPCODE_WIDTH.W) 
  val BRU_BLTZAL_OP= "b0111".U(OPCODE_WIDTH.W) 
  val BRU_J_OP= "b1000".U(OPCODE_WIDTH.W) 
  val BRU_JAL_OP= "b1001".U(OPCODE_WIDTH.W) 
  val BRU_JR_OP= "b1010".U(OPCODE_WIDTH.W) 
  val BRU_JALR_OP= "b1011".U(OPCODE_WIDTH.W) 
  val BRU_ERET_OP = "b1100".U(OPCODE_WIDTH.W)

  val LSU_LW_OP    = "b0000".U(OPCODE_WIDTH.W)
  val LSU_LH_OP    = "b0001".U(OPCODE_WIDTH.W)
  val LSU_LB_OP    = "b0010".U(OPCODE_WIDTH.W)
  val LSU_LBU_OP   = "b0011".U(OPCODE_WIDTH.W)
  val LSU_LWL_OP   = "b0100".U(OPCODE_WIDTH.W)
  val LSU_LWR_OP   = "b0101".U(OPCODE_WIDTH.W)
  val LSU_SW_OP    = "b0110".U(OPCODE_WIDTH.W)
  val LSU_SH_OP    = "b0111".U(OPCODE_WIDTH.W)
  val LSU_SB_OP    = "b1000".U(OPCODE_WIDTH.W)
  val LSU_SWL_OP   = "b1001".U(OPCODE_WIDTH.W)
  val LSU_SWR_OP   = "b1010".U(OPCODE_WIDTH.W)
  val LSU_LHU_OP =  "b1011".U(OPCODE_WIDTH.W)

  val STATUS_WIDTH = 3
  val LSU_DIE = "b000".U(STATUS_WIDTH.W)
  val LSU_DECODE = "b001".U(STATUS_WIDTH.W)
  val LSU_READ = "b010".U(STATUS_WIDTH.W)
  val LSU_CALC = "b011".U(STATUS_WIDTH.W)
  val LSU_WRITE = "b100".U(STATUS_WIDTH.W)
  val LSU_BACK = "b101".U(STATUS_WIDTH.W)

  val LSU_FUNC_B = 0.U(4.W)
  val LSU_FUNC_BU = 1.U(4.W)
  val LSU_FUNC_H = 2.U(4.W)
  val LSU_FUNC_HU = 3.U(4.W)
  val LSU_FUNC_W = 4.U(4.W)
  val LSU_FUNC_WU = 5.U(4.W)
  val LSU_FUNC_RWL = 6.U(4.W)
  val LSU_FUNC_RWR = 7.U(4.W)
  val LSU_FUNC_WWL = 8.U(4.W)
  val LSU_FUNC_WWR = 9.U(4.W)
  //MDU
  val MDU_DIV_OP = "b0000".U(OPCODE_WIDTH.W)
  val MDU_DIVU_OP = "b0001".U(OPCODE_WIDTH.W)
  val MDU_MULT_OP = "b0010".U(OPCODE_WIDTH.W)
  val MDU_MULTU_OP = "b0011".U(OPCODE_WIDTH.W)
  val MDU_MFHI_OP = "b0100".U(OPCODE_WIDTH.W)
  val MDU_MFLO_OP = "b0101".U(OPCODE_WIDTH.W)
  val MDU_MTHI_OP = "b0110".U(OPCODE_WIDTH.W)
  val MDU_MTLO_OP = "b0111".U(OPCODE_WIDTH.W)
  val MDU_MADD_OP = "b1000".U(OPCODE_WIDTH.W)
  val MDU_MADDU_OP = "b1001".U(OPCODE_WIDTH.W)
  val MDU_MSUB_OP = "b1010".U(OPCODE_WIDTH.W)
  val MDU_MSUBU_OP = "b1011".U(OPCODE_WIDTH.W)
  val MDU_MUL_OP = "b1100".U(OPCODE_WIDTH.W)

  //PRU
  val PRU_MFC0_OP = 0.U(4.W)
  val PRU_MTC0_OP = 1.U(4.W)
  val PRU_ERET_OP = 2.U(4.W)
}

trait InstrPattern {
  val LUI   = BitPat("b00111100000?????????????????????")
  val ADD   = BitPat("b000000???????????????00000100000")
  val ADDU  = BitPat("b000000???????????????00000100001")
  val SUB   = BitPat("b000000???????????????00000100010")
  val SUBU  = BitPat("b000000???????????????00000100011")
  val SLT   = BitPat("b000000???????????????00000101010")
  val SLTU  = BitPat("b000000???????????????00000101011")
  val AND   = BitPat("b000000???????????????00000100100")
  val OR    = BitPat("b000000???????????????00000100101")
  val XOR   = BitPat("b000000???????????????00000100110")
  val NOR   = BitPat("b000000???????????????00000100111")
  val SLTI  = BitPat("b001010??????????????????????????")
  val SLTIU = BitPat("b001011??????????????????????????")
  val SRA   = BitPat("b00000000000???????????????000011")
  val SRL   = BitPat("b00000000000???????????????000010")
  val SLL   = BitPat("b00000000000???????????????000000")
  val SRAV  = BitPat("b000000???????????????00000000111")
  val SRLV  = BitPat("b000000???????????????00000000110")
  val SLLV  = BitPat("b000000???????????????00000000100")
  val ROTR  = BitPat("b00000000001???????????????000010")
  val ROTRV = BitPat("b000000???????????????00001000110")
  val SEB   = BitPat("b01111100000??????????10000100000")
  val SEH   = BitPat("b01111100000??????????11000100000")
  val WSBH  = BitPat("b01111100000??????????00010100000")
  val INS   = BitPat("b011111????????????????????000100")
  val EXT   = BitPat("b011111????????????????????000000")
  val CLO   = BitPat("b011100???????????????00000100001")
  val CLZ   = BitPat("b011100???????????????00000100000")

  val ADDI  = BitPat("b001000??????????????????????????")
  val ADDIU = BitPat("b001001??????????????????????????")
  val ANDI  = BitPat("b001100??????????????????????????")
  val ORI   = BitPat("b001101??????????????????????????")
  val XORI  = BitPat("b001110??????????????????????????")
  val MOVN  = BitPat("b000000???????????????00000001011")
  val MOVZ  = BitPat("b000000???????????????00000001010")

  val BEQ   = BitPat("b000100??????????????????????????")
  val BNE   = BitPat("b000101??????????????????????????")
  val BLEZ  = BitPat("b000110?????00000????????????????")
  val BGEZ  = BitPat("b000001?????00001????????????????")
  val BGTZ  = BitPat("b000111?????00000????????????????")
  val BLTZ  = BitPat("b000001?????00000????????????????")
  val BLTZAL= BitPat("b000001?????10000????????????????")
  val BGEZAL= BitPat("b000001?????10001????????????????")
  val J     = BitPat("b000010??????????????????????????")
  val JAL   = BitPat("b000011??????????????????????????")
  val JR    = BitPat("b000000?????000000000000000001000")
  val JALR  = BitPat("b000000???????????????00000001001")

  val LW    = BitPat("b100011??????????????????????????")
  val LH    = BitPat("b100001??????????????????????????")
  val LHU   = BitPat("b100101??????????????????????????")
  val LB    = BitPat("b100000??????????????????????????")
  val LBU   = BitPat("b100100??????????????????????????")
  val LWL   = BitPat("b100010??????????????????????????")
  val LWR   = BitPat("b100110??????????????????????????")
  val SW    = BitPat("b101011??????????????????????????")
  val SH    = BitPat("b101001??????????????????????????")
  val SB    = BitPat("b101000??????????????????????????")
  val SWL   = BitPat("b101010??????????????????????????")
  val SWR   = BitPat("b101110??????????????????????????")

  val MFHI  = BitPat("b0000000000000000?????00000010000")
  val MFLO  = BitPat("b0000000000000000?????00000010010")
  val MTHI  = BitPat("b000000?????000000000000000010001")
  val MTLO  = BitPat("b000000?????000000000000000010011")
  val MUL   = BitPat("b011100???????????????00000000010")
  val MULT  = BitPat("b000000??????????0000000000011000")
  val MULTU = BitPat("b000000??????????0000000000011001")
  val DIV   = BitPat("b000000??????????0000000000011010")
  val DIVU  = BitPat("b000000??????????0000000000011011")
  val MADD  = BitPat("b011100??????????0000000000000000")
  val MADDU = BitPat("b011100??????????0000000000000001")
  val MSUB  = BitPat("b011100??????????0000000000000100")
  val MSUBU = BitPat("b011100??????????0000000000000101")

  val MFC0 = BitPat("b01000000000??????????00000000???")
  val MTC0 = BitPat("b01000000100??????????00000000???")
  val ERET = BitPat("b01000010000000000000000000011000")
}

object consts extends InstrPattern
  with MemConsts
  with CP0Consts
  with InstrConsts
  with CacheConsts
  with ExecUnitIndices
  with DecodeSelectors
{
  val Y = true.B
  val N = false.B
}
