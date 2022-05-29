package njumips

import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._
import njumips.configs._
import chisel3.experimental.IO
import javax.management.MBeanRegistrationException

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
    // for( i<- 0 to 31){
    //     printf(p"reg ${i}: ${regs(i)}\n")
    // }
    // when(io.cp0_write_in.enableEXL || io.cp0_write_in.enableOther || io.cp0_write_in.enableVaddress){
    //     val newCause = WireInit(regs(32.U + 13.U).asTypeOf(new cp0_Cause_13))
    //     val newStatus = WireInit(regs(32.U + 12.U).asTypeOf(new cp0_Status_12))
    //     when(io.cp0_write_in.enableEXL){
    //         newStatus.EXL := io.cp0_write_in.EXL
    //     }
    //     when(io.cp0_write_in.enableVaddress){
    //         regs(32.U + 8.U)(0) := io.cp0_write_in.vAddr(7, 0)
    //         regs(32.U + 8.U)(1) := io.cp0_write_in.vAddr(15, 8)
    //         regs(32.U + 8.U)(2) := io.cp0_write_in.vAddr(23, 16)
    //         regs(32.U + 8.U)(3) := io.cp0_write_in.vAddr(31, 24)
    //     }
    //     when(io.cp0_write_in.enableOther){
    //         newCause.ExcCode := io.cp0_write_in.ExcCode
    //         newCause.BD := io.cp0_write_in.BD
    //         regs(32.U + 12.U)(0) := newStatus.asUInt()(7, 0)
    //         regs(32.U + 12.U)(1) := newStatus.asUInt()(15, 8)
    //         regs(32.U + 12.U)(2) := newStatus.asUInt()(23, 16)
    //         regs(32.U + 12.U)(3) := newStatus.asUInt()(31, 24)
    //         regs(32.U + 13.U)(0) := newCause.asUInt()(7, 0)
    //         regs(32.U + 13.U)(1) := newCause.asUInt()(15, 8)
    //         regs(32.U + 13.U)(2) := newCause.asUInt()(23, 16)
    //         regs(32.U + 13.U)(3) := newCause.asUInt()(31, 24)
    //         regs(32.U + 14.U)(0) := io.cp0_write_in.epc(7, 0)
    //         regs(32.U + 14.U)(1) := io.cp0_write_in.epc(15, 8)
    //         regs(32.U + 14.U)(2) := io.cp0_write_in.epc(23, 16)
    //         regs(32.U + 14.U)(3) := io.cp0_write_in.epc(31, 24)
    //         printf("status %x\n", newStatus.asUInt())
    //         printf("cause %x\n", newCause.asUInt())
    //     }        
    // }
    // io.cp0_status := regs(32.U + 12.U).asTypeOf(new cp0_Status_12)
    // io.cp0_cause := regs(cp0_cause).asTypeOf(new cp0_Cause_13)
    // io.cp0_entryhi := regs(32.U + 10.U).asTypeOf(new EntryHi)
    //printf("GPR[8] t0 = %x\n", regs(8).asUInt())
}

class CP0 extends Module{
    val io = IO(new Bundle{
        val cp0_index = Output(UInt(conf.data_width.W))
        val cp0_random = Output(UInt(conf.data_width.W))
        val cp0_entryhi = Output(new EntryHi)
        val cp0_status = Output(new cp0_Status_12)
        val cp0_cause = Output(new cp0_Cause_13)
        val cp0_taglo = Output(UInt(32.W))
        val cp0_taghi = Output(UInt(32.W))
        val cp0_badAddr = Output(new cp0_BadVaddr_8)
        val cp0_epc = Output(new cp0_Epc_14)
        val cp0_context = Output(new cp0_Context_4)
        val cp0_entrylo_0 = Output(new EntryLo)
        val cp0_entrylo_1 = Output(new EntryLo)
        val cp0_compare_0 = Output(UInt(conf.data_width.W))
        val cp0_count_0 = Output(UInt(conf.data_width.W))
        val irq7 = Output(Bool())

        val in_index_sel_0 = new CP0WriteInput
        val in_random_sel_0 = new CP0WriteInput
        val in_entrylo0_sel_0 = new CP0WriteInput
        val in_entrylo1_sel_0 = new CP0WriteInput
        val in_taglo_sel_0 = new CP0WriteInput
        val in_taghi_sel_0 = new CP0WriteInput
        val in_cause_sel_0 = new CP0WriteInput
        val in_status_sel_0 = new CP0WriteInput
        val in_epc_sel_0 = new CP0WriteInput
        val in_badAddr_sel_0 = new CP0WriteInput
        val in_context_sel_0 = new CP0WriteInput
        val in_entryhi_sel_0 = new CP0WriteInput
        val in_cp0_compare_0 = new CP0WriteInput
        val in_cp0_count_0 = new CP0WriteInput
        
    })
    val index_sel_0 = RegInit(0.U(conf.data_width.W)) 
    val random_sel_0 = RegEnable(io.in_random_sel_0.data, io.in_random_sel_0.en)
    val taglo_sel_0 = RegEnable(io.in_taglo_sel_0.data, io.in_taglo_sel_0.en)
    val taghi_sel_0 = RegEnable(io.in_taghi_sel_0.data, io.in_taghi_sel_0.en)   
        
