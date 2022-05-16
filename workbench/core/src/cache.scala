package njumips
import chisel3._
import chisel3.util._
import njumips.configs._
import njumips.consts._

class CacheLine extends Bundle{
    val valid = Bool()
    val dirty = Bool()
    val tag = UInt((conf.addr_width - conf.cache_line_width).W)
    val data = Vec(1<<conf.cache_line_width, UInt(8.W))
}

class L1Cache extends Module{
    val io = IO(new Bundle{
        val cache_cmd = new CacheCommandIO
        val in = Flipped(new CacheIO)
        val out = new MemIO
    })
    val req_data = RegEnable(io.in.req.bits, io.in.req.fire())
    val resp_data = RegEnable(next=io.out.resp.bits.data, enable=io.out.resp.fire())
    val state = RegInit(0.U(32.W))
    val cache_cmd = RegEnable(io.cache_cmd, io.cache_cmd.en)

    io.in.req.ready := (io.in.resp.fire() || (state===0.U)) && (!cache_cmd.en)

    val cache = Mem(1<<conf.L1_index_width, new CacheLine)


    when(reset.asBool()){
        for(i <- 0 to (1<<conf.cache_line_width)-1){
            cache(i).valid := false.B 
            cache(i).dirty := false.B
        }
        cache_cmd.en := false.B
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
        when(io.in.req.bits.exception =/= ET_None){
            state := 5.U    // just return directly
        }. elsewhen(!io.in.req.bits.is_cached){   // uncached,
            state := 4.U
        } .otherwise{
            when(cache(index_fire).valid && cache(index_fire).tag===tag_fire){
                //printf(p"goto 1\n")
                state := 1.U    // cache hit
            } .otherwise{    // cache miss
                //printf(p"goto 2/3\n")
                line_count := 0.U(conf.cache_line_width.W)
                when(cache(index_fire).valid && cache(index_fire).dirty){ // write back 
                    state := 2.U
                } .otherwise{   // read 
                    state := 3.U
                }
            }
        }
    } .elsewhen(state===0.U && cache_cmd.en){
        when(cache_cmd.code==="b000".U){
            val idx = cache_cmd.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
            when(cache(idx).dirty){
                cache_cmd.addr := Cat(cache(idx).tag, 0.U(conf.cache_line_width.W))
                state := 6.U
            } .otherwise {
                cache(idx).valid := false.B
                cache_cmd.en := false.B
            }
        } .elsewhen(cache_cmd.code==="b010".U){

            // TODO: state := 7.U
        } .elsewhen(cache_cmd.code==="b100".U){
            val tag = cache_cmd.addr(conf.addr_width-1, conf.cache_line_width)
            val idx = cache_cmd.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
            when(cache(idx).valid && cache(idx).tag===tag){
                cache(idx).valid := false.B
            }
            cache_cmd.en := false.B 
        } .elsewhen(cache_cmd.code==="b101".U){
            val tag = cache_cmd.addr(conf.addr_width-1, conf.cache_line_width)
            val idx = cache_cmd.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
            when(cache(idx).valid && cache(idx).tag===tag){
                when(cache(idx).dirty){
                    state := 6.U
                } .otherwise{
                    cache(idx).valid := false.B 
                    cache_cmd.en := false.B
                }
            } .otherwise{
                cache_cmd.en := false.B
            }
        }
    }
    when(state===1.U){  // cache hit
        io.in.resp.valid := true.B
        io.in.resp.bits.exception := req_data.exception
        when(req_data.func===MX_RD){
            io.in.resp.bits.data := Cat(
                Cat(Mux(req_data.len==="b11".U, cache(index).data(offset+3.U), 0.U(8.W)),
                    Mux(req_data.len >="b10".U, cache(index).data(offset+2.U), 0.U(8.W))),
                Cat(Mux(req_data.len >="b01".U, cache(index).data(offset+1.U), 0.U(8.W)),
                    Mux(req_data.len >="b00".U, cache(index).data(offset+0.U), 0.U(8.W)))
            )
        } .otherwise{   // WR
            for(i <- 0 to 3){
                when(req_data.strb(i).asBool()){
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
        io.out.req.bits.addr := (cache(index).tag.asUInt()<<conf.cache_line_width) + line_count
        io.out.req.bits.len := 3.U 
        io.out.req.bits.strb := "b1111".U
        io.out.req.bits.data := Cat(
            Cat(cache(index).data(line_count+3.U), cache(index).data(line_count+2.U)), 
            Cat(cache(index).data(line_count+1.U), cache(index).data(line_count+0.U))
        )
        io.out.req.valid := true.B
        io.out.resp.ready := true.B
        when(io.out.req.fire()){
            line_count := line_count + 4.U
        }
        when(io.out.resp.fire() && line_count===(conf.cache_line_size/8).U){
            line_count := 0.U
            state := 3.U
        }
    } .elsewhen(state===3.U){   // read 
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
            cache(index).data(line_count-1.U) := io.out.resp.bits.data(31, 24)
            cache(index).data(line_count-2.U) := io.out.resp.bits.data(23, 16)
            cache(index).data(line_count-3.U) := io.out.resp.bits.data(15, 8)
            cache(index).data(line_count-4.U) := io.out.resp.bits.data(7, 0)
            when(line_count===(conf.cache_line_size/8).U){
                cache(index).valid := true.B 
                cache(index).tag := tag 
                cache(index).dirty := false.B
                state := 1.U
                line_count := 0.U
            }
        }
    } .elsewhen(state===4.U){   // uncached
        io.out.req.bits.func := req_data.func
        io.out.req.bits.addr := req_data.addr
        io.out.req.bits.len := req_data.len
        io.out.req.bits.strb := req_data.strb
        io.out.req.bits.data := req_data.data
        io.out.req.valid := true.B
        io.out.resp.ready := true.B
        when(io.out.resp.fire()){
            state := 5.U
        }
    } .elsewhen(state===5.U){
        io.in.resp.valid := true.B
        io.in.resp.bits.data := resp_data
        io.in.resp.bits.exception := req_data.exception
        when(io.in.resp.fire() && !io.in.req.fire()){
            state := 0.U
        }
    } .elsewhen(state===6.U){
        val idx = cache_cmd.addr(conf.L1_index_width+conf.cache_line_width-1, conf.cache_line_width)
        io.out.req.bits.func := MX_WR
        io.out.req.bits.addr := (cache(idx).tag.asUInt()<<conf.cache_line_width) + line_count
        io.out.req.bits.len := 3.U 
        io.out.req.bits.strb := "b1111".U
        io.out.req.bits.data := Cat(
            Cat(cache(idx).data(line_count+3.U), cache(idx).data(line_count+2.U)), 
            Cat(cache(idx).data(line_count+1.U), cache(idx).data(line_count+0.U))
        )
        io.out.req.valid := true.B
        io.out.resp.ready := true.B
        when(io.out.req.fire()){
            line_count := line_count + 4.U
        }
        when(io.out.resp.fire() && line_count===(conf.cache_line_size/8).U){
            line_count := 0.U
            state := 0.U
            cache(idx).valid := false.B 
            cache_cmd.en := false.B
        }
    }
}