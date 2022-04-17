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
    val isu_alu_prepared = RegNext(false.B)
    val r = RegEnable(io.isu_alu.bits, io.isu_alu.fire())
    io.isu_alu.ready := io.exec_wb.fire() || !isu_alu_prepared
    
    val A_in = WireInit(r.operand_1)
    val B_in = WireInit(r.operand_2)
    val ALU_op = WireInit(r.alu_op)
    val ALU_out = Wire(UInt(32.W))  
    
    // for ins and ext
    val msb = r.imm(15, 11).asUInt
    val lsb = r.imm(10, 6).asUInt
    val a_mask = (("h_ffff_ffff".U(32.W))>>(31.U(5.W)-msb+lsb)).asUInt()(31, 0)
    val b_mask = (~((a_mask<<lsb))).asUInt()(31, 0)

    // for rot
    val rot = Cat(B_in, 0.U(32.W)) >> A_in

    // for clz clo
    val A_in_Count = Mux(r.alu_op===ALU_CLO_OP, ~A_in, A_in)
    val tocount = Wire(Vec(32, Bool()))
    val count = Wire(Vec(32, UInt(6.W)))
    for(i <- 31 to 0 by -1){
        if(i==31) tocount(i) := (A_in_Count(i)===0.U)
        else tocount(i) := tocount(i+1) & (A_in_Count(i)===0.U)
    }
    for(i <- 31 to 0 by -1){
        if(i==31) count(i) := Mux(tocount(i), 1.U, 0.U)
        else count(i) := Mux(tocount(i), count(i+1)+1.U, count(i+1))
    }

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
        (r.alu_op === ALU_ROTR_OP) -> (rot(63, 32) + rot(31, 0)),
        (r.alu_op === ALU_SUBU_OP) -> (A_in - B_in),
        (r.alu_op === ALU_SUB_OP) -> (A_in.asSInt() - B_in.asSInt()).asUInt(),
        (r.alu_op === ALU_XOR_OP) -> (A_in ^ B_in),
        (r.alu_op === ALU_SEB_OP) -> Cat(Fill(24, B_in(7)), B_in(7, 0)),
        (r.alu_op === ALU_SEH_OP) -> Cat(Fill(16, B_in(15)), B_in(15, 0)),
        (r.alu_op === ALU_WSBH_OP)-> (Cat(Cat(B_in(23, 16), B_in(31, 24)), Cat(B_in(7, 0), B_in(15, 8)))),
        (r.alu_op === ALU_INS_OP) -> (((A_in&a_mask)<<lsb).asUInt()(31, 0) + (B_in&b_mask)),
        (r.alu_op === ALU_EXT_OP) -> ((A_in>>lsb) & ("h_ffff_ffff".U(32.W)>>(31.U - msb))),
        (r.alu_op === ALU_CLO_OP) -> count(0),
        (r.alu_op === ALU_CLZ_OP) -> count(0)
    ))(31, 0)
    //io.out.ALU_out := ALU_out
    //io.out.Overflow_out := false.B //XXX:modify when need exception
    //io.exec_wb.bits.
    io.exec_wb.bits.ALU_out := ALU_out
    io.exec_wb.bits.Overflow_out := false.B //XXX:modify when need exception
    io.exec_wb.bits.w_addr := r.rd_addr // io.isu_alu.bits.rd_addr
    io.exec_wb.bits.w_en := isu_alu_prepared    // XXX:
    io.exec_wb.bits.current_pc := r.current_pc
    io.exec_wb.bits.current_instr := r.current_instr
    io.exec_wb.valid := isu_alu_prepared  // 1 cycle 
    //printf(p"alu: ${r} \n- ${A_in} ${B_in}\n")
    when ((!io.isu_alu.fire() && io.exec_wb.fire())) {
        isu_alu_prepared := N
    } .elsewhen (io.isu_alu.fire()) {
        isu_alu_prepared := Y
    }
}

class BRU extends Module{
    val io = IO{new Bundle{
        val isu_bru = Flipped(Decoupled(new ISU_BRU))
        val exec_wb = Decoupled(new BRU_WB)
    }}    
    val isu_bur_fire = RegNext(false.B)
    val r = RegEnableUse(io.isu_bru.bits, io.isu_bru.fire())
    io.isu_bru.ready := io.exec_wb.fire() || !isu_bur_fire

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

    when ((!io.isu_bru.fire() && io.exec_wb.fire())) {
        isu_bur_fire := N
    } .elsewhen (io.isu_bru.fire()) {
        isu_bur_fire := Y
    }
    bruwb.current_pc := r.current_pc
    bruwb.current_instr := r.current_instr
    io.exec_wb.bits <> bruwb
    io.exec_wb.valid := isu_bur_fire
}