    val baddAddr_sel_0 = RegInit(0.U(32.W))
    val badAddr_sel_0 = RegInit(0.U(32.W))
    val cause_sel_0 = RegInit(0.U(32.W))
    val status_sel_0 = RegInit("h_1040_0000".U(32.W))
    val epc_sel_0 = RegInit(0.U(32.W))
    val context_sel_0 = RegInit(0.U(32.W))
    val entry_hi_sel_0 = RegEnable(io.in_entryhi_sel_0.data & "h_ffffe0ff".U(conf.data_width.W), io.in_entryhi_sel_0.en)
    val entrylo_0_sel_0 = RegEnable(io.in_entrylo0_sel_0.data & "h_03ff_ffff".U(conf.data_width.W), io.in_entrylo0_sel_0.en)
    val entrylo_1_sel_0 = RegEnable(io.in_entrylo1_sel_0.data & "h_03ff_ffff".U(conf.data_width.W), io.in_entrylo1_sel_0.en)
    val count_sel_0 = RegInit(0.U(32.W))
    val compare_sel_0 = RegInit(0.U(32.W))

    io.cp0_index := index_sel_0
    io.cp0_random := random_sel_0
    io.cp0_badAddr := badAddr_sel_0.asTypeOf(new cp0_BadVaddr_8)
    io.cp0_cause := cause_sel_0.asTypeOf(new cp0_Cause_13)
    io.cp0_epc := epc_sel_0.asTypeOf(new cp0_Epc_14)
    io.cp0_status := status_sel_0.asTypeOf(new cp0_Status_12)
    io.cp0_context := context_sel_0.asTypeOf(new cp0_Context_4)
    io.cp0_entryhi := entry_hi_sel_0.asTypeOf(new EntryHi)
    io.cp0_entrylo_0 := entrylo_0_sel_0.asTypeOf(new EntryLo)
    io.cp0_entrylo_1 := entrylo_1_sel_0.asTypeOf(new EntryLo)
    io.cp0_taglo := taglo_sel_0
    io.cp0_taghi := taghi_sel_0
    io.cp0_count_0 := count_sel_0
    io.cp0_compare_0 := compare_sel_0
    printf("@cp0 cause %x\n", cause_sel_0)

    def isIrq7() = compare_sel_0 === count_sel_0
    when(io.in_cause_sel_0.en){
        cause_sel_0 := io.in_cause_sel_0.data & "b1000_0000_1100_0000_1111_1111_0111_1100".U
    }
    when(io.in_epc_sel_0.en){epc_sel_0 := io.in_epc_sel_0.data}
    when(io.in_status_sel_0.en){
        when(isIrq7()){
            status_sel_0 := io.in_status_sel_0.data | "b1000_0000_0000_0000".U
        }
        .otherwise{status_sel_0 := io.in_status_sel_0.data}
    }.otherwise{
        status_sel_0 := status_sel_0 | "b1000_0000_0000_0000".U
    }
    
    when(isIrq7()){
        io.irq7 := Y
    }.otherwise{
        io.irq7 := status_sel_0(15) === 1.U
    }


    when(io.in_badAddr_sel_0.en){badAddr_sel_0 := io.in_badAddr_sel_0.data}
    when (io.in_context_sel_0.en){context_sel_0 := io.in_context_sel_0.data}
    when(io.in_index_sel_0.en){
        index_sel_0 := Mux(io.in_index_sel_0.data==="h_8000_0000".U, io.in_index_sel_0.data, io.in_index_sel_0.data(log2Ceil(conf.tlb_size)-1, 0))
    }
    when(io.in_cp0_count_0.en){
        count_sel_0 := io.in_cp0_count_0.data
    }.otherwise{
        count_sel_0 := count_sel_0 + 1.U
    }
    when(io.in_cp0_compare_0.en){
        compare_sel_0 := io.in_cp0_compare_0.data
    }
}