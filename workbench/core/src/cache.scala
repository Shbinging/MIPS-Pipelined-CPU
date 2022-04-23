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
    val io = new Bundle{
        val in = Flipped(new MemIO)
        val out = Flipped(new MemIO)
    }
    val req_data = RegEnable(io.in.req.bits, io.in.req.fire())
    val state = RegInit(0.U(2.W))

    io.req.in.ready := io.in.resp.fire() | (state===0.U)

    val cache = Mem(1<<conf.L1_index_width, CacheLine)

    when(reset.asBool()){
        for(i <- 0 to (1<<conf.cache_line_width)-1){
            cache(i).valid := false.B 
            cache(i).dirty := false.B
        }
    }
    val index = req_data.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
    val tag = req_data.addr(conf.addr_width, conf.cache_line_width)
    val offset = req_data.addr(conf.cache_line_width-1, 0)
    
    val line_count = 0.U(conf.cache_line_width.W)


    io.out.req.valid := false.B
    io.in.resp.valid := false.B

    when(io.in.req.fire()){
        val index_fire = io.in.req.bits.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
        val tag_fire = io.in.req.bits.addr(conf.addr_width, conf.cache_line_width)
        when(cache(index_fire).ready && cache(index_fire).tag===tag_fire){
            state := 1.U    // cache hit
        } .otherwise{    // cache miss
            line_count := 0.U(conf.cache_line_width.W)
            when(cache(index_fire).ready && cache(index_fire).dirty){ // write back 
                state := 2.U
            } .otherwise{   // read 
                state := 3.U
            }
        }
    }
    when(state===1.U){  // req ready, read data?
        io.in.resp.bits.valid := true.B
        when(req_data.func===MX_RD){
            io.in.resp.bit.data := Cat(
                Cat(Mux(req_data.len==="b11".U, cache(index).data(offset+3.U), 0.U(8.W)),
                    Mux(req_data.len >="b10".U, cache(index).data(offset+2.U), 0.U(8.W))),
                Cat(Mux(req_data.len >="b01".U, cache(index).data(offset+1.U), 0.U(8.W)),
                    Mux(req_data.len >="b00".U, cache(index).data(offset+0.U), 0.U(8.W)))
            )
        } .otherwise{   // WR
            for(i <- 3 to 0){
                when(req_data.str(i)){
                    cache(index).data(offset+i.U) := req_data.data((i+1)*8-1, i*8)
                }
            }
            cache(index).dirty := true.B
        }
        when(io.in.resp.fire() && !io.in.req.fire()){
            state := 0.U
        }
    } .elsewhen(state===2.U){  // write back 
        io.out.req.bits.func := MX_WR
        io.out.req.bits.addr := cache(index).tag.UInt<<conf.cache_line_width + line_count
        io.out.req.bits.len := 3.U 
        io.out.req.bits.strb := "b1111".U
        io.out.req.valid := true.B
        when(dev.io.in.req.fire()){
            line_count := line_count + 4.U
        }
        when(dev.io.in.resp.fire() && line_count===conf.cache_line_size.U){
            state := 3.U
        }
    } .elsewhen(state===3.U){   // read 
        io.out.req.bits.func := MX_RD
        io.out.req.bits.addr := tag.UInt<<conf.cache_line_width + line_count
        io.out.req.bits.len := 3.U 
        io.out.req.bits.strb := "b1111".U
        io.out.req.valid := true.B
        when(io.out.req.fire()){
            line_count := line_count + 4.U
        }
        when(io.out.resp.fire()){
            // TODO: cistern
            cache(index)(line_count-4.U) := io.out.resp.data(31, 24)
            cache(index)(line_count-3.U) := io.out.resp.data(23, 16)
            cache(index)(line_count-2.U) := io.out.resp.data(15, 8)
            cache(index)(line_count-1.U) := io.out.in.resp.data(7, 0)

            when(line_count===conf.cache_line_size.U){
                cache(index).valid := true.B 
                cache(index).tag := tag 
                cache(index).dirty := false.B
                state := 1.U
            }
        }
    }
}