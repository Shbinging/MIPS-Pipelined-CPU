circuit Example :
  module Example :
    input clock : Clock
    input reset : UInt<1>
    input io_in : UInt<32>
    output io_out : UInt<32>
  
    reg reg : UInt<32>, clock with :
      reset => (UInt<1>("h0"), reg) @[example.scala 12:22]
    io_out <= reg @[example.scala 14:12]
    reg <= mux(reset, UInt<1>("h0"), io_in) @[example.scala 13:9]
