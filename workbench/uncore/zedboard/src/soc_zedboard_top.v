module uart_flip(
  input in_txd,
  output in_rxd,
  output out_txd,
  input out_rxd
);
  assign out_txd = in_txd;
  assign in_rxd = out_rxd;
endmodule


module soc_zedboard_top
   (VGA_blue,
    VGA_clk,
    VGA_de,
    VGA_green,
    VGA_hsync,
    VGA_red,
    VGA_vsync);
  output [3:0]VGA_blue;
  output VGA_clk;
  output VGA_de;
  output [3:0]VGA_green;
  output VGA_hsync;
  output [3:0]VGA_red;
  output VGA_vsync;

  wire [3:0]VGA_blue;
  wire VGA_clk;
  wire VGA_de;
  wire [3:0]VGA_green;
  wire VGA_hsync;
  wire [3:0]VGA_red;
  wire VGA_vsync;

  noop_design noop_design_i
       (.VGA_blue(VGA_blue),
        .VGA_clk(VGA_clk),
        .VGA_de(VGA_de),
        .VGA_green(VGA_green),
        .VGA_hsync(VGA_hsync),
        .VGA_red(VGA_red),
        .VGA_vsync(VGA_vsync));
endmodule
