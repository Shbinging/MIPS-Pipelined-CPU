package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class adder extends Module{
    val io = IO(
        new Bundle{
            val A_in = Input(UInt(32.W))
            val B_in = Input(UInt(32.W))
            val Cin = Input(UInt(1.W))
            val Zero = Output(UInt(1.W))
            val Carry = Output(UInt(1.W))
            val Overflow = Output(UInt(1.W))
            val Negative = Output(UInt(1.W))
            val O_out = Output(UInt(32.W))
            override def toPrintable: Printable = {
                p"================\n"+
                p"${Cin.toString()} : $Cin\n"+
                p"Zero : $Zero\n"+
                p"Carry : $Carry\n"+
                p"Overflow : $Overflow\n"+
                p"Negative: $Negative\n"+
                p"O_out: ${Hexadecimal(O_out)}\n"+
                p"==================\n"
            }
        }
    )
    val res = WireInit(io.A_in +& io.B_in +& io.Cin)
    io.Carry := res(32)
    io.Zero := (res(31, 0) === 0.U)
    io.Overflow := ((~io.A_in(31)) & (~io.B_in(31)) & (res(31))) | ( io.A_in(31) & io.B_in(31) & (!res(31)))
    io.Negative := res(31)
    io.O_out := res(31, 0)
}
class barrelShifter extends Module{
    val io = IO(new Bundle{
        val shift_in = Input(UInt(32.W))
        val shift_amount = Input(UInt(5.W))
        val shift_op = Input(UInt(2.W))
        val shift_out = Output(UInt(32.W))
    })
    io.shift_out := 0.U
    switch(io.shift_op){
        is(0.U){
            io.shift_out := (io.shift_in << io.shift_amount)(31, 0)
        }
        is (1.U){
            io.shift_out := io.shift_in >> io.shift_amount
        }
        is (2.U){
            io.shift_out := (io.shift_in.asSInt() >> io.shift_amount).asUInt()
        }
        is (3.U){
            io.shift_out := (io.shift_in << (32.U - io.shift_amount)) | (io.shift_in >> io.shift_amount)
        }
    }
}

class AluPart extends Module{
    val io = IO(
        new Bundle{
            val A_in = Input(UInt(32.W))
            val B_in = Input(UInt(32.W))
            val ALU_op = Input(UInt(4.W))

            val ALU_out = Output(UInt(32.W))
            val Less = Output(UInt(1.W))
            val Overflow_out = Output(UInt(1.W))
            val Zero = Output(UInt(1.W))

            override def toPrintable: Printable = {
                p"================\n"+
                p"ALU_out : ${Hexadecimal(ALU_out)}\n"+
                p"Less : $Less\n"+
                p"Overflow_out : $Overflow_out\n"+
                p"Zero : $Zero\n"+
                p"==================\n"
            }
        }
    )

    val ALU_ctr = Wire(Vec(3, UInt(1.W)))
    val op3 = WireInit(io.ALU_op(3).asUInt())
    val op2 = WireInit(io.ALU_op(2).asUInt())
    val op1 = WireInit(io.ALU_op(1).asUInt())
    val op0 = WireInit(io.ALU_op(0).asUInt())
    ALU_ctr(2) := ~op3 & ~op1 | ~op3 & op2 & op0 | op3 & op1 
    ALU_ctr(1) := ~op3 & ~op2 & ~op1 | op3 & ~op2 & ~op0 | op2 & op1 & ~op0|op3 & op1
    ALU_ctr(0) := ~op2 & ~op1 | ~op3 & op2 & op0 | op3 & op2 & op1
    
    val Adder = Module(new adder)
    Adder.io.A_in := io.A_in
    Adder.io.B_in := io.B_in ^ Fill(32, io.ALU_op(0))
    Adder.io.Cin := io.ALU_op(0)

    when(io.ALU_op === "b0111".U){
        io.Less := ~Adder.io.Carry
    }.otherwise{
        io.Less := Adder.io.Overflow ^ Adder.io.Negative
    }

