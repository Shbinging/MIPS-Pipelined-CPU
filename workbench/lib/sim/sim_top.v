`timescale 1ns/1ps

`define TRACE_REF_FILE "../../../../../../testbench/trace.txt"

module njumips_system;

// Clocks
reg clk,resetn;

initial begin
  clk     = 1'b0;
  resetn  = 1'b0;
  #1000;
  resetn  = 1'b1;
end

always #15.15 clk     = ~clk;

wire [31:0]io_commit_gpr_0;
wire [31:0]io_commit_gpr_1;
wire [31:0]io_commit_gpr_10;
wire [31:0]io_commit_gpr_11;
wire [31:0]io_commit_gpr_12;
wire [31:0]io_commit_gpr_13;
wire [31:0]io_commit_gpr_14;
wire [31:0]io_commit_gpr_15;
wire [31:0]io_commit_gpr_16;
wire [31:0]io_commit_gpr_17;
wire [31:0]io_commit_gpr_18;
wire [31:0]io_commit_gpr_19;
wire [31:0]io_commit_gpr_2;
wire [31:0]io_commit_gpr_20;
wire [31:0]io_commit_gpr_21;
wire [31:0]io_commit_gpr_22;
wire [31:0]io_commit_gpr_23;
wire [31:0]io_commit_gpr_24;
wire [31:0]io_commit_gpr_25;
wire [31:0]io_commit_gpr_26;
wire [31:0]io_commit_gpr_27;
wire [31:0]io_commit_gpr_28;
wire [31:0]io_commit_gpr_29;
wire [31:0]io_commit_gpr_3;
wire [31:0]io_commit_gpr_30;
wire [31:0]io_commit_gpr_31;
wire [31:0]io_commit_gpr_4;
wire [31:0]io_commit_gpr_5;
wire [31:0]io_commit_gpr_6;
wire [31:0]io_commit_gpr_7;
wire [31:0]io_commit_gpr_8;
wire [31:0]io_commit_gpr_9;
wire [31:0]io_commit_instr;
wire [31:0]io_commit_pc;
wire io_commit_valid;

