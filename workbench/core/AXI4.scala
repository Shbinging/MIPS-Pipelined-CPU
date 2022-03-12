package njumips
package core

import chisel3._
import chisel3.util._
import njumips.consts._
import njumips.configs._
import njumips.utils._

class HandShakeIO extends Bundle {
  val valid = Output(Bool())
  val ready = Input(Bool())

  def fire() = valid && ready
}

abstract class AXI4ChannelA extends HandShakeIO
{
  val id = Output(UInt(conf.axi_id_width.W))
  val addr = Output(UInt(32.W))
  val len = Output(UInt(8.W))
  val size = Output(UInt(3.W))
  val burst = Output(UInt(2.W))
  val lock = Output(Bool())
  val cache = Output(UInt(4.W))
  val prot = Output(UInt(3.W))
  val region = Output(UInt(4.W))
  val qos = Output(UInt(4.W))
  val user = Output(UInt(5.W))
}

class AXI4ChannelAW extends AXI4ChannelA
{
}

class AXI4ChannelAR extends AXI4ChannelA
{
}

class AXI4ChannelW(data_width: Int) extends HandShakeIO
{
  val id = Output(UInt(conf.axi_id_width.W))
  val data = Output(UInt(data_width.W))
  val strb = Output(UInt((data_width / 8).W))
  val last = Output(Bool())
  val user = Output(UInt(5.W))

  override def cloneType = { new AXI4ChannelW(data_width).asInstanceOf[this.type] }
}

class AXI4ChannelB extends HandShakeIO
{
  val id = Output(UInt(conf.axi_id_width.W))
  val resp = Output(UInt(2.W))
  val user = Output(UInt(5.W))
}

// read data channel signals
class AXI4ChannelR(data_width: Int) extends HandShakeIO
{
  val id = Output(UInt(conf.axi_id_width.W))
  val data = Output(UInt(data_width.W))
  val resp = Output(UInt(2.W))
  val last = Output(Bool())
  val user = Output(UInt(5.W))

  override def cloneType = { new AXI4ChannelR(data_width).asInstanceOf[this.type] }
}

class AXI4IO(data_width: Int) extends Bundle 
{
  val aw = new AXI4ChannelAW
  val w = new AXI4ChannelW(data_width)
  val b = Flipped(new AXI4ChannelB)
  val ar = new AXI4ChannelAR
  val r = Flipped(new AXI4ChannelR(data_width))

  override def cloneType = { new AXI4IO(data_width).asInstanceOf[this.type] }
}
