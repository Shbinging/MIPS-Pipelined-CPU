package njumips
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class CacheLine extends Bundle{
    val valid = Bool()
    val dirty = Bool()
    val tag = UInt((conf.addr_width - conf.cache_line_width).W)
    val data = Mem(1<<conf.cache_line_width, UInt(8.W))
}

class L1Cache extends Module{
    val io = IO(new Bundle{
        val in = Flipped(new MemIO)
        val out = new MemIO
    })
    val req_data = RegEnable(io.in.req.bits, io.in.req.fire())
    val state = RegInit(0.U(2.W))

    io.in.req.ready := io.in.resp.fire() | (state===0.U)

    val cache = Mem(1<<conf.L1_index_width, Reg(new CacheLine))

    when(reset.asBool()){
        for(i <- 0 to (1<<conf.cache_line_width)-1){
            cache(i).valid := false.B 
            cache(i).dirty := false.B
        }
    }
    val index = req_data.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
    val tag = req_data.addr(conf.addr_width-1, conf.cache_line_width)
    val offset = req_data.addr(conf.cache_line_width-1, 0)
    
    val line_count = RegInit(0.U(32.W))

    
    io.out.req.valid := false.B
    io.out.req.bits <> DontCare
    
    io.out.resp.ready := false.B
    
    io.in.resp.valid := false.B
    io.in.resp.bits <> DontCare

    when(io.in.req.fire()){
        val index_fire = io.in.req.bits.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
        val tag_fire = io.in.req.bits.addr(conf.addr_width-1, conf.cache_line_width)
        when(cache(index_fire).valid && cache(index_fire).tag===tag_fire){
            state := 1.U    // cache hit
        } .otherwise{    // cache miss
            line_count := 0.U(conf.cache_line_width.W)
            when(cache(index_fire).valid && cache(index_fire).dirty){ // write back 
                state := 2.U
            } .otherwise{   // read 
                state := 3.U
            }
        }
    }
    when(state===1.U){  // req ready, read data?
        printf("state 1\n")
        io.in.resp.valid := true.B
        when(req_data.func===MX_RD){
            io.in.resp.bits.data := Cat(
                Cat(Mux(req_data.len==="b11".U, cache(index).data(offset+3.U), 0.U(8.W)),
                    Mux(req_data.len >="b10".U, cache(index).data(offset+2.U), 0.U(8.W))),
                Cat(Mux(req_data.len >="b01".U, cache(index).data(offset+1.U), 0.U(8.W)),
                    Mux(req_data.len >="b00".U, cache(index).data(offset+0.U), 0.U(8.W)))
            )
            printf(p"length: ${req_data.len} ${offset} ${io.in.resp.bits.data}\n")
            printf(p"${index}: ${cache(index).data(0.U)}, ${cache(index).data(1.U)}, ${cache(index).data(2.U)}, ${cache(index).data(3.U)}\n")
        } .otherwise{   // WR
            for(i <- 3 to 0){
                when(req_data.strb(i)){
                    cache(index).data(offset+i.U) := req_data.data((i+1)*8-1, i*8)
                }
            }
            cache(index).dirty := true.B
        }
        when(io.in.resp.fire() && !io.in.req.fire()){
            state := 0.U
        }
    } .elsewhen(state===2.U){  // write back 
        printf("state 2\n")
        io.out.req.bits.func := MX_WR
        io.out.req.bits.addr := (cache(index).tag.asUInt()<<conf.cache_line_width) + line_count
        io.out.req.bits.len := 3.U 
        io.out.req.bits.strb := "b1111".U
        io.out.req.valid := true.B
        // TODO: cistern 
        when(io.out.req.fire()){
            line_count := line_count + 4.U
        }
        when(io.out.resp.fire() && line_count===(conf.cache_line_size/8).U){
            line_count := 0.U
            state := 3.U
        }
    } .elsewhen(state===3.U){   // read 
        printf(p"state 3, ${line_count}/${conf.cache_line_size.U}\n")
        io.out.req.bits.func := MX_RD
        io.out.req.bits.addr := (tag.asUInt()<<conf.cache_line_width) + line_count
        io.out.req.bits.len := 3.U 
        io.out.req.bits.strb := "b1111".U
        io.out.req.valid := true.B
        io.out.resp.ready := true.B
        when(io.out.req.fire()){
            line_count := line_count + 4.U
        }
        when(io.out.resp.fire()){
            // TODO: cistern
            cache(index).data(line_count-1.U) := io.out.resp.bits.data(31, 24)
            cache(index).data(line_count-2.U) := io.out.resp.bits.data(23, 16)
            cache(index).data(line_count-3.U) := io.out.resp.bits.data(15, 8)
            cache(index).data(line_count-4.U) := io.out.resp.bits.data(7, 0)
            printf(p"READ ${index}-${line_count-4.U}: ${io.out.resp.bits.data(7,0)}, ${io.out.resp.bits.data(15,8)}, ${io.out.resp.bits.data(23,16)}, ${io.out.resp.bits.data(31, 24)}\n")
            // printf(p"${index}: ${line_count-4.U} read: ${io.out.resp.bits.data}\n")
            printf(p"RD${index}: ${cache(index).data(0.U)}, ${cache(index).data(1.U)}, ${cache(index).data(2.U)}, ${cache(index).data(3.U)}\n")
            when(line_count===(conf.cache_line_size/8).U){
                printf(p" state 3 done!\n")
                cache(index).valid := true.B 
                cache(index).tag := tag 
                cache(index).dirty := false.B
                state := 1.U
                line_count := 0.U
            }
        }
    }
}