noop_sim noop_sim_i
   (.aresetn(resetn),
    .clk(clk),
    .dcm_locked(1'b1),
    .io_commit_gpr_0(io_commit_gpr_0),
    .io_commit_gpr_1(io_commit_gpr_1),
    .io_commit_gpr_10(io_commit_gpr_10),
    .io_commit_gpr_11(io_commit_gpr_11),
    .io_commit_gpr_12(io_commit_gpr_12),
    .io_commit_gpr_13(io_commit_gpr_13),
    .io_commit_gpr_14(io_commit_gpr_14),
    .io_commit_gpr_15(io_commit_gpr_15),
    .io_commit_gpr_16(io_commit_gpr_16),
    .io_commit_gpr_17(io_commit_gpr_17),
    .io_commit_gpr_18(io_commit_gpr_18),
    .io_commit_gpr_19(io_commit_gpr_19),
    .io_commit_gpr_2(io_commit_gpr_2),
    .io_commit_gpr_20(io_commit_gpr_20),
    .io_commit_gpr_21(io_commit_gpr_21),
    .io_commit_gpr_22(io_commit_gpr_22),
    .io_commit_gpr_23(io_commit_gpr_23),
    .io_commit_gpr_24(io_commit_gpr_24),
    .io_commit_gpr_25(io_commit_gpr_25),
    .io_commit_gpr_26(io_commit_gpr_26),
    .io_commit_gpr_27(io_commit_gpr_27),
    .io_commit_gpr_28(io_commit_gpr_28),
    .io_commit_gpr_29(io_commit_gpr_29),
    .io_commit_gpr_3(io_commit_gpr_3),
    .io_commit_gpr_30(io_commit_gpr_30),
    .io_commit_gpr_31(io_commit_gpr_31),
    .io_commit_gpr_4(io_commit_gpr_4),
    .io_commit_gpr_5(io_commit_gpr_5),
    .io_commit_gpr_6(io_commit_gpr_6),
    .io_commit_gpr_7(io_commit_gpr_7),
    .io_commit_gpr_8(io_commit_gpr_8),
    .io_commit_gpr_9(io_commit_gpr_9),
    .io_commit_instr(io_commit_instr),
    .io_commit_pc(io_commit_pc),
    .io_commit_valid(io_commit_valid));

integer trace_ref;
initial begin
    trace_ref = $fopen(`TRACE_REF_FILE, "rb");
end

reg [31:0]ref_pc;
reg [31:0]ref_hi;
reg [31:0]ref_lo;
reg [31:0]ref_ninstr;
reg [31:0]ref_instr;
reg [31:0]ref_gpr[0:31];

always@(posedge clk) begin
  #1;
  if(io_commit_valid) begin
    $fscanf(trace_ref, "$pc: 0x%h $hi: 0x%h $lo: 0x%h\n", ref_pc, ref_hi, ref_lo);
    $fscanf(trace_ref, "$ninstr: %h $instr: %h\n", ref_ninstr, ref_instr);
    $fscanf(trace_ref, "$0 :0x%h $at:0x%h $v0:0x%h $v1:0x%h\n", ref_gpr[0], ref_gpr[1], ref_gpr[2], ref_gpr[3]);
    $fscanf(trace_ref, "$a0:0x%h $a1:0x%h $a2:0x%h $a3:0x%h\n", ref_gpr[4], ref_gpr[5], ref_gpr[6], ref_gpr[7]);
    $fscanf(trace_ref, "$t0:0x%h $t1:0x%h $t2:0x%h $t3:0x%h\n", ref_gpr[8], ref_gpr[9], ref_gpr[10], ref_gpr[11]);
    $fscanf(trace_ref, "$t4:0x%h $t5:0x%h $t6:0x%h $t7:0x%h\n", ref_gpr[12], ref_gpr[13], ref_gpr[14], ref_gpr[15]);
    $fscanf(trace_ref, "$s0:0x%h $s1:0x%h $s2:0x%h $s3:0x%h\n", ref_gpr[16], ref_gpr[17], ref_gpr[18], ref_gpr[19]);
    $fscanf(trace_ref, "$s4:0x%h $s5:0x%h $s6:0x%h $s7:0x%h\n", ref_gpr[20], ref_gpr[21], ref_gpr[22], ref_gpr[23]);
    $fscanf(trace_ref, "$t8:0x%h $t9:0x%h $k0:0x%h $k1:0x%h\n", ref_gpr[24], ref_gpr[25], ref_gpr[26], ref_gpr[27]);
    $fscanf(trace_ref, "$gp:0x%h $sp:0x%h $fp:0x%h $ra:0x%h\n", ref_gpr[28], ref_gpr[29], ref_gpr[30], ref_gpr[31]);
  end
end

always@(posedge clk) begin
  #2;
  if (resetn && io_commit_valid) begin
    if (io_commit_pc != ref_pc) begin
      $display("@%0t: pc: core:0x%08h <> ref:0x%08h\n", $time, io_commit_pc, ref_pc);
      $finish;
    end

    if (io_commit_instr != ref_instr) begin
      $display("@%0t: %08x: instr: core:0x%08h <> ref:0x%08h\n", $time, ref_pc, io_commit_instr, ref_instr);
      $finish;
    end

`define GPRS(X) \
  `X(0)  `X(1)  `X(2)  `X(3)  `X(4)  `X(5)  `X(6)  `X(7)  \
  `X(8)  `X(9)  `X(10) `X(11) `X(12) `X(13) `X(14) `X(15) \
  `X(16) `X(17) `X(18) `X(19) `X(20) `X(21) `X(22) `X(23) \
  `X(24) `X(25) `X(26) `X(27) `X(28) `X(29) `X(30) `X(31)
`define GPR_TEST(n) \
    if (io_commit_gpr_``n != ref_gpr[n]) begin \
      $display("@%0t: %08x: gpr[%2d]: core:0x%08h <> ref:0x%08h\n", $time, ref_pc, n, io_commit_gpr_``n, ref_gpr[n]); \
      $finish; \
    end

    `GPRS(GPR_TEST);
  end
end

initial begin
  forever begin
    #500000;
    $display("@%0t: CPU commit PC is %x", $time, io_commit_pc);
  end
end

endmodule