    when(io.ALU_op(3,1) === "b111".U){
        io.Overflow_out := Adder.io.Overflow
    }.otherwise{
        io.Overflow_out := 0.U
    }

    io.Zero := Adder.io.Zero
    
    io.ALU_out := 0.U
    switch(ALU_ctr.asUInt()){
        is(0.U){
            val vecc = VecInit((io.A_in ^ Fill(32, io.ALU_op(0))).asBools())
            io.ALU_out := 31.U - vecc.lastIndexWhere((c:Bool)=> c)
            //io.ALU_out:= 0.U
        }
        is(1.U){
            io.ALU_out := io.A_in ^ io.B_in
        }
        is(2.U){
            io.ALU_out := io.A_in | io.B_in
        }
        is(3.U){
            io.ALU_out := ~(io.A_in | io.B_in)
        }
        is(4.U){
            io.ALU_out := io.A_in & io.B_in
        }
        is(5.U){
            io.ALU_out := Mux(io.Less.asBool(), 1.U, 0.U)
            //io.ALU_out := 0.U
        }
        is(6.U){
            //io.ALU_out := 0.U
            io.ALU_out := Mux(io.ALU_op(0), Cat(Fill(16, io.B_in(15)), io.B_in(15, 0)), Cat(Fill(24, io.B_in(7)), io.B_in(7, 0)))
        }
        is(7.U){
            io.ALU_out := Adder.io.O_out
        }
    }
    //printf(p"${io.toPrintable}")
}

class ALU extends Module{
    val io = IO{new Bundle{
        val isu_alu = Flipped(Decoupled(new ISU_ALU))
        val out = new ALUOutput
        val exec_wb = Decoupled(new EXEC_WB)
    }}
    
    val isu_alu_fire = RegNext(io.isu_alu.fire())
    val r = RegEnableUse(io.isu_alu.bits, io.isu_alu.fire())
    val A_in = WireInit(r.operand_1)
    val B_in = WireInit(r.operand_2)
    val ALU_op = WireInit(r.alu_op)
    val ALU_out = Wire(UInt(32.W))
    val Less = Wire(UInt(1.W))
    val Overflow_out = Wire(UInt(1.W))
    val Zero = Wire(UInt(1.W))

    val alu = Module(new AluPart)
    val shift = Module(new barrelShifter)
    
    shift.io.shift_amount := A_in(4, 0)
    shift.io.shift_in := B_in
    shift.io.shift_op := DontCare
    switch(ALU_op){
        is("b1000".U){
            shift.io.shift_op := 0.U
        }
        is("b1010".U){
            shift.io.shift_op := 1.U
        }
        is("b1011".U){
            shift.io.shift_op := 2.U
        }
    }
    alu.io.ALU_op := ALU_op
    alu.io.A_in := A_in
    alu.io.B_in := B_in
    when (VecInit("b1000".U, "b1010".U, "b1011".U).contains(r.alu_op)){
        ALU_out := shift.io.shift_out
        Less := 0.U
        Overflow_out := 0.U
        Zero := Mux(shift.io.shift_out === 0.U, 1.U, 0.U)
    }.otherwise{
        ALU_out := alu.io.ALU_out
        Less := alu.io.Less
        Overflow_out := alu.io.Overflow_out
        Zero := alu.io.Zero
    }
    io.out := DontCare
    io.exec_wb := DontCare
    io.out.ALU_out := ALU_out
    io.out.Less := Less.asBool()
    io.out.Overflow_out := Overflow_out.asBool()
    io.out.Zero := Zero.asBool() //alu
    
    io.exec_wb.bits.w_addr := io.isu_alu.bits.rd_addr
    io.exec_wb.bits.w_en := true.B
    io.exec_wb.bits.exu_id := ALU_ID
    io.exec_wb.valid := isu_alu_fire  // 1 cycle 
}