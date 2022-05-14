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
        val cp0_write_in = new CP0WriteInput
        val cp0_entryhi = Output(new EntryHi)
        val cp0_status = Output(new cp0_Status_12)
        val cp0_cause = Output(new cp0_Cause_13)
    })
    // TODO: support variable lengths configs

    //regs(0-31) gpr regs(32-63) cp0
    val regs = RegInit(VecInit(Seq.fill(64){VecInit(Seq.fill(4){0.U(8.W)})}))
    val time = RegInit(0.U(32.W))
    time := time + 1.U
    io.read_out.rs_data := regs(io.read_in.rs_addr).asUInt()
    io.read_out.rt_data := regs(io.read_in.rt_addr).asUInt()

    when(io.write_in.w_en.asUInt() =/= 0.U && io.write_in.addr =/= 0.U){
        //printf(p"${time}: Write to regs: ${io.write_in.addr}, ${io.write_in.data}, ${io.write_in.w_en}\n")
        when(io.write_in.w_en(0)){
            regs(io.write_in.addr)(0) := io.write_in.data(7, 0)
        }
        when(io.write_in.w_en(1)){
            regs(io.write_in.addr)(1) := io.write_in.data(15, 8)
        }
        when(io.write_in.w_en(2)){
            regs(io.write_in.addr)(2) := io.write_in.data(23, 16)
        }
        when(io.write_in.w_en(3)){
            regs(io.write_in.addr)(3) := io.write_in.data(31, 24)
        }
    }
    //printf("readin %x %x\n", io.read_in.rs_addr, io.read_in.rt_addr)
    for( i <- 0 to 31){
        io.gpr_commit(i) := regs(i).asUInt()
    }
    when(io.cp0_write_in.enableEXL || io.cp0_write_in.enableOther || io.cp0_write_in.enableVaddress){
        val newCause = WireInit(regs(32.U + 13.U).asTypeOf(new cp0_Cause_13))
        val newStatus = WireInit(regs(32.U + 12.U).asTypeOf(new cp0_Status_12))
        when(io.cp0_write_in.enableEXL){
            newStatus.EXL := io.cp0_write_in.EXL
        }
        when(io.cp0_write_in.enableVaddress){
            regs(32.U + 8.U)(0) := io.cp0_write_in.vAddr(7, 0)
            regs(32.U + 8.U)(1) := io.cp0_write_in.vAddr(15, 8)
            regs(32.U + 8.U)(2) := io.cp0_write_in.vAddr(23, 16)
            regs(32.U + 8.U)(3) := io.cp0_write_in.vAddr(31, 24)
        }
        when(io.cp0_write_in.enableOther){
            newCause.ExcCode := io.cp0_write_in.ExcCode
            newCause.BD := io.cp0_write_in.BD
            regs(32.U + 12.U)(0) := newStatus.asUInt()(7, 0)
            regs(32.U + 12.U)(1) := newStatus.asUInt()(15, 8)
            regs(32.U + 12.U)(2) := newStatus.asUInt()(23, 16)
            regs(32.U + 12.U)(3) := newStatus.asUInt()(31, 24)
            regs(32.U + 13.U)(0) := newCause.asUInt()(7, 0)
            regs(32.U + 13.U)(1) := newCause.asUInt()(15, 8)
            regs(32.U + 13.U)(2) := newCause.asUInt()(23, 16)
            regs(32.U + 13.U)(3) := newCause.asUInt()(31, 24)
            regs(32.U + 14.U)(0) := io.cp0_write_in.epc(7, 0)
            regs(32.U + 14.U)(1) := io.cp0_write_in.epc(15, 8)
            regs(32.U + 14.U)(2) := io.cp0_write_in.epc(23, 16)
            regs(32.U + 14.U)(3) := io.cp0_write_in.epc(31, 24)
            printf("status %x\n", newStatus.asUInt())
            printf("cause %x\n", newCause.asUInt())
        }        
    }
    io.cp0_status := regs(32.U + 12.U).asTypeOf(new cp0_Status_12)
    io.cp0_cause := regs(cp0_cause).asTypeOf(new cp0_Cause_13)
    io.cp0_entryhi := regs(32.U + 10.U).asTypeOf(new EntryHi)
    //printf("GPR[8] t0 = %x\n", regs(8).asUInt())
}

