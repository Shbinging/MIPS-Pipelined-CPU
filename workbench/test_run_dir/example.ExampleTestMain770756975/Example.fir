;buildInfoPackage: chisel3, version: 3.4.3, scalaVersion: 2.12.12, sbtVersion: 1.3.10
circuit Example : 
  module Example : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip in : UInt<32>, out : UInt<32>}
    
    reg reg : UInt, clock with : (reset => (reset, UInt<1>("h00"))) @[example.scala 12:22]
    reg <= io.in @[example.scala 13:9]
    io.out <= reg @[example.scala 14:12]
    
