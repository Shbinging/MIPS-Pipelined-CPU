
package src

import chisel3._
import chisel3.iotesters._
//import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._
import chisel3.iotesters.PeekPokeTester

class TryyyTest(dut: tryyy) extends PeekPokeTester(dut) {
  poke(dut.io.input_data.data, 1.U)
  step(1)
  poke(dut.io.input_data.data, 2.U)
  expect(dut.io.output_data.data, 1.U)
  print(peek(dut.io.output_data.data))
}

// object TryyyTestMain{
//   def main(args: Array[String]): Unit = {
//     chisel3.iotesters.Driver.execute(args, ()=>new tryyy) { c => new TryyyTest(c)}
//   }
  
// }