class LSU extends Module{
    val io = IO{new Bundle{
        val isu_lsu = Flipped(Decoupled(new ISU_LSU))
        val exec_wb = Decoupled(new LSU_WB)
    }}
    io.exec_wb.bits <> DontCare // FIXME
    //printf("io.exec_wb.valid %d io.isu_lsu.ready %d\n", io.exec_wb.valid, io.isu_lsu.ready)
    io.exec_wb.valid:=false.B
    val isu_lsu_fire = RegInit(false.B)
    io.isu_lsu.ready := io.exec_wb.fire() || !isu_lsu_fire
    val r = RegEnable(io.isu_lsu.bits, io.isu_lsu.fire())
    val state_reg = RegInit(LSU_DIE)

    when ((!io.isu_lsu.fire() && io.exec_wb.fire())) {
        isu_lsu_fire := N
    } .elsewhen (io.isu_lsu.fire()) {
        isu_lsu_fire := Y
        state_reg := LSU_DECODE
    }

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
            //printf("lsu ok\n");
			io.exec_wb.valid := true.B
			io.exec_wb.bits.w_addr := back_reg.w_addr
			io.exec_wb.bits.w_en := back_reg.w_en
			io.exec_wb.bits.w_data := back_reg.w_data
			state_reg := LSU_DIE
		}
	}

    io.exec_wb.bits.current_pc := r.current_pc
    io.exec_wb.bits.current_instr := r.current_instr
}


class Divider extends Module {
    val io = IO(Flipped(new DividerIO))
    val dividend = (io.data_dividend_bits).asSInt
    val divisor = (io.data_divisor_bits).asSInt
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
    
    val isu_mdu_fired = RegInit(false.B)
    val isu_mdu_reg = RegEnable(io.isu_mdu.bits, io.isu_mdu.fire())
    io.isu_mdu.ready := io.exec_wb.fire() || !isu_mdu_fired
    //printf("io.isu_mdu.ready %d isu_mdu_fired %d \n", io.isu_mdu.ready, isu_mdu_fired);
    when ((!io.isu_mdu.fire() && io.exec_wb.fire())) {
        isu_mdu_fired:= false.B
    } .elsewhen (io.isu_mdu.fire()) {
        //printf("mdu is working!\n");
        isu_mdu_fired := true.B
    }

    val mdu_wb_valid = RegInit(false.B)
    val mdu_wb_reg = Reg(new MDU_WB)

    val hi = RegInit(0.U(32.W))
    val lo = RegInit(0.U(32.W))
    
    val multiplier_delay_count = RegInit((conf.mul_stages).U(3.W))    // 7


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
        when(dividor.io.data_dividend_valid & dividor.io.data_divisor_valid){
            //printf(p"${dividor.io.data_dividend_bits} / ${dividor.io.data_divisor_bits}\n")
        }

        val is_mul = VecInit(MDU_MUL_OP, MDU_MULT_OP, MDU_MULTU_OP, MDU_MADD_OP, MDU_MADDU_OP, MDU_MSUB_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op)
        multiplier_delay_count := Mux(is_mul && multiplier_delay_count==conf.mul_stages.U(3.W), (conf.mul_stages-1).U(3.W), (conf.mul_stages).U(3.W))
        multiplier.io.data_a := Cat(
            Mux(VecInit(MDU_MULTU_OP, MDU_MADDU_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op), 0.U(1.W), isu_mdu_reg.rsData(31)),
            isu_mdu_reg.rsData
        )
        multiplier.io.data_b := Cat(
            Mux(VecInit(MDU_MULTU_OP, MDU_MADDU_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op), 0.U(1.W), isu_mdu_reg.rtData(31)),
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
            val hi_lo = Cat(hi, lo) + multiplier.io.data_dout(63, 0)
            hi := hi_lo(63, 32)
            lo := hi_lo(31, 0)
        } .elsewhen(VecInit(MDU_MSUB_OP, MDU_MSUBU_OP).contains(isu_mdu_reg.mdu_op)){
            val hi_lo = Cat(hi, lo) - multiplier.io.data_dout(63, 0)
            hi := hi_lo(63, 32)
            lo := hi_lo(31, 0)
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
        lo := dividor.io.data_dout_bits(71, 40)
        hi := dividor.io.data_dout_bits(31, 0)  
    }

    when(multiplier_delay_count =/= conf.mul_stages.U(3.W)){
        multiplier_delay_count := multiplier_delay_count - 1.U
    }
    //printf("count:%d\n", multiplier_delay_count)
    mdu_wb_reg.current_pc := isu_mdu_reg.current_pc
    //printf("pc %x\n", isu_mdu_reg.current_pc)
    mdu_wb_reg.current_instr := isu_mdu_reg.current_instr
    io.exec_wb.valid := mdu_wb_valid
    io.exec_wb.bits <> mdu_wb_reg
    

    when(io.exec_wb.fire()){
        mdu_wb_valid := false.B
    }
}
