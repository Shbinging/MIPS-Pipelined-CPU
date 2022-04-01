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
    // when (isu_alu_fire){
    // // printf("=====EXEC=====\n")
    // printf(p"${io.isu_alu}\n")
    // //printf(p"${io.out}\n")
    // printf(p"${io.exec_wb}\n")
    // printf("==========\n")
    // }
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
        bruwb.w_en := true.B
        bruwb.w_addr := Mux(r.bru_op===BRU_JALR_OP, r.rd, 31.U)
        bruwb.w_data := r.pcNext + 4.U
    }
//bruwb.w_pc_addr := 
    io.exec_wb.valid := isu_bur_fire

    io.exec_wb.bits <> bruwb
}

class LSU extends Module{
    val io = IO{new Bundle{
        val isu_lsu = Flipped(Decoupled(new ISU_LSU))
        val exec_wb = Decoupled(new LSU_WB)
    }}
    io.exec_wb.bits := DontCare
    io.isu_lsu.ready := true.B
    val isu_lsu_fire = Reg(Bool())
    val state_reg = Reg(UInt(STATUS_WIDTH.W))
    when(reset.asBool()){
        isu_lsu_fire := false.B
        state_reg := LSU_DIE
    }

    when(io.isu_lsu.fire()){
        isu_lsu_fire := true.B
        state_reg := LSU_DECODE
    }

