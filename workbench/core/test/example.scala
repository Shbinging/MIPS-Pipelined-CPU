package example

import chisel3._
import chisel3.iotesters._
//import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._
import chisel3.iotesters.PeekPokeTester
import src.tryy

class ExampleTest(dut: tryy) extends PeekPokeTester(dut) {
  poke(dut.io.aa.rs_addr, 3.U)
  step(1)
  print(peek(dut.io.bb.rs_addr))
}

// object ExampleTestMain{
//   def main(args: Array[String]): Unit = {
//     chisel3.iotesters.Driver.execute(args, ()=>new tryy) { c => new ExampleTest(c)}
//   }
  
// }