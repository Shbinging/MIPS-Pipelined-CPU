package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import njumips.configs._

// class ProgramCounter extends Module {
//     val io = IO(new Bundle{
//         val in = new PCInput
//         val out = Output(UInt(conf.addr_width.W))
//     })
//     val pc = RegEnable(io.in.w_data, io.in.w_en)

//     withReset(reset.asBool()){
//         pc := conf.start_addr.asUInt()
//     }

//     io.out := pc 
// }

class GPR extends Module {
    val io = IO(new Bundle{
        val read_in = new GPRReadIntput
        val write_in = new GPRWriteInput
        val read_out = new GPRReadOutput
        val gpr_commit = Output(Vec(32, UInt(32.W)))
    })
    // TODO: support variable lengths configs
    val gprs = RegInit(VecInit(Seq.fill(32){VecInit(Seq.fill(4){0.U(8.W)})}))
    val time = RegInit(0.U(32.W))
    time := time + 1.U
    io.read_out.rs_data := gprs(io.read_in.rs_addr).asUInt()
    io.read_out.rt_data := gprs(io.read_in.rt_addr).asUInt()
   
    when(io.write_in.w_en.asUInt() =/= 0.U & io.write_in.addr =/= 0.U){
        //printf(p"${time}: Write to regs: ${io.write_in.addr}, ${io.write_in.data}, ${io.write_in.w_en}\n")
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
    //printf("readin %x %x\n", io.read_in.rs_addr, io.read_in.rt_addr)
    for( i <- 0 to 31){
        io.gpr_commit(i) := gprs(i).asUInt()
    }
}