    val r = RegEnable(io.isu_lsu.bits, io.isu_lsu.fire())
	val back_reg = Reg(new Bundle{
		val w_en = UInt(4.W)
    	val w_addr = UInt(REG_SZ.W)
		val w_data = UInt(conf.data_width.W)
		})
	val read_reg = Reg(new Bundle{
		val addr = UInt(conf.data_width.W)
		val len = UInt(2.W)
		val en = Bool()
	})
	val exec_reg = Reg(new Bundle{
		val preRead = Bool()
		val func = UInt(4.W)
		val preReadData = UInt(conf.data_width.W)//need calc
	})
	val write_reg = Reg(new Bundle{
		val addr = UInt(conf.data_width.W)
		val strb = UInt(4.W)
		val en = Bool()
		val w_data = UInt(conf.data_width.W)
	})
	val dev = Module(new SimDev)
	dev.io.clock := clock
    dev.io.reset := reset.asBool() 
    dev.io.in.req.bits.is_cached := DontCare
    dev.io.in.req.bits.data := DontCare
    dev.io.in.req.valid := false.B
    dev.io.in.resp.ready := false.B
	io.exec_wb.valid := false.B
    //printf("state %d valid %d\n", state_reg, io.exec_wb.valid)
	switch(state_reg){
		is(LSU_DIE){
			io.exec_wb.valid:=false.B
		}
		is(LSU_DECODE){
			val vAddr = Wire(UInt(32.W))
			vAddr := (r.imm.asTypeOf(SInt(32.W)) + r.rsData.asSInt()).asUInt()
            val offset = Wire(UInt(2.W))
            offset := vAddr(1, 0)
            //printf("%x %x %x\n", r.imm, r.rsData, vAddr)
			val rt = Wire(UInt(5.W))
			rt := r.rt
			val decoded_instr = ListLookup(r.lsu_op, List("b1111".U(4.W), rt, vAddr, 0.U, true.B, true.B, LSU_FUNC_B, DontCare, DontCare, false.B), Array(
					//read
					BitPat(LSU_LB_OP)->List("b1111".U(4.W), rt, vAddr, 0.U, true.B, true.B, LSU_FUNC_B, DontCare, DontCare, false.B),
					BitPat(LSU_LBU_OP)->List("b1111".U(4.W), rt, vAddr, 0.U, true.B, true.B, LSU_FUNC_BU, DontCare, DontCare, false.B),
					BitPat(LSU_LH_OP)->List("b1111".U(4.W), rt, vAddr, 1.U, true.B, true.B, LSU_FUNC_H, DontCare, DontCare, false.B),
					BitPat(LSU_LHU_OP)->List("b1111".U(4.W), rt, vAddr, 1.U, true.B, true.B, LSU_FUNC_HU, DontCare, DontCare, false.B),
					BitPat(LSU_LW_OP)->List("b1111".U(4.W), rt, vAddr, 3.U, true.B, true.B, LSU_FUNC_WU, DontCare, DontCare, false.B),
					BitPat(LSU_LWL_OP)->List("b1111".U(4.W), rt, vAddr, 3.U, true.B, true.B, LSU_FUNC_RWL, DontCare, DontCare, false.B),
					BitPat(LSU_LWR_OP)->List("b1111".U(4.W), rt, vAddr, 3.U, true.B, true.B, LSU_FUNC_RWR, DontCare, DontCare, false.B),
					//write
					BitPat(LSU_SB_OP)->List(0.U(4.W), DontCare, DontCare, DontCare, false.B, false.B, DontCare, vAddr, "b1".U << offset, true.B),
					BitPat(LSU_SH_OP)->List(0.U(4.W), DontCare, DontCare, DontCare, false.B, false.B, DontCare, vAddr, "b11".U << offset, true.B),
					BitPat(LSU_SW_OP)->List(0.U(4.W), DontCare, DontCare, DontCare, false.B, false.B, DontCare, vAddr, "b1111".U, true.B),
					BitPat(LSU_SWL_OP)->List(0.U(4.W), DontCare, vAddr, 3.U, true.B, true.B, LSU_FUNC_WWL, vAddr, "b1111".U, true.B),
					BitPat(LSU_SWR_OP)->List(0.U(4.W), DontCare, vAddr, 3.U, true.B, true.B, LSU_FUNC_WWR, vAddr, "b1111".U, true.B),
				)
			)
			back_reg.w_en := decoded_instr(0)
			back_reg.w_addr := decoded_instr(1)
			read_reg.addr := decoded_instr(2)
			read_reg.len := decoded_instr(3)
			read_reg.en := decoded_instr(4)
			exec_reg.preRead := decoded_instr(5)
			exec_reg.func := decoded_instr(6)
			write_reg.addr := decoded_instr(7)
			write_reg.strb := decoded_instr(8)
			write_reg.en := decoded_instr(9)
			state_reg := LSU_READ
            // when ((vAddr & 3.U) =/= 0.U){//address exception
            //     io.exec_wb.valid := true.B
			//     io.exec_wb.bits.w_addr := DontCare
			//     io.exec_wb.bits.w_en := 0.U
			//     io.exec_wb.bits.w_data := DontCare
			//     state_reg := LSU_DIE
            // }
		}
		is(LSU_READ){
			when(!read_reg.en){
				state_reg := LSU_CALC
			}.otherwise{
				dev.io.in.req.valid := true.B
				dev.io.in.req.bits.addr :=read_reg.addr & (~3.U(32.W))
				dev.io.in.req.bits.func := MX_RD
				dev.io.in.req.bits.len := 3.U
				dev.io.in.resp.ready := true.B
			}
			when(dev.io.in.resp.fire()){
				state_reg := LSU_CALC
				exec_reg.preReadData := dev.io.in.resp.bits.data >> (read_reg.addr(1, 0) << 3)
				dev.io.in.req.valid := false.B
			}
		}
		is (LSU_CALC){
			when(exec_reg.preRead){
				val shiftMask1 = VecInit(0x00ffffff.U, 0x0000ffff.U, 0x000000ff.U, 0x0.U)
				val shiftMask2 = VecInit(0x0.U, 0xff000000L.U, 0xffff0000L.U, 0xffffff00L.U)

				val index = WireInit(write_reg.addr(1, 0).asUInt())
				write_reg.w_data := Mux1H(Seq(
					(exec_reg.func === LSU_FUNC_B)->(exec_reg.preReadData(7, 0).asTypeOf(SInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_BU)->(exec_reg.preReadData(7, 0).asTypeOf(UInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_H)->(exec_reg.preReadData(15, 0).asTypeOf(SInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_HU)->(exec_reg.preReadData(15, 0).asTypeOf(UInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_W)->(exec_reg.preReadData.asTypeOf(SInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_WU)->(exec_reg.preReadData.asTypeOf(UInt(32.W)).asUInt()),
					(exec_reg.func === LSU_FUNC_RWL)->((exec_reg.preReadData << ((~index) << 3)) | ((r.rtData) & shiftMask1(index))).asTypeOf(UInt(32.W)),
					(exec_reg.func === LSU_FUNC_RWR)->((exec_reg.preReadData >> (index << 3)) | (r.rtData & shiftMask2(index))).asTypeOf(UInt(32.W)),
					(exec_reg.func === LSU_FUNC_WWL) ->((r.rtData >> ((~index) << 3)) | (exec_reg.preReadData & shiftMask2(~index))).asTypeOf(UInt(32.W)),
					(exec_reg.func === LSU_FUNC_WWR) ->((r.rtData << (index << 3)) | (exec_reg.preReadData & shiftMask1(~index))).asTypeOf(UInt(32.W))
				)
				).asTypeOf(UInt(32.W))
			}.otherwise{
				write_reg.w_data := r.rtData
			}
			state_reg := LSU_WRITE
		}
		is (LSU_WRITE){
			when(!write_reg.en){
				back_reg.w_data := write_reg.w_data
				state_reg := LSU_BACK
			}.otherwise{
                dev.io.in.req.bits.data := write_reg.w_data << (write_reg.addr(1, 0) << 3.U)
				dev.io.in.req.valid := true.B
				dev.io.in.req.bits.addr := write_reg.addr & (~3.U(32.W))
                //printf("%x\n", dev.io.in.req.bits.addr)
				dev.io.in.req.bits.func := MX_WR
				dev.io.in.req.bits.strb := write_reg.strb
                //printf("strb %x\n", write_reg.strb)
				dev.io.in.resp.ready := true.B
				back_reg.w_data := DontCare
			}
			when(dev.io.in.resp.fire()){
				state_reg := LSU_BACK
				dev.io.in.req.valid := false.B
			}
		}
		is (LSU_BACK){
			io.exec_wb.valid := true.B
			io.exec_wb.bits.w_addr := back_reg.w_addr
			io.exec_wb.bits.w_en := back_reg.w_en
			io.exec_wb.bits.w_data := back_reg.w_data
			state_reg := LSU_DIE
		}
	}
}


    // val vAddr = WireInit((r.imm.asSInt() + r.rsData.asSInt()).asUInt())
    // val shiftPos = VecInit(0.U, 8.U, 16.U, 24.U, 32.U)
    // val index = vAddr(1, 0).asUInt()
    // dev.io.in.req.bits.addr := vAddr
    // val resp_fire = RegNext(dev.io.in.resp.fire().asBool())
    // val resp_data_reg = RegEnable(dev.io.in.resp.bits, dev.io.in.resp.fire())
    // when(VecInit(LSU_LBU_OP, LSU_LB_OP, LSU_LH_OP, LSU_LWL_OP, LSU_LWR_OP, LSU_LW_OP).contains(r.lsu_op)){
    //     val decoded_instr = ListLookup(r.lsu_op, List(MX_RD, 0.U), Array(
    //         BitPat(LSU_LBU_OP)->List(MX_RD, 0.U),
    //         BitPat(LSU_LB_OP)->List(MX_RD, 0.U),
    //         BitPat(LSU_LH_OP)->List(MX_RD, 1.U),
    //         BitPat(LSU_LWL_OP)->List(MX_RD, 3.U),
    //         BitPat(LSU_LWR_OP)->List(MX_RD, 3.U),
    //         BitPat(LSU_LW_OP)->List(MX_RD, 3.U),
    //     ))
    //     dev.io.in.req.bits.func := decoded_instr(0)
    //     dev.io.in.req.bits.len := decoded_instr(1)
    //     // when(resp_fire){

    //     // }
    // //     when(resp_fire){
    // //         io.exec_wb.bits.w_en := "b1111".U
    // //         io.exec_wb.bits.w_addr := r.rt
    // //         val shiftMask1 = VecInit("0xffffff".U, "0xffff".U, "0xff".U, "0x0".U)
    // //         io.exec_wb.bits.w_data := MuxLookup(r.lsu_op, 0.U, Seq(
    // //             LSU_LB_OP->((resp_data_reg.data & 0xff.U).asTypeOf(SInt(32.W))).asUInt(),
    // //             LSU_LBU_OP->(resp_data_reg.data & 0xff.U).asUInt(),
    // //             LSU_LH_OP -> ((resp_data_reg.data &0xffff.U).asTypeOf(SInt(32.W))).asUInt(),
    // //             LSU_LHU_OP -> (resp_data_reg.data &0xffff.U).asUInt(),
    // //             LSU_LW_OP -> ((resp_data_reg.data).asTypeOf(SInt(32.W))).asUInt(),
    // //             LSU_LWL_OP -> (((resp_data_reg.data) << (~index << 3)) | (shiftMask1(index) & r.rt)),
    // //             LSU_LWR_OP -> ((resp_data_reg.data) >> (index << 3)),
    // //             ).asTypeOf(UInt(32.W))
    // //         when(r.lsu_op === LSU_LWR_OP){
    // //             val shiftMask2 = VecInit("b1111".U, "b0111".U, "b0011".U, "b0001".U)
    // //             io.exec_wb.bits.w_en := shiftMask2(index)
    // //         }   
    // //     }
    // }.otherwise{
    //         dev.io.in.req.bits.func := MX_WR
    //         dev.io.in.req.bits.data := MuxLookup(r.lsu_op, 0.U,Array(
    //             LSU_SB_OP -> r.rtData(7, 0),
    //             LSU_SH_OP -> r.rtData(15, 0),
    //             LSU_SW_OP -> r.rtData,
    //             LSU_SWL_OP -> (r.rtData >> shiftPos(3.U - index)),
    //             LSU_SWR_OP -> (r.rsData >> shiftPos(index))
    //         ))
    //         dev.io.in.req.bits.len := MuxLookup(r.lsu_op, 0.U, Array(
    //             LSU_SB_OP -> 0.U,
    //             LSU_SH_OP -> 1.U,
    //             LSU_SW_OP -> 3.U,
    //             LSU_SWL_OP -> 3.U,
    //             LSU_SWR_OP -> 3.U
    //         ))
    //         when(r.lsu_op === LSU_SWL_OP){
    //             dev.io.in.req.bits.len := index
    //         }
    //         when(r.lsu_op === LSU_SWR_OP){
    //             dev.io.in.req.bits.len := 3.U - index
    //             dev.io.in.req.bits.addr := vAddr + index
    //         }
    //         io.exec_wb.bits.w_en := 0.U
    
    // }
    // when(resp_fire){
    //     isu_lsu_fire := 0.U
    // }
    // io.exec_wb.valid := resp_fire & ~reset.asBool()


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
    val isu_mdu_fired = RegEnable(io.isu_mdu.fire(), false.B, io.isu_mdu.fire())
    val isu_mdu_reg = RegEnable(io.isu_mdu.bits, io.isu_mdu.fire())
    
    val mdu_wb_valid = RegInit(false.B)
    val mdu_wb_reg = Reg(new MDU_WB)

    val hi = RegInit(0.U(32.W))
    val lo = RegInit(0.U(32.W))
    
    val multiplier_delay_count = RegInit((conf.mul_stages).U(3.W))    // 8


    when(isu_mdu_fired){
        // data_dividend_valid, data_divisor_valid, data_divident_bits, data_divisor_bits
        dividor.io.data_dividend_valid := VecInit(MDU_DIV_OP, MDU_DIVU_OP).contains(isu_mdu_reg.mdu_op)
        dividor.io.data_divisor_valid := VecInit(MDU_DIV_OP, MDU_DIVU_OP).contains(isu_mdu_reg.mdu_op)
        // DIV or DIVU
        dividor.io.data_dividend_bits := Cat(
            Mux(isu_mdu_reg.mdu_op===MDU_DIV_OP, Fill(8, isu_mdu_reg.rsData(31)), 0.U(8.W)),
            isu_mdu_reg.rsData
        )
        dividor.io.data_divisor_bits := Cat(
            Mux(isu_mdu_reg.mdu_op===MDU_DIV_OP, Fill(8, isu_mdu_reg.rtData(31)), 0.U(8.W)),
            isu_mdu_reg.rtData
        )        

        multiplier_delay_count := Mux(
            VecInit(MDU_MUL_OP, MDU_MULT_OP, MDU_MULTU_OP, MDU_MADD_OP, MDU_MADDU_OP, MDU_MSUB_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op),
            (conf.mul_stages).U(3.W), (conf.mul_stages-1).U(3.W)
        )
        multiplier.io.data_a := Cat(
            Mux(VecInit(MDU_MULTU_OP, MDU_MADDU_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op), isu_mdu_reg.rsData(31), 0.U(1.W)),
            isu_mdu_reg.rsData
        )
        multiplier.io.data_b := Cat(
            Mux(VecInit(MDU_MULTU_OP, MDU_MADDU_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op), isu_mdu_reg.rtData(31), 0.U(1.W)),
            isu_mdu_reg.rtData
        )
        
        mdu_wb_reg.w_en := VecInit(MDU_MFHI_OP, MDU_MFLO_OP).contains(isu_mdu_reg.mdu_op)
        mdu_wb_reg.w_addr := isu_mdu_reg.rd 
        mdu_wb_reg.w_data := Mux(isu_mdu_reg.mdu_op===MDU_MFHI_OP, hi, lo)

        hi := Mux(isu_mdu_reg.mdu_op===MDU_MTHI_OP, isu_mdu_reg.rsData, hi)
        lo := Mux(isu_mdu_reg.mdu_op===MDU_MTLO_OP, isu_mdu_reg.rsData, lo)

        when(VecInit(MDU_MTHI_OP, MDU_MTLO_OP, MDU_MFHI_OP, MDU_MFLO_OP).contains(isu_mdu_reg.mdu_op)){
            mdu_wb_valid := true.B
        } .otherwise{
            mdu_wb_valid := false.B
        }
        isu_mdu_fired := false.B
        printf(p"ISU_MDU_FIRED! with ${isu_mdu_reg.mdu_op}\n")
    } .otherwise{
        dividor.io <> DontCare
        dividor.io.data_dividend_valid := false.B
        dividor.io.data_divisor_valid := false.B
        multiplier.io <> DontCare
    }

    when(multiplier_delay_count === 0.U(3.W)){  // Multiplier OK
        multiplier_delay_count := conf.mul_stages.U(3.W)
        mdu_wb_reg.w_en := Mux(isu_mdu_reg.mdu_op===MDU_MUL_OP, true.B, false.B)
        mdu_wb_valid := true.B
        when(VecInit(MDU_MADD_OP, MDU_MADDU_OP).contains(isu_mdu_reg.mdu_op)){
            hi := hi + multiplier.io.data_dout(63, 32)
            lo := lo + multiplier.io.data_dout(31, 0)
        } .elsewhen(VecInit(MDU_MSUB_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op)){
            hi := hi - multiplier.io.data_dout(63, 32)
            lo := lo - multiplier.io.data_dout(31, 0)
        } .elsewhen(isu_mdu_reg.mdu_op===MDU_MUL_OP){
            mdu_wb_reg.w_addr := isu_mdu_reg.rd
            mdu_wb_reg.w_data := multiplier.io.data_dout(31, 0)
        }.otherwise{
            hi := multiplier.io.data_dout(63, 32)
            lo := multiplier.io.data_dout(31, 0)
        }
    }
    when(dividor.io.data_dout_valid){
        mdu_wb_reg.w_en := false.B 
        mdu_wb_valid := true.B
        hi := dividor.io.data_dout_bits(71, 40)
        lo := dividor.io.data_dout_bits(31, 0)  
        printf(p"DIV: ${dividor.io.data_dout_bits(71, 40)} - ${ dividor.io.data_dout_bits(31, 0)}\n")
    }

    when(multiplier_delay_count =/= conf.mul_stages.U(3.W)){
        multiplier_delay_count := multiplier_delay_count - 1.U
    }


    io.exec_wb.valid := mdu_wb_valid
    io.exec_wb.bits <> mdu_wb_reg
    when(io.exec_wb.fire()){
        mdu_wb_valid := false.B
    }
    //printf(p"fire to wb: ${io.exec_wb.fire()}")
}
