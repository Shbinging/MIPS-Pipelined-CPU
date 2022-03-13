package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.utils._
import njumips.consts._
import njumips.configs._

class ProgramCounter extends Module {
    val io = IO(new Bundle{
        val in = new PCInput
        val out = Output(UInt(conf.addr_width.W))
    })

    val pc = RegEnable(io.in.w_data, io.in.w_en)

    withReset(reset.asBool()){
        pc := conf.start_addr.asUInt()
    }

    io.out := pc 
}

class GPR extends Module {
    val io = IO(new Bundle{
        val read_in = new GPRReadIntput
        val write_in = new GPRWriteInput
        val read_out = new GPRReadOutput
    })
    // TODO: support variable lengths configs
    val gprs = RegInit(VecInit(Seq.fill(4)(0.U(8.W))))

    val rs_gpr = gprs(io.read_in.rs_addr)
    val rt_gpr = gprs(io.read_in.rt_addr)

    io.read_out.rs_data := Cat(Cat(rs_gpr(3), rs_gpr(2)), Cat(rs_gpr(1), rs_gpr(0)))
    io.read_out.rt_data := Cat(Cat(rt_gpr(3), rt_gpr(2)), Cat(rt_gpr(1), rt_gpr(0)))
   
    when(io.write_in.w_en.asUInt() =/= 0.U & io.write_in.addr =/= 0.U){
        when(io.write_in.w_en(0)){
            gprs(io.write_in.addr)(0) := io.write_in.data(7, 0)
        }
        when(io.write_in.w_en(1)){
            gprs(io.write_in.addr)(1) := io.write_in.data(15, 8)
        }
        when(io.write_in.w_en(2)){
            gprs(io.write_in.addr)(2) := io.write_in.data(23, 16)
        }
        when(io.write_in.w_en(3)){
            gprs(io.write_in.addr)(3) := io.write_in.data(31, 24)
        }
    }

}