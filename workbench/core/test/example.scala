package example

import chisel3._
import chisel3.iotesters._
//import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._
import chisel3.iotesters.PeekPokeTester

class ExampleTest(dut: Example) extends PeekPokeTester(dut) {
  poke(dut.io.in, 0.U)
  step(1)
  expect(dut.io.out, 0.U)
}

object ExampleTestMain{
  def main(args: Array[String]): Unit = {
    chisel3.iotesters.Driver.execute(args, ()=>new Example) { c => new ExampleTest(c)}
  }
  
}