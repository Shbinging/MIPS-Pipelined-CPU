package njumips
package configs

import chisel3._

object conf {
  val xprlen = 32
  val addr_width = 32
  val data_width = 32
  val xprbyte = xprlen / 8
  val start_addr = "hbfc00000".U
  val axi_data_width = 32
  val axi_id_width = 3
  val memio_cycles = 2
  val INSTR_ID_SZ = 6
  val mul_stages = 7
  val div_stages = 45
  val random_delay = false
  val cache_line_width = 4
  val cache_line_size = 8 * (1<<cache_line_width) // 16 Bytes
  val L1_index_width = 5 // 64 lines
  val L1_cache_size = (1<<L1_index_width) * cache_line_size // 16KB

  val tlb_size = 1<<5
}
