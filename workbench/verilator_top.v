module GPR(
  input         clock,
  input         reset,
  input  [4:0]  io_read_in_rs_addr,
  input  [4:0]  io_read_in_rt_addr,
  input  [3:0]  io_write_in_w_en,
  input  [4:0]  io_write_in_addr,
  input  [31:0] io_write_in_data,
  output [31:0] io_read_out_rs_data,
  output [31:0] io_read_out_rt_data,
  output [31:0] io_gpr_commit_0,
  output [31:0] io_gpr_commit_1,
  output [31:0] io_gpr_commit_2,
  output [31:0] io_gpr_commit_3,
  output [31:0] io_gpr_commit_4,
  output [31:0] io_gpr_commit_5,
  output [31:0] io_gpr_commit_6,
  output [31:0] io_gpr_commit_7,
  output [31:0] io_gpr_commit_8,
  output [31:0] io_gpr_commit_9,
  output [31:0] io_gpr_commit_10,
  output [31:0] io_gpr_commit_11,
  output [31:0] io_gpr_commit_12,
  output [31:0] io_gpr_commit_13,
  output [31:0] io_gpr_commit_14,
  output [31:0] io_gpr_commit_15,
  output [31:0] io_gpr_commit_16,
  output [31:0] io_gpr_commit_17,
  output [31:0] io_gpr_commit_18,
  output [31:0] io_gpr_commit_19,
  output [31:0] io_gpr_commit_20,
  output [31:0] io_gpr_commit_21,
  output [31:0] io_gpr_commit_22,
  output [31:0] io_gpr_commit_23,
  output [31:0] io_gpr_commit_24,
  output [31:0] io_gpr_commit_25,
  output [31:0] io_gpr_commit_26,
  output [31:0] io_gpr_commit_27,
  output [31:0] io_gpr_commit_28,
  output [31:0] io_gpr_commit_29,
  output [31:0] io_gpr_commit_30,
  output [31:0] io_gpr_commit_31
);
  reg [7:0] gprs_0_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_0;
  reg [7:0] gprs_0_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_1;
  reg [7:0] gprs_0_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_2;
  reg [7:0] gprs_0_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_3;
  reg [7:0] gprs_1_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_4;
  reg [7:0] gprs_1_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_5;
  reg [7:0] gprs_1_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_6;
  reg [7:0] gprs_1_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_7;
  reg [7:0] gprs_2_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_8;
  reg [7:0] gprs_2_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_9;
  reg [7:0] gprs_2_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_10;
  reg [7:0] gprs_2_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_11;
  reg [7:0] gprs_3_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_12;
  reg [7:0] gprs_3_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_13;
  reg [7:0] gprs_3_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_14;
  reg [7:0] gprs_3_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_15;
  reg [7:0] gprs_4_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_16;
  reg [7:0] gprs_4_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_17;
  reg [7:0] gprs_4_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_18;
  reg [7:0] gprs_4_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_19;
  reg [7:0] gprs_5_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_20;
  reg [7:0] gprs_5_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_21;
  reg [7:0] gprs_5_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_22;
  reg [7:0] gprs_5_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_23;
  reg [7:0] gprs_6_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_24;
  reg [7:0] gprs_6_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_25;
  reg [7:0] gprs_6_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_26;
  reg [7:0] gprs_6_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_27;
  reg [7:0] gprs_7_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_28;
  reg [7:0] gprs_7_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_29;
  reg [7:0] gprs_7_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_30;
  reg [7:0] gprs_7_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_31;
  reg [7:0] gprs_8_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_32;
  reg [7:0] gprs_8_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_33;
  reg [7:0] gprs_8_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_34;
  reg [7:0] gprs_8_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_35;
  reg [7:0] gprs_9_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_36;
  reg [7:0] gprs_9_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_37;
  reg [7:0] gprs_9_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_38;
  reg [7:0] gprs_9_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_39;
  reg [7:0] gprs_10_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_40;
  reg [7:0] gprs_10_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_41;
  reg [7:0] gprs_10_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_42;
  reg [7:0] gprs_10_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_43;
  reg [7:0] gprs_11_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_44;
  reg [7:0] gprs_11_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_45;
  reg [7:0] gprs_11_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_46;
  reg [7:0] gprs_11_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_47;
  reg [7:0] gprs_12_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_48;
  reg [7:0] gprs_12_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_49;
  reg [7:0] gprs_12_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_50;
  reg [7:0] gprs_12_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_51;
  reg [7:0] gprs_13_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_52;
  reg [7:0] gprs_13_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_53;
  reg [7:0] gprs_13_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_54;
  reg [7:0] gprs_13_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_55;
  reg [7:0] gprs_14_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_56;
  reg [7:0] gprs_14_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_57;
  reg [7:0] gprs_14_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_58;
  reg [7:0] gprs_14_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_59;
  reg [7:0] gprs_15_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_60;
  reg [7:0] gprs_15_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_61;
  reg [7:0] gprs_15_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_62;
  reg [7:0] gprs_15_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_63;
  reg [7:0] gprs_16_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_64;
  reg [7:0] gprs_16_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_65;
  reg [7:0] gprs_16_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_66;
  reg [7:0] gprs_16_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_67;
  reg [7:0] gprs_17_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_68;
  reg [7:0] gprs_17_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_69;
  reg [7:0] gprs_17_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_70;
  reg [7:0] gprs_17_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_71;
  reg [7:0] gprs_18_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_72;
  reg [7:0] gprs_18_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_73;
  reg [7:0] gprs_18_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_74;
  reg [7:0] gprs_18_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_75;
  reg [7:0] gprs_19_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_76;
  reg [7:0] gprs_19_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_77;
  reg [7:0] gprs_19_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_78;
  reg [7:0] gprs_19_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_79;
  reg [7:0] gprs_20_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_80;
  reg [7:0] gprs_20_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_81;
  reg [7:0] gprs_20_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_82;
  reg [7:0] gprs_20_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_83;
  reg [7:0] gprs_21_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_84;
  reg [7:0] gprs_21_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_85;
  reg [7:0] gprs_21_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_86;
  reg [7:0] gprs_21_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_87;
  reg [7:0] gprs_22_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_88;
  reg [7:0] gprs_22_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_89;
  reg [7:0] gprs_22_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_90;
  reg [7:0] gprs_22_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_91;
  reg [7:0] gprs_23_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_92;
  reg [7:0] gprs_23_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_93;
  reg [7:0] gprs_23_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_94;
  reg [7:0] gprs_23_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_95;
  reg [7:0] gprs_24_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_96;
  reg [7:0] gprs_24_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_97;
  reg [7:0] gprs_24_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_98;
  reg [7:0] gprs_24_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_99;
  reg [7:0] gprs_25_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_100;
  reg [7:0] gprs_25_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_101;
  reg [7:0] gprs_25_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_102;
  reg [7:0] gprs_25_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_103;
  reg [7:0] gprs_26_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_104;
  reg [7:0] gprs_26_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_105;
  reg [7:0] gprs_26_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_106;
  reg [7:0] gprs_26_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_107;
  reg [7:0] gprs_27_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_108;
  reg [7:0] gprs_27_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_109;
  reg [7:0] gprs_27_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_110;
  reg [7:0] gprs_27_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_111;
  reg [7:0] gprs_28_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_112;
  reg [7:0] gprs_28_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_113;
  reg [7:0] gprs_28_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_114;
  reg [7:0] gprs_28_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_115;
  reg [7:0] gprs_29_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_116;
  reg [7:0] gprs_29_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_117;
  reg [7:0] gprs_29_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_118;
  reg [7:0] gprs_29_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_119;
  reg [7:0] gprs_30_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_120;
  reg [7:0] gprs_30_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_121;
  reg [7:0] gprs_30_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_122;
  reg [7:0] gprs_30_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_123;
  reg [7:0] gprs_31_0; // @[registers.scala 31:23]
  reg [31:0] _RAND_124;
  reg [7:0] gprs_31_1; // @[registers.scala 31:23]
  reg [31:0] _RAND_125;
  reg [7:0] gprs_31_2; // @[registers.scala 31:23]
  reg [31:0] _RAND_126;
  reg [7:0] gprs_31_3; // @[registers.scala 31:23]
  reg [31:0] _RAND_127;
  wire [7:0] _GEN_4 = 5'h1 == io_read_in_rs_addr ? gprs_1_0 : gprs_0_0; // @[registers.scala 34:59]
  wire [7:0] _GEN_5 = 5'h1 == io_read_in_rs_addr ? gprs_1_1 : gprs_0_1; // @[registers.scala 34:59]
  wire [7:0] _GEN_6 = 5'h1 == io_read_in_rs_addr ? gprs_1_2 : gprs_0_2; // @[registers.scala 34:59]
  wire [7:0] _GEN_7 = 5'h1 == io_read_in_rs_addr ? gprs_1_3 : gprs_0_3; // @[registers.scala 34:59]
  wire [7:0] _GEN_8 = 5'h2 == io_read_in_rs_addr ? gprs_2_0 : _GEN_4; // @[registers.scala 34:59]
  wire [7:0] _GEN_9 = 5'h2 == io_read_in_rs_addr ? gprs_2_1 : _GEN_5; // @[registers.scala 34:59]
  wire [7:0] _GEN_10 = 5'h2 == io_read_in_rs_addr ? gprs_2_2 : _GEN_6; // @[registers.scala 34:59]
  wire [7:0] _GEN_11 = 5'h2 == io_read_in_rs_addr ? gprs_2_3 : _GEN_7; // @[registers.scala 34:59]
  wire [7:0] _GEN_12 = 5'h3 == io_read_in_rs_addr ? gprs_3_0 : _GEN_8; // @[registers.scala 34:59]
  wire [7:0] _GEN_13 = 5'h3 == io_read_in_rs_addr ? gprs_3_1 : _GEN_9; // @[registers.scala 34:59]
  wire [7:0] _GEN_14 = 5'h3 == io_read_in_rs_addr ? gprs_3_2 : _GEN_10; // @[registers.scala 34:59]
  wire [7:0] _GEN_15 = 5'h3 == io_read_in_rs_addr ? gprs_3_3 : _GEN_11; // @[registers.scala 34:59]
  wire [7:0] _GEN_16 = 5'h4 == io_read_in_rs_addr ? gprs_4_0 : _GEN_12; // @[registers.scala 34:59]
  wire [7:0] _GEN_17 = 5'h4 == io_read_in_rs_addr ? gprs_4_1 : _GEN_13; // @[registers.scala 34:59]
  wire [7:0] _GEN_18 = 5'h4 == io_read_in_rs_addr ? gprs_4_2 : _GEN_14; // @[registers.scala 34:59]
  wire [7:0] _GEN_19 = 5'h4 == io_read_in_rs_addr ? gprs_4_3 : _GEN_15; // @[registers.scala 34:59]
  wire [7:0] _GEN_20 = 5'h5 == io_read_in_rs_addr ? gprs_5_0 : _GEN_16; // @[registers.scala 34:59]
  wire [7:0] _GEN_21 = 5'h5 == io_read_in_rs_addr ? gprs_5_1 : _GEN_17; // @[registers.scala 34:59]
  wire [7:0] _GEN_22 = 5'h5 == io_read_in_rs_addr ? gprs_5_2 : _GEN_18; // @[registers.scala 34:59]
  wire [7:0] _GEN_23 = 5'h5 == io_read_in_rs_addr ? gprs_5_3 : _GEN_19; // @[registers.scala 34:59]
  wire [7:0] _GEN_24 = 5'h6 == io_read_in_rs_addr ? gprs_6_0 : _GEN_20; // @[registers.scala 34:59]
  wire [7:0] _GEN_25 = 5'h6 == io_read_in_rs_addr ? gprs_6_1 : _GEN_21; // @[registers.scala 34:59]
  wire [7:0] _GEN_26 = 5'h6 == io_read_in_rs_addr ? gprs_6_2 : _GEN_22; // @[registers.scala 34:59]
  wire [7:0] _GEN_27 = 5'h6 == io_read_in_rs_addr ? gprs_6_3 : _GEN_23; // @[registers.scala 34:59]
  wire [7:0] _GEN_28 = 5'h7 == io_read_in_rs_addr ? gprs_7_0 : _GEN_24; // @[registers.scala 34:59]
  wire [7:0] _GEN_29 = 5'h7 == io_read_in_rs_addr ? gprs_7_1 : _GEN_25; // @[registers.scala 34:59]
  wire [7:0] _GEN_30 = 5'h7 == io_read_in_rs_addr ? gprs_7_2 : _GEN_26; // @[registers.scala 34:59]
  wire [7:0] _GEN_31 = 5'h7 == io_read_in_rs_addr ? gprs_7_3 : _GEN_27; // @[registers.scala 34:59]
  wire [7:0] _GEN_32 = 5'h8 == io_read_in_rs_addr ? gprs_8_0 : _GEN_28; // @[registers.scala 34:59]
  wire [7:0] _GEN_33 = 5'h8 == io_read_in_rs_addr ? gprs_8_1 : _GEN_29; // @[registers.scala 34:59]
  wire [7:0] _GEN_34 = 5'h8 == io_read_in_rs_addr ? gprs_8_2 : _GEN_30; // @[registers.scala 34:59]
  wire [7:0] _GEN_35 = 5'h8 == io_read_in_rs_addr ? gprs_8_3 : _GEN_31; // @[registers.scala 34:59]
  wire [7:0] _GEN_36 = 5'h9 == io_read_in_rs_addr ? gprs_9_0 : _GEN_32; // @[registers.scala 34:59]
  wire [7:0] _GEN_37 = 5'h9 == io_read_in_rs_addr ? gprs_9_1 : _GEN_33; // @[registers.scala 34:59]
  wire [7:0] _GEN_38 = 5'h9 == io_read_in_rs_addr ? gprs_9_2 : _GEN_34; // @[registers.scala 34:59]
  wire [7:0] _GEN_39 = 5'h9 == io_read_in_rs_addr ? gprs_9_3 : _GEN_35; // @[registers.scala 34:59]
  wire [7:0] _GEN_40 = 5'ha == io_read_in_rs_addr ? gprs_10_0 : _GEN_36; // @[registers.scala 34:59]
  wire [7:0] _GEN_41 = 5'ha == io_read_in_rs_addr ? gprs_10_1 : _GEN_37; // @[registers.scala 34:59]
  wire [7:0] _GEN_42 = 5'ha == io_read_in_rs_addr ? gprs_10_2 : _GEN_38; // @[registers.scala 34:59]
  wire [7:0] _GEN_43 = 5'ha == io_read_in_rs_addr ? gprs_10_3 : _GEN_39; // @[registers.scala 34:59]
  wire [7:0] _GEN_44 = 5'hb == io_read_in_rs_addr ? gprs_11_0 : _GEN_40; // @[registers.scala 34:59]
  wire [7:0] _GEN_45 = 5'hb == io_read_in_rs_addr ? gprs_11_1 : _GEN_41; // @[registers.scala 34:59]
  wire [7:0] _GEN_46 = 5'hb == io_read_in_rs_addr ? gprs_11_2 : _GEN_42; // @[registers.scala 34:59]
  wire [7:0] _GEN_47 = 5'hb == io_read_in_rs_addr ? gprs_11_3 : _GEN_43; // @[registers.scala 34:59]
  wire [7:0] _GEN_48 = 5'hc == io_read_in_rs_addr ? gprs_12_0 : _GEN_44; // @[registers.scala 34:59]
  wire [7:0] _GEN_49 = 5'hc == io_read_in_rs_addr ? gprs_12_1 : _GEN_45; // @[registers.scala 34:59]
  wire [7:0] _GEN_50 = 5'hc == io_read_in_rs_addr ? gprs_12_2 : _GEN_46; // @[registers.scala 34:59]
  wire [7:0] _GEN_51 = 5'hc == io_read_in_rs_addr ? gprs_12_3 : _GEN_47; // @[registers.scala 34:59]
  wire [7:0] _GEN_52 = 5'hd == io_read_in_rs_addr ? gprs_13_0 : _GEN_48; // @[registers.scala 34:59]
  wire [7:0] _GEN_53 = 5'hd == io_read_in_rs_addr ? gprs_13_1 : _GEN_49; // @[registers.scala 34:59]
  wire [7:0] _GEN_54 = 5'hd == io_read_in_rs_addr ? gprs_13_2 : _GEN_50; // @[registers.scala 34:59]
  wire [7:0] _GEN_55 = 5'hd == io_read_in_rs_addr ? gprs_13_3 : _GEN_51; // @[registers.scala 34:59]
  wire [7:0] _GEN_56 = 5'he == io_read_in_rs_addr ? gprs_14_0 : _GEN_52; // @[registers.scala 34:59]
  wire [7:0] _GEN_57 = 5'he == io_read_in_rs_addr ? gprs_14_1 : _GEN_53; // @[registers.scala 34:59]
  wire [7:0] _GEN_58 = 5'he == io_read_in_rs_addr ? gprs_14_2 : _GEN_54; // @[registers.scala 34:59]
  wire [7:0] _GEN_59 = 5'he == io_read_in_rs_addr ? gprs_14_3 : _GEN_55; // @[registers.scala 34:59]
  wire [7:0] _GEN_60 = 5'hf == io_read_in_rs_addr ? gprs_15_0 : _GEN_56; // @[registers.scala 34:59]
  wire [7:0] _GEN_61 = 5'hf == io_read_in_rs_addr ? gprs_15_1 : _GEN_57; // @[registers.scala 34:59]
  wire [7:0] _GEN_62 = 5'hf == io_read_in_rs_addr ? gprs_15_2 : _GEN_58; // @[registers.scala 34:59]
  wire [7:0] _GEN_63 = 5'hf == io_read_in_rs_addr ? gprs_15_3 : _GEN_59; // @[registers.scala 34:59]
  wire [7:0] _GEN_64 = 5'h10 == io_read_in_rs_addr ? gprs_16_0 : _GEN_60; // @[registers.scala 34:59]
  wire [7:0] _GEN_65 = 5'h10 == io_read_in_rs_addr ? gprs_16_1 : _GEN_61; // @[registers.scala 34:59]
  wire [7:0] _GEN_66 = 5'h10 == io_read_in_rs_addr ? gprs_16_2 : _GEN_62; // @[registers.scala 34:59]
  wire [7:0] _GEN_67 = 5'h10 == io_read_in_rs_addr ? gprs_16_3 : _GEN_63; // @[registers.scala 34:59]
  wire [7:0] _GEN_68 = 5'h11 == io_read_in_rs_addr ? gprs_17_0 : _GEN_64; // @[registers.scala 34:59]
  wire [7:0] _GEN_69 = 5'h11 == io_read_in_rs_addr ? gprs_17_1 : _GEN_65; // @[registers.scala 34:59]
  wire [7:0] _GEN_70 = 5'h11 == io_read_in_rs_addr ? gprs_17_2 : _GEN_66; // @[registers.scala 34:59]
  wire [7:0] _GEN_71 = 5'h11 == io_read_in_rs_addr ? gprs_17_3 : _GEN_67; // @[registers.scala 34:59]
  wire [7:0] _GEN_72 = 5'h12 == io_read_in_rs_addr ? gprs_18_0 : _GEN_68; // @[registers.scala 34:59]
  wire [7:0] _GEN_73 = 5'h12 == io_read_in_rs_addr ? gprs_18_1 : _GEN_69; // @[registers.scala 34:59]
  wire [7:0] _GEN_74 = 5'h12 == io_read_in_rs_addr ? gprs_18_2 : _GEN_70; // @[registers.scala 34:59]
  wire [7:0] _GEN_75 = 5'h12 == io_read_in_rs_addr ? gprs_18_3 : _GEN_71; // @[registers.scala 34:59]
  wire [7:0] _GEN_76 = 5'h13 == io_read_in_rs_addr ? gprs_19_0 : _GEN_72; // @[registers.scala 34:59]
  wire [7:0] _GEN_77 = 5'h13 == io_read_in_rs_addr ? gprs_19_1 : _GEN_73; // @[registers.scala 34:59]
  wire [7:0] _GEN_78 = 5'h13 == io_read_in_rs_addr ? gprs_19_2 : _GEN_74; // @[registers.scala 34:59]
  wire [7:0] _GEN_79 = 5'h13 == io_read_in_rs_addr ? gprs_19_3 : _GEN_75; // @[registers.scala 34:59]
  wire [7:0] _GEN_80 = 5'h14 == io_read_in_rs_addr ? gprs_20_0 : _GEN_76; // @[registers.scala 34:59]
  wire [7:0] _GEN_81 = 5'h14 == io_read_in_rs_addr ? gprs_20_1 : _GEN_77; // @[registers.scala 34:59]
  wire [7:0] _GEN_82 = 5'h14 == io_read_in_rs_addr ? gprs_20_2 : _GEN_78; // @[registers.scala 34:59]
  wire [7:0] _GEN_83 = 5'h14 == io_read_in_rs_addr ? gprs_20_3 : _GEN_79; // @[registers.scala 34:59]
  wire [7:0] _GEN_84 = 5'h15 == io_read_in_rs_addr ? gprs_21_0 : _GEN_80; // @[registers.scala 34:59]
  wire [7:0] _GEN_85 = 5'h15 == io_read_in_rs_addr ? gprs_21_1 : _GEN_81; // @[registers.scala 34:59]
  wire [7:0] _GEN_86 = 5'h15 == io_read_in_rs_addr ? gprs_21_2 : _GEN_82; // @[registers.scala 34:59]
  wire [7:0] _GEN_87 = 5'h15 == io_read_in_rs_addr ? gprs_21_3 : _GEN_83; // @[registers.scala 34:59]
  wire [7:0] _GEN_88 = 5'h16 == io_read_in_rs_addr ? gprs_22_0 : _GEN_84; // @[registers.scala 34:59]
  wire [7:0] _GEN_89 = 5'h16 == io_read_in_rs_addr ? gprs_22_1 : _GEN_85; // @[registers.scala 34:59]
  wire [7:0] _GEN_90 = 5'h16 == io_read_in_rs_addr ? gprs_22_2 : _GEN_86; // @[registers.scala 34:59]
  wire [7:0] _GEN_91 = 5'h16 == io_read_in_rs_addr ? gprs_22_3 : _GEN_87; // @[registers.scala 34:59]
  wire [7:0] _GEN_92 = 5'h17 == io_read_in_rs_addr ? gprs_23_0 : _GEN_88; // @[registers.scala 34:59]
  wire [7:0] _GEN_93 = 5'h17 == io_read_in_rs_addr ? gprs_23_1 : _GEN_89; // @[registers.scala 34:59]
  wire [7:0] _GEN_94 = 5'h17 == io_read_in_rs_addr ? gprs_23_2 : _GEN_90; // @[registers.scala 34:59]
  wire [7:0] _GEN_95 = 5'h17 == io_read_in_rs_addr ? gprs_23_3 : _GEN_91; // @[registers.scala 34:59]
  wire [7:0] _GEN_96 = 5'h18 == io_read_in_rs_addr ? gprs_24_0 : _GEN_92; // @[registers.scala 34:59]
  wire [7:0] _GEN_97 = 5'h18 == io_read_in_rs_addr ? gprs_24_1 : _GEN_93; // @[registers.scala 34:59]
  wire [7:0] _GEN_98 = 5'h18 == io_read_in_rs_addr ? gprs_24_2 : _GEN_94; // @[registers.scala 34:59]
  wire [7:0] _GEN_99 = 5'h18 == io_read_in_rs_addr ? gprs_24_3 : _GEN_95; // @[registers.scala 34:59]
  wire [7:0] _GEN_100 = 5'h19 == io_read_in_rs_addr ? gprs_25_0 : _GEN_96; // @[registers.scala 34:59]
  wire [7:0] _GEN_101 = 5'h19 == io_read_in_rs_addr ? gprs_25_1 : _GEN_97; // @[registers.scala 34:59]
  wire [7:0] _GEN_102 = 5'h19 == io_read_in_rs_addr ? gprs_25_2 : _GEN_98; // @[registers.scala 34:59]
  wire [7:0] _GEN_103 = 5'h19 == io_read_in_rs_addr ? gprs_25_3 : _GEN_99; // @[registers.scala 34:59]
  wire [7:0] _GEN_104 = 5'h1a == io_read_in_rs_addr ? gprs_26_0 : _GEN_100; // @[registers.scala 34:59]
  wire [7:0] _GEN_105 = 5'h1a == io_read_in_rs_addr ? gprs_26_1 : _GEN_101; // @[registers.scala 34:59]
  wire [7:0] _GEN_106 = 5'h1a == io_read_in_rs_addr ? gprs_26_2 : _GEN_102; // @[registers.scala 34:59]
  wire [7:0] _GEN_107 = 5'h1a == io_read_in_rs_addr ? gprs_26_3 : _GEN_103; // @[registers.scala 34:59]
  wire [7:0] _GEN_108 = 5'h1b == io_read_in_rs_addr ? gprs_27_0 : _GEN_104; // @[registers.scala 34:59]
  wire [7:0] _GEN_109 = 5'h1b == io_read_in_rs_addr ? gprs_27_1 : _GEN_105; // @[registers.scala 34:59]
  wire [7:0] _GEN_110 = 5'h1b == io_read_in_rs_addr ? gprs_27_2 : _GEN_106; // @[registers.scala 34:59]
  wire [7:0] _GEN_111 = 5'h1b == io_read_in_rs_addr ? gprs_27_3 : _GEN_107; // @[registers.scala 34:59]
  wire [7:0] _GEN_112 = 5'h1c == io_read_in_rs_addr ? gprs_28_0 : _GEN_108; // @[registers.scala 34:59]
  wire [7:0] _GEN_113 = 5'h1c == io_read_in_rs_addr ? gprs_28_1 : _GEN_109; // @[registers.scala 34:59]
  wire [7:0] _GEN_114 = 5'h1c == io_read_in_rs_addr ? gprs_28_2 : _GEN_110; // @[registers.scala 34:59]
  wire [7:0] _GEN_115 = 5'h1c == io_read_in_rs_addr ? gprs_28_3 : _GEN_111; // @[registers.scala 34:59]
  wire [7:0] _GEN_116 = 5'h1d == io_read_in_rs_addr ? gprs_29_0 : _GEN_112; // @[registers.scala 34:59]
  wire [7:0] _GEN_117 = 5'h1d == io_read_in_rs_addr ? gprs_29_1 : _GEN_113; // @[registers.scala 34:59]
  wire [7:0] _GEN_118 = 5'h1d == io_read_in_rs_addr ? gprs_29_2 : _GEN_114; // @[registers.scala 34:59]
  wire [7:0] _GEN_119 = 5'h1d == io_read_in_rs_addr ? gprs_29_3 : _GEN_115; // @[registers.scala 34:59]
  wire [7:0] _GEN_120 = 5'h1e == io_read_in_rs_addr ? gprs_30_0 : _GEN_116; // @[registers.scala 34:59]
  wire [7:0] _GEN_121 = 5'h1e == io_read_in_rs_addr ? gprs_30_1 : _GEN_117; // @[registers.scala 34:59]
  wire [7:0] _GEN_122 = 5'h1e == io_read_in_rs_addr ? gprs_30_2 : _GEN_118; // @[registers.scala 34:59]
  wire [7:0] _GEN_123 = 5'h1e == io_read_in_rs_addr ? gprs_30_3 : _GEN_119; // @[registers.scala 34:59]
  wire [7:0] _GEN_124 = 5'h1f == io_read_in_rs_addr ? gprs_31_0 : _GEN_120; // @[registers.scala 34:59]
  wire [7:0] _GEN_125 = 5'h1f == io_read_in_rs_addr ? gprs_31_1 : _GEN_121; // @[registers.scala 34:59]
  wire [7:0] _GEN_126 = 5'h1f == io_read_in_rs_addr ? gprs_31_2 : _GEN_122; // @[registers.scala 34:59]
  wire [7:0] _GEN_127 = 5'h1f == io_read_in_rs_addr ? gprs_31_3 : _GEN_123; // @[registers.scala 34:59]
  wire [15:0] _T_33 = {_GEN_125,_GEN_124}; // @[registers.scala 34:59]
  wire [15:0] _T_34 = {_GEN_127,_GEN_126}; // @[registers.scala 34:59]
  wire [7:0] _GEN_132 = 5'h1 == io_read_in_rt_addr ? gprs_1_0 : gprs_0_0; // @[registers.scala 35:59]
  wire [7:0] _GEN_133 = 5'h1 == io_read_in_rt_addr ? gprs_1_1 : gprs_0_1; // @[registers.scala 35:59]
  wire [7:0] _GEN_134 = 5'h1 == io_read_in_rt_addr ? gprs_1_2 : gprs_0_2; // @[registers.scala 35:59]
  wire [7:0] _GEN_135 = 5'h1 == io_read_in_rt_addr ? gprs_1_3 : gprs_0_3; // @[registers.scala 35:59]
  wire [7:0] _GEN_136 = 5'h2 == io_read_in_rt_addr ? gprs_2_0 : _GEN_132; // @[registers.scala 35:59]
  wire [7:0] _GEN_137 = 5'h2 == io_read_in_rt_addr ? gprs_2_1 : _GEN_133; // @[registers.scala 35:59]
  wire [7:0] _GEN_138 = 5'h2 == io_read_in_rt_addr ? gprs_2_2 : _GEN_134; // @[registers.scala 35:59]
  wire [7:0] _GEN_139 = 5'h2 == io_read_in_rt_addr ? gprs_2_3 : _GEN_135; // @[registers.scala 35:59]
  wire [7:0] _GEN_140 = 5'h3 == io_read_in_rt_addr ? gprs_3_0 : _GEN_136; // @[registers.scala 35:59]
  wire [7:0] _GEN_141 = 5'h3 == io_read_in_rt_addr ? gprs_3_1 : _GEN_137; // @[registers.scala 35:59]
  wire [7:0] _GEN_142 = 5'h3 == io_read_in_rt_addr ? gprs_3_2 : _GEN_138; // @[registers.scala 35:59]
  wire [7:0] _GEN_143 = 5'h3 == io_read_in_rt_addr ? gprs_3_3 : _GEN_139; // @[registers.scala 35:59]
  wire [7:0] _GEN_144 = 5'h4 == io_read_in_rt_addr ? gprs_4_0 : _GEN_140; // @[registers.scala 35:59]
  wire [7:0] _GEN_145 = 5'h4 == io_read_in_rt_addr ? gprs_4_1 : _GEN_141; // @[registers.scala 35:59]
  wire [7:0] _GEN_146 = 5'h4 == io_read_in_rt_addr ? gprs_4_2 : _GEN_142; // @[registers.scala 35:59]
  wire [7:0] _GEN_147 = 5'h4 == io_read_in_rt_addr ? gprs_4_3 : _GEN_143; // @[registers.scala 35:59]
  wire [7:0] _GEN_148 = 5'h5 == io_read_in_rt_addr ? gprs_5_0 : _GEN_144; // @[registers.scala 35:59]
  wire [7:0] _GEN_149 = 5'h5 == io_read_in_rt_addr ? gprs_5_1 : _GEN_145; // @[registers.scala 35:59]
  wire [7:0] _GEN_150 = 5'h5 == io_read_in_rt_addr ? gprs_5_2 : _GEN_146; // @[registers.scala 35:59]
  wire [7:0] _GEN_151 = 5'h5 == io_read_in_rt_addr ? gprs_5_3 : _GEN_147; // @[registers.scala 35:59]
  wire [7:0] _GEN_152 = 5'h6 == io_read_in_rt_addr ? gprs_6_0 : _GEN_148; // @[registers.scala 35:59]
  wire [7:0] _GEN_153 = 5'h6 == io_read_in_rt_addr ? gprs_6_1 : _GEN_149; // @[registers.scala 35:59]
  wire [7:0] _GEN_154 = 5'h6 == io_read_in_rt_addr ? gprs_6_2 : _GEN_150; // @[registers.scala 35:59]
  wire [7:0] _GEN_155 = 5'h6 == io_read_in_rt_addr ? gprs_6_3 : _GEN_151; // @[registers.scala 35:59]
  wire [7:0] _GEN_156 = 5'h7 == io_read_in_rt_addr ? gprs_7_0 : _GEN_152; // @[registers.scala 35:59]
  wire [7:0] _GEN_157 = 5'h7 == io_read_in_rt_addr ? gprs_7_1 : _GEN_153; // @[registers.scala 35:59]
  wire [7:0] _GEN_158 = 5'h7 == io_read_in_rt_addr ? gprs_7_2 : _GEN_154; // @[registers.scala 35:59]
  wire [7:0] _GEN_159 = 5'h7 == io_read_in_rt_addr ? gprs_7_3 : _GEN_155; // @[registers.scala 35:59]
  wire [7:0] _GEN_160 = 5'h8 == io_read_in_rt_addr ? gprs_8_0 : _GEN_156; // @[registers.scala 35:59]
  wire [7:0] _GEN_161 = 5'h8 == io_read_in_rt_addr ? gprs_8_1 : _GEN_157; // @[registers.scala 35:59]
  wire [7:0] _GEN_162 = 5'h8 == io_read_in_rt_addr ? gprs_8_2 : _GEN_158; // @[registers.scala 35:59]
  wire [7:0] _GEN_163 = 5'h8 == io_read_in_rt_addr ? gprs_8_3 : _GEN_159; // @[registers.scala 35:59]
  wire [7:0] _GEN_164 = 5'h9 == io_read_in_rt_addr ? gprs_9_0 : _GEN_160; // @[registers.scala 35:59]
  wire [7:0] _GEN_165 = 5'h9 == io_read_in_rt_addr ? gprs_9_1 : _GEN_161; // @[registers.scala 35:59]
  wire [7:0] _GEN_166 = 5'h9 == io_read_in_rt_addr ? gprs_9_2 : _GEN_162; // @[registers.scala 35:59]
  wire [7:0] _GEN_167 = 5'h9 == io_read_in_rt_addr ? gprs_9_3 : _GEN_163; // @[registers.scala 35:59]
  wire [7:0] _GEN_168 = 5'ha == io_read_in_rt_addr ? gprs_10_0 : _GEN_164; // @[registers.scala 35:59]
  wire [7:0] _GEN_169 = 5'ha == io_read_in_rt_addr ? gprs_10_1 : _GEN_165; // @[registers.scala 35:59]
  wire [7:0] _GEN_170 = 5'ha == io_read_in_rt_addr ? gprs_10_2 : _GEN_166; // @[registers.scala 35:59]
  wire [7:0] _GEN_171 = 5'ha == io_read_in_rt_addr ? gprs_10_3 : _GEN_167; // @[registers.scala 35:59]
  wire [7:0] _GEN_172 = 5'hb == io_read_in_rt_addr ? gprs_11_0 : _GEN_168; // @[registers.scala 35:59]
  wire [7:0] _GEN_173 = 5'hb == io_read_in_rt_addr ? gprs_11_1 : _GEN_169; // @[registers.scala 35:59]
  wire [7:0] _GEN_174 = 5'hb == io_read_in_rt_addr ? gprs_11_2 : _GEN_170; // @[registers.scala 35:59]
  wire [7:0] _GEN_175 = 5'hb == io_read_in_rt_addr ? gprs_11_3 : _GEN_171; // @[registers.scala 35:59]
  wire [7:0] _GEN_176 = 5'hc == io_read_in_rt_addr ? gprs_12_0 : _GEN_172; // @[registers.scala 35:59]
  wire [7:0] _GEN_177 = 5'hc == io_read_in_rt_addr ? gprs_12_1 : _GEN_173; // @[registers.scala 35:59]
  wire [7:0] _GEN_178 = 5'hc == io_read_in_rt_addr ? gprs_12_2 : _GEN_174; // @[registers.scala 35:59]
  wire [7:0] _GEN_179 = 5'hc == io_read_in_rt_addr ? gprs_12_3 : _GEN_175; // @[registers.scala 35:59]
  wire [7:0] _GEN_180 = 5'hd == io_read_in_rt_addr ? gprs_13_0 : _GEN_176; // @[registers.scala 35:59]
  wire [7:0] _GEN_181 = 5'hd == io_read_in_rt_addr ? gprs_13_1 : _GEN_177; // @[registers.scala 35:59]
  wire [7:0] _GEN_182 = 5'hd == io_read_in_rt_addr ? gprs_13_2 : _GEN_178; // @[registers.scala 35:59]
  wire [7:0] _GEN_183 = 5'hd == io_read_in_rt_addr ? gprs_13_3 : _GEN_179; // @[registers.scala 35:59]
  wire [7:0] _GEN_184 = 5'he == io_read_in_rt_addr ? gprs_14_0 : _GEN_180; // @[registers.scala 35:59]
  wire [7:0] _GEN_185 = 5'he == io_read_in_rt_addr ? gprs_14_1 : _GEN_181; // @[registers.scala 35:59]
  wire [7:0] _GEN_186 = 5'he == io_read_in_rt_addr ? gprs_14_2 : _GEN_182; // @[registers.scala 35:59]
  wire [7:0] _GEN_187 = 5'he == io_read_in_rt_addr ? gprs_14_3 : _GEN_183; // @[registers.scala 35:59]
  wire [7:0] _GEN_188 = 5'hf == io_read_in_rt_addr ? gprs_15_0 : _GEN_184; // @[registers.scala 35:59]
  wire [7:0] _GEN_189 = 5'hf == io_read_in_rt_addr ? gprs_15_1 : _GEN_185; // @[registers.scala 35:59]
  wire [7:0] _GEN_190 = 5'hf == io_read_in_rt_addr ? gprs_15_2 : _GEN_186; // @[registers.scala 35:59]
  wire [7:0] _GEN_191 = 5'hf == io_read_in_rt_addr ? gprs_15_3 : _GEN_187; // @[registers.scala 35:59]
  wire [7:0] _GEN_192 = 5'h10 == io_read_in_rt_addr ? gprs_16_0 : _GEN_188; // @[registers.scala 35:59]
  wire [7:0] _GEN_193 = 5'h10 == io_read_in_rt_addr ? gprs_16_1 : _GEN_189; // @[registers.scala 35:59]
  wire [7:0] _GEN_194 = 5'h10 == io_read_in_rt_addr ? gprs_16_2 : _GEN_190; // @[registers.scala 35:59]
  wire [7:0] _GEN_195 = 5'h10 == io_read_in_rt_addr ? gprs_16_3 : _GEN_191; // @[registers.scala 35:59]
  wire [7:0] _GEN_196 = 5'h11 == io_read_in_rt_addr ? gprs_17_0 : _GEN_192; // @[registers.scala 35:59]
  wire [7:0] _GEN_197 = 5'h11 == io_read_in_rt_addr ? gprs_17_1 : _GEN_193; // @[registers.scala 35:59]
  wire [7:0] _GEN_198 = 5'h11 == io_read_in_rt_addr ? gprs_17_2 : _GEN_194; // @[registers.scala 35:59]
  wire [7:0] _GEN_199 = 5'h11 == io_read_in_rt_addr ? gprs_17_3 : _GEN_195; // @[registers.scala 35:59]
  wire [7:0] _GEN_200 = 5'h12 == io_read_in_rt_addr ? gprs_18_0 : _GEN_196; // @[registers.scala 35:59]
  wire [7:0] _GEN_201 = 5'h12 == io_read_in_rt_addr ? gprs_18_1 : _GEN_197; // @[registers.scala 35:59]
  wire [7:0] _GEN_202 = 5'h12 == io_read_in_rt_addr ? gprs_18_2 : _GEN_198; // @[registers.scala 35:59]
  wire [7:0] _GEN_203 = 5'h12 == io_read_in_rt_addr ? gprs_18_3 : _GEN_199; // @[registers.scala 35:59]
  wire [7:0] _GEN_204 = 5'h13 == io_read_in_rt_addr ? gprs_19_0 : _GEN_200; // @[registers.scala 35:59]
  wire [7:0] _GEN_205 = 5'h13 == io_read_in_rt_addr ? gprs_19_1 : _GEN_201; // @[registers.scala 35:59]
  wire [7:0] _GEN_206 = 5'h13 == io_read_in_rt_addr ? gprs_19_2 : _GEN_202; // @[registers.scala 35:59]
  wire [7:0] _GEN_207 = 5'h13 == io_read_in_rt_addr ? gprs_19_3 : _GEN_203; // @[registers.scala 35:59]
  wire [7:0] _GEN_208 = 5'h14 == io_read_in_rt_addr ? gprs_20_0 : _GEN_204; // @[registers.scala 35:59]
  wire [7:0] _GEN_209 = 5'h14 == io_read_in_rt_addr ? gprs_20_1 : _GEN_205; // @[registers.scala 35:59]
  wire [7:0] _GEN_210 = 5'h14 == io_read_in_rt_addr ? gprs_20_2 : _GEN_206; // @[registers.scala 35:59]
  wire [7:0] _GEN_211 = 5'h14 == io_read_in_rt_addr ? gprs_20_3 : _GEN_207; // @[registers.scala 35:59]
  wire [7:0] _GEN_212 = 5'h15 == io_read_in_rt_addr ? gprs_21_0 : _GEN_208; // @[registers.scala 35:59]
  wire [7:0] _GEN_213 = 5'h15 == io_read_in_rt_addr ? gprs_21_1 : _GEN_209; // @[registers.scala 35:59]
  wire [7:0] _GEN_214 = 5'h15 == io_read_in_rt_addr ? gprs_21_2 : _GEN_210; // @[registers.scala 35:59]
  wire [7:0] _GEN_215 = 5'h15 == io_read_in_rt_addr ? gprs_21_3 : _GEN_211; // @[registers.scala 35:59]
  wire [7:0] _GEN_216 = 5'h16 == io_read_in_rt_addr ? gprs_22_0 : _GEN_212; // @[registers.scala 35:59]
  wire [7:0] _GEN_217 = 5'h16 == io_read_in_rt_addr ? gprs_22_1 : _GEN_213; // @[registers.scala 35:59]
  wire [7:0] _GEN_218 = 5'h16 == io_read_in_rt_addr ? gprs_22_2 : _GEN_214; // @[registers.scala 35:59]
  wire [7:0] _GEN_219 = 5'h16 == io_read_in_rt_addr ? gprs_22_3 : _GEN_215; // @[registers.scala 35:59]
  wire [7:0] _GEN_220 = 5'h17 == io_read_in_rt_addr ? gprs_23_0 : _GEN_216; // @[registers.scala 35:59]
  wire [7:0] _GEN_221 = 5'h17 == io_read_in_rt_addr ? gprs_23_1 : _GEN_217; // @[registers.scala 35:59]
  wire [7:0] _GEN_222 = 5'h17 == io_read_in_rt_addr ? gprs_23_2 : _GEN_218; // @[registers.scala 35:59]
  wire [7:0] _GEN_223 = 5'h17 == io_read_in_rt_addr ? gprs_23_3 : _GEN_219; // @[registers.scala 35:59]
  wire [7:0] _GEN_224 = 5'h18 == io_read_in_rt_addr ? gprs_24_0 : _GEN_220; // @[registers.scala 35:59]
  wire [7:0] _GEN_225 = 5'h18 == io_read_in_rt_addr ? gprs_24_1 : _GEN_221; // @[registers.scala 35:59]
  wire [7:0] _GEN_226 = 5'h18 == io_read_in_rt_addr ? gprs_24_2 : _GEN_222; // @[registers.scala 35:59]
  wire [7:0] _GEN_227 = 5'h18 == io_read_in_rt_addr ? gprs_24_3 : _GEN_223; // @[registers.scala 35:59]
  wire [7:0] _GEN_228 = 5'h19 == io_read_in_rt_addr ? gprs_25_0 : _GEN_224; // @[registers.scala 35:59]
  wire [7:0] _GEN_229 = 5'h19 == io_read_in_rt_addr ? gprs_25_1 : _GEN_225; // @[registers.scala 35:59]
  wire [7:0] _GEN_230 = 5'h19 == io_read_in_rt_addr ? gprs_25_2 : _GEN_226; // @[registers.scala 35:59]
  wire [7:0] _GEN_231 = 5'h19 == io_read_in_rt_addr ? gprs_25_3 : _GEN_227; // @[registers.scala 35:59]
  wire [7:0] _GEN_232 = 5'h1a == io_read_in_rt_addr ? gprs_26_0 : _GEN_228; // @[registers.scala 35:59]
  wire [7:0] _GEN_233 = 5'h1a == io_read_in_rt_addr ? gprs_26_1 : _GEN_229; // @[registers.scala 35:59]
  wire [7:0] _GEN_234 = 5'h1a == io_read_in_rt_addr ? gprs_26_2 : _GEN_230; // @[registers.scala 35:59]
  wire [7:0] _GEN_235 = 5'h1a == io_read_in_rt_addr ? gprs_26_3 : _GEN_231; // @[registers.scala 35:59]
  wire [7:0] _GEN_236 = 5'h1b == io_read_in_rt_addr ? gprs_27_0 : _GEN_232; // @[registers.scala 35:59]
  wire [7:0] _GEN_237 = 5'h1b == io_read_in_rt_addr ? gprs_27_1 : _GEN_233; // @[registers.scala 35:59]
  wire [7:0] _GEN_238 = 5'h1b == io_read_in_rt_addr ? gprs_27_2 : _GEN_234; // @[registers.scala 35:59]
  wire [7:0] _GEN_239 = 5'h1b == io_read_in_rt_addr ? gprs_27_3 : _GEN_235; // @[registers.scala 35:59]
  wire [7:0] _GEN_240 = 5'h1c == io_read_in_rt_addr ? gprs_28_0 : _GEN_236; // @[registers.scala 35:59]
  wire [7:0] _GEN_241 = 5'h1c == io_read_in_rt_addr ? gprs_28_1 : _GEN_237; // @[registers.scala 35:59]
  wire [7:0] _GEN_242 = 5'h1c == io_read_in_rt_addr ? gprs_28_2 : _GEN_238; // @[registers.scala 35:59]
  wire [7:0] _GEN_243 = 5'h1c == io_read_in_rt_addr ? gprs_28_3 : _GEN_239; // @[registers.scala 35:59]
  wire [7:0] _GEN_244 = 5'h1d == io_read_in_rt_addr ? gprs_29_0 : _GEN_240; // @[registers.scala 35:59]
  wire [7:0] _GEN_245 = 5'h1d == io_read_in_rt_addr ? gprs_29_1 : _GEN_241; // @[registers.scala 35:59]
  wire [7:0] _GEN_246 = 5'h1d == io_read_in_rt_addr ? gprs_29_2 : _GEN_242; // @[registers.scala 35:59]
  wire [7:0] _GEN_247 = 5'h1d == io_read_in_rt_addr ? gprs_29_3 : _GEN_243; // @[registers.scala 35:59]
  wire [7:0] _GEN_248 = 5'h1e == io_read_in_rt_addr ? gprs_30_0 : _GEN_244; // @[registers.scala 35:59]
  wire [7:0] _GEN_249 = 5'h1e == io_read_in_rt_addr ? gprs_30_1 : _GEN_245; // @[registers.scala 35:59]
  wire [7:0] _GEN_250 = 5'h1e == io_read_in_rt_addr ? gprs_30_2 : _GEN_246; // @[registers.scala 35:59]
  wire [7:0] _GEN_251 = 5'h1e == io_read_in_rt_addr ? gprs_30_3 : _GEN_247; // @[registers.scala 35:59]
  wire [7:0] _GEN_252 = 5'h1f == io_read_in_rt_addr ? gprs_31_0 : _GEN_248; // @[registers.scala 35:59]
  wire [7:0] _GEN_253 = 5'h1f == io_read_in_rt_addr ? gprs_31_1 : _GEN_249; // @[registers.scala 35:59]
  wire [7:0] _GEN_254 = 5'h1f == io_read_in_rt_addr ? gprs_31_2 : _GEN_250; // @[registers.scala 35:59]
  wire [7:0] _GEN_255 = 5'h1f == io_read_in_rt_addr ? gprs_31_3 : _GEN_251; // @[registers.scala 35:59]
  wire [15:0] _T_36 = {_GEN_253,_GEN_252}; // @[registers.scala 35:59]
  wire [15:0] _T_37 = {_GEN_255,_GEN_254}; // @[registers.scala 35:59]
  wire  _T_39 = io_write_in_w_en != 4'h0; // @[registers.scala 37:36]
  wire  _T_40 = io_write_in_addr != 5'h0; // @[registers.scala 37:63]
  wire  _T_41 = _T_39 & _T_40; // @[registers.scala 37:44]
  wire [15:0] _T_50 = {gprs_0_1,gprs_0_0}; // @[registers.scala 53:43]
  wire [15:0] _T_51 = {gprs_0_3,gprs_0_2}; // @[registers.scala 53:43]
  wire [15:0] _T_53 = {gprs_1_1,gprs_1_0}; // @[registers.scala 53:43]
  wire [15:0] _T_54 = {gprs_1_3,gprs_1_2}; // @[registers.scala 53:43]
  wire [15:0] _T_56 = {gprs_2_1,gprs_2_0}; // @[registers.scala 53:43]
  wire [15:0] _T_57 = {gprs_2_3,gprs_2_2}; // @[registers.scala 53:43]
  wire [15:0] _T_59 = {gprs_3_1,gprs_3_0}; // @[registers.scala 53:43]
  wire [15:0] _T_60 = {gprs_3_3,gprs_3_2}; // @[registers.scala 53:43]
  wire [15:0] _T_62 = {gprs_4_1,gprs_4_0}; // @[registers.scala 53:43]
  wire [15:0] _T_63 = {gprs_4_3,gprs_4_2}; // @[registers.scala 53:43]
  wire [15:0] _T_65 = {gprs_5_1,gprs_5_0}; // @[registers.scala 53:43]
  wire [15:0] _T_66 = {gprs_5_3,gprs_5_2}; // @[registers.scala 53:43]
  wire [15:0] _T_68 = {gprs_6_1,gprs_6_0}; // @[registers.scala 53:43]
  wire [15:0] _T_69 = {gprs_6_3,gprs_6_2}; // @[registers.scala 53:43]
  wire [15:0] _T_71 = {gprs_7_1,gprs_7_0}; // @[registers.scala 53:43]
  wire [15:0] _T_72 = {gprs_7_3,gprs_7_2}; // @[registers.scala 53:43]
  wire [15:0] _T_74 = {gprs_8_1,gprs_8_0}; // @[registers.scala 53:43]
  wire [15:0] _T_75 = {gprs_8_3,gprs_8_2}; // @[registers.scala 53:43]
  wire [15:0] _T_77 = {gprs_9_1,gprs_9_0}; // @[registers.scala 53:43]
  wire [15:0] _T_78 = {gprs_9_3,gprs_9_2}; // @[registers.scala 53:43]
  wire [15:0] _T_80 = {gprs_10_1,gprs_10_0}; // @[registers.scala 53:43]
  wire [15:0] _T_81 = {gprs_10_3,gprs_10_2}; // @[registers.scala 53:43]
  wire [15:0] _T_83 = {gprs_11_1,gprs_11_0}; // @[registers.scala 53:43]
  wire [15:0] _T_84 = {gprs_11_3,gprs_11_2}; // @[registers.scala 53:43]
  wire [15:0] _T_86 = {gprs_12_1,gprs_12_0}; // @[registers.scala 53:43]
  wire [15:0] _T_87 = {gprs_12_3,gprs_12_2}; // @[registers.scala 53:43]
  wire [15:0] _T_89 = {gprs_13_1,gprs_13_0}; // @[registers.scala 53:43]
  wire [15:0] _T_90 = {gprs_13_3,gprs_13_2}; // @[registers.scala 53:43]
  wire [15:0] _T_92 = {gprs_14_1,gprs_14_0}; // @[registers.scala 53:43]
  wire [15:0] _T_93 = {gprs_14_3,gprs_14_2}; // @[registers.scala 53:43]
  wire [15:0] _T_95 = {gprs_15_1,gprs_15_0}; // @[registers.scala 53:43]
  wire [15:0] _T_96 = {gprs_15_3,gprs_15_2}; // @[registers.scala 53:43]
  wire [15:0] _T_98 = {gprs_16_1,gprs_16_0}; // @[registers.scala 53:43]
  wire [15:0] _T_99 = {gprs_16_3,gprs_16_2}; // @[registers.scala 53:43]
  wire [15:0] _T_101 = {gprs_17_1,gprs_17_0}; // @[registers.scala 53:43]
  wire [15:0] _T_102 = {gprs_17_3,gprs_17_2}; // @[registers.scala 53:43]
  wire [15:0] _T_104 = {gprs_18_1,gprs_18_0}; // @[registers.scala 53:43]
  wire [15:0] _T_105 = {gprs_18_3,gprs_18_2}; // @[registers.scala 53:43]
  wire [15:0] _T_107 = {gprs_19_1,gprs_19_0}; // @[registers.scala 53:43]
  wire [15:0] _T_108 = {gprs_19_3,gprs_19_2}; // @[registers.scala 53:43]
  wire [15:0] _T_110 = {gprs_20_1,gprs_20_0}; // @[registers.scala 53:43]
  wire [15:0] _T_111 = {gprs_20_3,gprs_20_2}; // @[registers.scala 53:43]
  wire [15:0] _T_113 = {gprs_21_1,gprs_21_0}; // @[registers.scala 53:43]
  wire [15:0] _T_114 = {gprs_21_3,gprs_21_2}; // @[registers.scala 53:43]
  wire [15:0] _T_116 = {gprs_22_1,gprs_22_0}; // @[registers.scala 53:43]
  wire [15:0] _T_117 = {gprs_22_3,gprs_22_2}; // @[registers.scala 53:43]
  wire [15:0] _T_119 = {gprs_23_1,gprs_23_0}; // @[registers.scala 53:43]
  wire [15:0] _T_120 = {gprs_23_3,gprs_23_2}; // @[registers.scala 53:43]
  wire [15:0] _T_122 = {gprs_24_1,gprs_24_0}; // @[registers.scala 53:43]
  wire [15:0] _T_123 = {gprs_24_3,gprs_24_2}; // @[registers.scala 53:43]
  wire [15:0] _T_125 = {gprs_25_1,gprs_25_0}; // @[registers.scala 53:43]
  wire [15:0] _T_126 = {gprs_25_3,gprs_25_2}; // @[registers.scala 53:43]
  wire [15:0] _T_128 = {gprs_26_1,gprs_26_0}; // @[registers.scala 53:43]
  wire [15:0] _T_129 = {gprs_26_3,gprs_26_2}; // @[registers.scala 53:43]
  wire [15:0] _T_131 = {gprs_27_1,gprs_27_0}; // @[registers.scala 53:43]
  wire [15:0] _T_132 = {gprs_27_3,gprs_27_2}; // @[registers.scala 53:43]
  wire [15:0] _T_134 = {gprs_28_1,gprs_28_0}; // @[registers.scala 53:43]
  wire [15:0] _T_135 = {gprs_28_3,gprs_28_2}; // @[registers.scala 53:43]
  wire [15:0] _T_137 = {gprs_29_1,gprs_29_0}; // @[registers.scala 53:43]
  wire [15:0] _T_138 = {gprs_29_3,gprs_29_2}; // @[registers.scala 53:43]
  wire [15:0] _T_140 = {gprs_30_1,gprs_30_0}; // @[registers.scala 53:43]
  wire [15:0] _T_141 = {gprs_30_3,gprs_30_2}; // @[registers.scala 53:43]
  wire [15:0] _T_143 = {gprs_31_1,gprs_31_0}; // @[registers.scala 53:43]
  wire [15:0] _T_144 = {gprs_31_3,gprs_31_2}; // @[registers.scala 53:43]
  assign io_read_out_rs_data = {_T_34,_T_33}; // @[registers.scala 34:25]
  assign io_read_out_rt_data = {_T_37,_T_36}; // @[registers.scala 35:25]
  assign io_gpr_commit_0 = {_T_51,_T_50}; // @[registers.scala 53:26]
  assign io_gpr_commit_1 = {_T_54,_T_53}; // @[registers.scala 53:26]
  assign io_gpr_commit_2 = {_T_57,_T_56}; // @[registers.scala 53:26]
  assign io_gpr_commit_3 = {_T_60,_T_59}; // @[registers.scala 53:26]
  assign io_gpr_commit_4 = {_T_63,_T_62}; // @[registers.scala 53:26]
  assign io_gpr_commit_5 = {_T_66,_T_65}; // @[registers.scala 53:26]
  assign io_gpr_commit_6 = {_T_69,_T_68}; // @[registers.scala 53:26]
  assign io_gpr_commit_7 = {_T_72,_T_71}; // @[registers.scala 53:26]
  assign io_gpr_commit_8 = {_T_75,_T_74}; // @[registers.scala 53:26]
  assign io_gpr_commit_9 = {_T_78,_T_77}; // @[registers.scala 53:26]
  assign io_gpr_commit_10 = {_T_81,_T_80}; // @[registers.scala 53:26]
  assign io_gpr_commit_11 = {_T_84,_T_83}; // @[registers.scala 53:26]
  assign io_gpr_commit_12 = {_T_87,_T_86}; // @[registers.scala 53:26]
  assign io_gpr_commit_13 = {_T_90,_T_89}; // @[registers.scala 53:26]
  assign io_gpr_commit_14 = {_T_93,_T_92}; // @[registers.scala 53:26]
  assign io_gpr_commit_15 = {_T_96,_T_95}; // @[registers.scala 53:26]
  assign io_gpr_commit_16 = {_T_99,_T_98}; // @[registers.scala 53:26]
  assign io_gpr_commit_17 = {_T_102,_T_101}; // @[registers.scala 53:26]
  assign io_gpr_commit_18 = {_T_105,_T_104}; // @[registers.scala 53:26]
  assign io_gpr_commit_19 = {_T_108,_T_107}; // @[registers.scala 53:26]
  assign io_gpr_commit_20 = {_T_111,_T_110}; // @[registers.scala 53:26]
  assign io_gpr_commit_21 = {_T_114,_T_113}; // @[registers.scala 53:26]
  assign io_gpr_commit_22 = {_T_117,_T_116}; // @[registers.scala 53:26]
  assign io_gpr_commit_23 = {_T_120,_T_119}; // @[registers.scala 53:26]
  assign io_gpr_commit_24 = {_T_123,_T_122}; // @[registers.scala 53:26]
  assign io_gpr_commit_25 = {_T_126,_T_125}; // @[registers.scala 53:26]
  assign io_gpr_commit_26 = {_T_129,_T_128}; // @[registers.scala 53:26]
  assign io_gpr_commit_27 = {_T_132,_T_131}; // @[registers.scala 53:26]
  assign io_gpr_commit_28 = {_T_135,_T_134}; // @[registers.scala 53:26]
  assign io_gpr_commit_29 = {_T_138,_T_137}; // @[registers.scala 53:26]
  assign io_gpr_commit_30 = {_T_141,_T_140}; // @[registers.scala 53:26]
  assign io_gpr_commit_31 = {_T_144,_T_143}; // @[registers.scala 53:26]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  gprs_0_0 = _RAND_0[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  gprs_0_1 = _RAND_1[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  gprs_0_2 = _RAND_2[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  gprs_0_3 = _RAND_3[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  gprs_1_0 = _RAND_4[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  gprs_1_1 = _RAND_5[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  gprs_1_2 = _RAND_6[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  gprs_1_3 = _RAND_7[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{`RANDOM}};
  gprs_2_0 = _RAND_8[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{`RANDOM}};
  gprs_2_1 = _RAND_9[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_10 = {1{`RANDOM}};
  gprs_2_2 = _RAND_10[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_11 = {1{`RANDOM}};
  gprs_2_3 = _RAND_11[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_12 = {1{`RANDOM}};
  gprs_3_0 = _RAND_12[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_13 = {1{`RANDOM}};
  gprs_3_1 = _RAND_13[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_14 = {1{`RANDOM}};
  gprs_3_2 = _RAND_14[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_15 = {1{`RANDOM}};
  gprs_3_3 = _RAND_15[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_16 = {1{`RANDOM}};
  gprs_4_0 = _RAND_16[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_17 = {1{`RANDOM}};
  gprs_4_1 = _RAND_17[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_18 = {1{`RANDOM}};
  gprs_4_2 = _RAND_18[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_19 = {1{`RANDOM}};
  gprs_4_3 = _RAND_19[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_20 = {1{`RANDOM}};
  gprs_5_0 = _RAND_20[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_21 = {1{`RANDOM}};
  gprs_5_1 = _RAND_21[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_22 = {1{`RANDOM}};
  gprs_5_2 = _RAND_22[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_23 = {1{`RANDOM}};
  gprs_5_3 = _RAND_23[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_24 = {1{`RANDOM}};
  gprs_6_0 = _RAND_24[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_25 = {1{`RANDOM}};
  gprs_6_1 = _RAND_25[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_26 = {1{`RANDOM}};
  gprs_6_2 = _RAND_26[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_27 = {1{`RANDOM}};
  gprs_6_3 = _RAND_27[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_28 = {1{`RANDOM}};
  gprs_7_0 = _RAND_28[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_29 = {1{`RANDOM}};
  gprs_7_1 = _RAND_29[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_30 = {1{`RANDOM}};
  gprs_7_2 = _RAND_30[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_31 = {1{`RANDOM}};
  gprs_7_3 = _RAND_31[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_32 = {1{`RANDOM}};
  gprs_8_0 = _RAND_32[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_33 = {1{`RANDOM}};
  gprs_8_1 = _RAND_33[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_34 = {1{`RANDOM}};
  gprs_8_2 = _RAND_34[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_35 = {1{`RANDOM}};
  gprs_8_3 = _RAND_35[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_36 = {1{`RANDOM}};
  gprs_9_0 = _RAND_36[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_37 = {1{`RANDOM}};
  gprs_9_1 = _RAND_37[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_38 = {1{`RANDOM}};
  gprs_9_2 = _RAND_38[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_39 = {1{`RANDOM}};
  gprs_9_3 = _RAND_39[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_40 = {1{`RANDOM}};
  gprs_10_0 = _RAND_40[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_41 = {1{`RANDOM}};
  gprs_10_1 = _RAND_41[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_42 = {1{`RANDOM}};
  gprs_10_2 = _RAND_42[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_43 = {1{`RANDOM}};
  gprs_10_3 = _RAND_43[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_44 = {1{`RANDOM}};
  gprs_11_0 = _RAND_44[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_45 = {1{`RANDOM}};
  gprs_11_1 = _RAND_45[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_46 = {1{`RANDOM}};
  gprs_11_2 = _RAND_46[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_47 = {1{`RANDOM}};
  gprs_11_3 = _RAND_47[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_48 = {1{`RANDOM}};
  gprs_12_0 = _RAND_48[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_49 = {1{`RANDOM}};
  gprs_12_1 = _RAND_49[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_50 = {1{`RANDOM}};
  gprs_12_2 = _RAND_50[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_51 = {1{`RANDOM}};
  gprs_12_3 = _RAND_51[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_52 = {1{`RANDOM}};
  gprs_13_0 = _RAND_52[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_53 = {1{`RANDOM}};
  gprs_13_1 = _RAND_53[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_54 = {1{`RANDOM}};
  gprs_13_2 = _RAND_54[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_55 = {1{`RANDOM}};
  gprs_13_3 = _RAND_55[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_56 = {1{`RANDOM}};
  gprs_14_0 = _RAND_56[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_57 = {1{`RANDOM}};
  gprs_14_1 = _RAND_57[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_58 = {1{`RANDOM}};
  gprs_14_2 = _RAND_58[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_59 = {1{`RANDOM}};
  gprs_14_3 = _RAND_59[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_60 = {1{`RANDOM}};
  gprs_15_0 = _RAND_60[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_61 = {1{`RANDOM}};
  gprs_15_1 = _RAND_61[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_62 = {1{`RANDOM}};
  gprs_15_2 = _RAND_62[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_63 = {1{`RANDOM}};
  gprs_15_3 = _RAND_63[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_64 = {1{`RANDOM}};
  gprs_16_0 = _RAND_64[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_65 = {1{`RANDOM}};
  gprs_16_1 = _RAND_65[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_66 = {1{`RANDOM}};
  gprs_16_2 = _RAND_66[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_67 = {1{`RANDOM}};
  gprs_16_3 = _RAND_67[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_68 = {1{`RANDOM}};
  gprs_17_0 = _RAND_68[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_69 = {1{`RANDOM}};
  gprs_17_1 = _RAND_69[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_70 = {1{`RANDOM}};
  gprs_17_2 = _RAND_70[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_71 = {1{`RANDOM}};
  gprs_17_3 = _RAND_71[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_72 = {1{`RANDOM}};
  gprs_18_0 = _RAND_72[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_73 = {1{`RANDOM}};
  gprs_18_1 = _RAND_73[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_74 = {1{`RANDOM}};
  gprs_18_2 = _RAND_74[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_75 = {1{`RANDOM}};
  gprs_18_3 = _RAND_75[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_76 = {1{`RANDOM}};
  gprs_19_0 = _RAND_76[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_77 = {1{`RANDOM}};
  gprs_19_1 = _RAND_77[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_78 = {1{`RANDOM}};
  gprs_19_2 = _RAND_78[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_79 = {1{`RANDOM}};
  gprs_19_3 = _RAND_79[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_80 = {1{`RANDOM}};
  gprs_20_0 = _RAND_80[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_81 = {1{`RANDOM}};
  gprs_20_1 = _RAND_81[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_82 = {1{`RANDOM}};
  gprs_20_2 = _RAND_82[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_83 = {1{`RANDOM}};
  gprs_20_3 = _RAND_83[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_84 = {1{`RANDOM}};
  gprs_21_0 = _RAND_84[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_85 = {1{`RANDOM}};
  gprs_21_1 = _RAND_85[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_86 = {1{`RANDOM}};
  gprs_21_2 = _RAND_86[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_87 = {1{`RANDOM}};
  gprs_21_3 = _RAND_87[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_88 = {1{`RANDOM}};
  gprs_22_0 = _RAND_88[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_89 = {1{`RANDOM}};
  gprs_22_1 = _RAND_89[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_90 = {1{`RANDOM}};
  gprs_22_2 = _RAND_90[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_91 = {1{`RANDOM}};
  gprs_22_3 = _RAND_91[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_92 = {1{`RANDOM}};
  gprs_23_0 = _RAND_92[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_93 = {1{`RANDOM}};
  gprs_23_1 = _RAND_93[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_94 = {1{`RANDOM}};
  gprs_23_2 = _RAND_94[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_95 = {1{`RANDOM}};
  gprs_23_3 = _RAND_95[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_96 = {1{`RANDOM}};
  gprs_24_0 = _RAND_96[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_97 = {1{`RANDOM}};
  gprs_24_1 = _RAND_97[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_98 = {1{`RANDOM}};
  gprs_24_2 = _RAND_98[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_99 = {1{`RANDOM}};
  gprs_24_3 = _RAND_99[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_100 = {1{`RANDOM}};
  gprs_25_0 = _RAND_100[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_101 = {1{`RANDOM}};
  gprs_25_1 = _RAND_101[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_102 = {1{`RANDOM}};
  gprs_25_2 = _RAND_102[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_103 = {1{`RANDOM}};
  gprs_25_3 = _RAND_103[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_104 = {1{`RANDOM}};
  gprs_26_0 = _RAND_104[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_105 = {1{`RANDOM}};
  gprs_26_1 = _RAND_105[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_106 = {1{`RANDOM}};
  gprs_26_2 = _RAND_106[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_107 = {1{`RANDOM}};
  gprs_26_3 = _RAND_107[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_108 = {1{`RANDOM}};
  gprs_27_0 = _RAND_108[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_109 = {1{`RANDOM}};
  gprs_27_1 = _RAND_109[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_110 = {1{`RANDOM}};
  gprs_27_2 = _RAND_110[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_111 = {1{`RANDOM}};
  gprs_27_3 = _RAND_111[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_112 = {1{`RANDOM}};
  gprs_28_0 = _RAND_112[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_113 = {1{`RANDOM}};
  gprs_28_1 = _RAND_113[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_114 = {1{`RANDOM}};
  gprs_28_2 = _RAND_114[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_115 = {1{`RANDOM}};
  gprs_28_3 = _RAND_115[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_116 = {1{`RANDOM}};
  gprs_29_0 = _RAND_116[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_117 = {1{`RANDOM}};
  gprs_29_1 = _RAND_117[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_118 = {1{`RANDOM}};
  gprs_29_2 = _RAND_118[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_119 = {1{`RANDOM}};
  gprs_29_3 = _RAND_119[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_120 = {1{`RANDOM}};
  gprs_30_0 = _RAND_120[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_121 = {1{`RANDOM}};
  gprs_30_1 = _RAND_121[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_122 = {1{`RANDOM}};
  gprs_30_2 = _RAND_122[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_123 = {1{`RANDOM}};
  gprs_30_3 = _RAND_123[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_124 = {1{`RANDOM}};
  gprs_31_0 = _RAND_124[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_125 = {1{`RANDOM}};
  gprs_31_1 = _RAND_125[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_126 = {1{`RANDOM}};
  gprs_31_2 = _RAND_126[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_127 = {1{`RANDOM}};
  gprs_31_3 = _RAND_127[7:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      gprs_0_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h0 == io_write_in_addr) begin
          gprs_0_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_0_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h0 == io_write_in_addr) begin
          gprs_0_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_0_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h0 == io_write_in_addr) begin
          gprs_0_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_0_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h0 == io_write_in_addr) begin
          gprs_0_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_1_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1 == io_write_in_addr) begin
          gprs_1_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_1_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1 == io_write_in_addr) begin
          gprs_1_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_1_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1 == io_write_in_addr) begin
          gprs_1_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_1_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1 == io_write_in_addr) begin
          gprs_1_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_2_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h2 == io_write_in_addr) begin
          gprs_2_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_2_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h2 == io_write_in_addr) begin
          gprs_2_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_2_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h2 == io_write_in_addr) begin
          gprs_2_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_2_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h2 == io_write_in_addr) begin
          gprs_2_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_3_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h3 == io_write_in_addr) begin
          gprs_3_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_3_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h3 == io_write_in_addr) begin
          gprs_3_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_3_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h3 == io_write_in_addr) begin
          gprs_3_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_3_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h3 == io_write_in_addr) begin
          gprs_3_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_4_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h4 == io_write_in_addr) begin
          gprs_4_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_4_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h4 == io_write_in_addr) begin
          gprs_4_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_4_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h4 == io_write_in_addr) begin
          gprs_4_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_4_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h4 == io_write_in_addr) begin
          gprs_4_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_5_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h5 == io_write_in_addr) begin
          gprs_5_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_5_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h5 == io_write_in_addr) begin
          gprs_5_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_5_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h5 == io_write_in_addr) begin
          gprs_5_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_5_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h5 == io_write_in_addr) begin
          gprs_5_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_6_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h6 == io_write_in_addr) begin
          gprs_6_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_6_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h6 == io_write_in_addr) begin
          gprs_6_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_6_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h6 == io_write_in_addr) begin
          gprs_6_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_6_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h6 == io_write_in_addr) begin
          gprs_6_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_7_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h7 == io_write_in_addr) begin
          gprs_7_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_7_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h7 == io_write_in_addr) begin
          gprs_7_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_7_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h7 == io_write_in_addr) begin
          gprs_7_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_7_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h7 == io_write_in_addr) begin
          gprs_7_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_8_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h8 == io_write_in_addr) begin
          gprs_8_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_8_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h8 == io_write_in_addr) begin
          gprs_8_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_8_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h8 == io_write_in_addr) begin
          gprs_8_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_8_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h8 == io_write_in_addr) begin
          gprs_8_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_9_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h9 == io_write_in_addr) begin
          gprs_9_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_9_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h9 == io_write_in_addr) begin
          gprs_9_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_9_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h9 == io_write_in_addr) begin
          gprs_9_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_9_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h9 == io_write_in_addr) begin
          gprs_9_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_10_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'ha == io_write_in_addr) begin
          gprs_10_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_10_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'ha == io_write_in_addr) begin
          gprs_10_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_10_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'ha == io_write_in_addr) begin
          gprs_10_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_10_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'ha == io_write_in_addr) begin
          gprs_10_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_11_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'hb == io_write_in_addr) begin
          gprs_11_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_11_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'hb == io_write_in_addr) begin
          gprs_11_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_11_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'hb == io_write_in_addr) begin
          gprs_11_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_11_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'hb == io_write_in_addr) begin
          gprs_11_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_12_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'hc == io_write_in_addr) begin
          gprs_12_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_12_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'hc == io_write_in_addr) begin
          gprs_12_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_12_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'hc == io_write_in_addr) begin
          gprs_12_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_12_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'hc == io_write_in_addr) begin
          gprs_12_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_13_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'hd == io_write_in_addr) begin
          gprs_13_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_13_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'hd == io_write_in_addr) begin
          gprs_13_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_13_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'hd == io_write_in_addr) begin
          gprs_13_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_13_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'hd == io_write_in_addr) begin
          gprs_13_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_14_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'he == io_write_in_addr) begin
          gprs_14_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_14_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'he == io_write_in_addr) begin
          gprs_14_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_14_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'he == io_write_in_addr) begin
          gprs_14_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_14_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'he == io_write_in_addr) begin
          gprs_14_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_15_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'hf == io_write_in_addr) begin
          gprs_15_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_15_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'hf == io_write_in_addr) begin
          gprs_15_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_15_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'hf == io_write_in_addr) begin
          gprs_15_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_15_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'hf == io_write_in_addr) begin
          gprs_15_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_16_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h10 == io_write_in_addr) begin
          gprs_16_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_16_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h10 == io_write_in_addr) begin
          gprs_16_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_16_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h10 == io_write_in_addr) begin
          gprs_16_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_16_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h10 == io_write_in_addr) begin
          gprs_16_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_17_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h11 == io_write_in_addr) begin
          gprs_17_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_17_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h11 == io_write_in_addr) begin
          gprs_17_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_17_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h11 == io_write_in_addr) begin
          gprs_17_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_17_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h11 == io_write_in_addr) begin
          gprs_17_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_18_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h12 == io_write_in_addr) begin
          gprs_18_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_18_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h12 == io_write_in_addr) begin
          gprs_18_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_18_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h12 == io_write_in_addr) begin
          gprs_18_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_18_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h12 == io_write_in_addr) begin
          gprs_18_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_19_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h13 == io_write_in_addr) begin
          gprs_19_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_19_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h13 == io_write_in_addr) begin
          gprs_19_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_19_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h13 == io_write_in_addr) begin
          gprs_19_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_19_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h13 == io_write_in_addr) begin
          gprs_19_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_20_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h14 == io_write_in_addr) begin
          gprs_20_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_20_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h14 == io_write_in_addr) begin
          gprs_20_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_20_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h14 == io_write_in_addr) begin
          gprs_20_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_20_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h14 == io_write_in_addr) begin
          gprs_20_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_21_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h15 == io_write_in_addr) begin
          gprs_21_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_21_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h15 == io_write_in_addr) begin
          gprs_21_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_21_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h15 == io_write_in_addr) begin
          gprs_21_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_21_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h15 == io_write_in_addr) begin
          gprs_21_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_22_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h16 == io_write_in_addr) begin
          gprs_22_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_22_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h16 == io_write_in_addr) begin
          gprs_22_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_22_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h16 == io_write_in_addr) begin
          gprs_22_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_22_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h16 == io_write_in_addr) begin
          gprs_22_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_23_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h17 == io_write_in_addr) begin
          gprs_23_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_23_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h17 == io_write_in_addr) begin
          gprs_23_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_23_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h17 == io_write_in_addr) begin
          gprs_23_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_23_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h17 == io_write_in_addr) begin
          gprs_23_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_24_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h18 == io_write_in_addr) begin
          gprs_24_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_24_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h18 == io_write_in_addr) begin
          gprs_24_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_24_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h18 == io_write_in_addr) begin
          gprs_24_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_24_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h18 == io_write_in_addr) begin
          gprs_24_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_25_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h19 == io_write_in_addr) begin
          gprs_25_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_25_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h19 == io_write_in_addr) begin
          gprs_25_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_25_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h19 == io_write_in_addr) begin
          gprs_25_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_25_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h19 == io_write_in_addr) begin
          gprs_25_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_26_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1a == io_write_in_addr) begin
          gprs_26_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_26_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1a == io_write_in_addr) begin
          gprs_26_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_26_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1a == io_write_in_addr) begin
          gprs_26_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_26_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1a == io_write_in_addr) begin
          gprs_26_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_27_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1b == io_write_in_addr) begin
          gprs_27_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_27_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1b == io_write_in_addr) begin
          gprs_27_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_27_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1b == io_write_in_addr) begin
          gprs_27_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_27_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1b == io_write_in_addr) begin
          gprs_27_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_28_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1c == io_write_in_addr) begin
          gprs_28_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_28_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1c == io_write_in_addr) begin
          gprs_28_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_28_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1c == io_write_in_addr) begin
          gprs_28_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_28_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1c == io_write_in_addr) begin
          gprs_28_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_29_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1d == io_write_in_addr) begin
          gprs_29_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_29_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1d == io_write_in_addr) begin
          gprs_29_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_29_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1d == io_write_in_addr) begin
          gprs_29_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_29_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1d == io_write_in_addr) begin
          gprs_29_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_30_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1e == io_write_in_addr) begin
          gprs_30_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_30_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1e == io_write_in_addr) begin
          gprs_30_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_30_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1e == io_write_in_addr) begin
          gprs_30_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_30_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1e == io_write_in_addr) begin
          gprs_30_3 <= io_write_in_data[31:24];
        end
      end
    end
    if (reset) begin
      gprs_31_0 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[0]) begin
        if (5'h1f == io_write_in_addr) begin
          gprs_31_0 <= io_write_in_data[7:0];
        end
      end
    end
    if (reset) begin
      gprs_31_1 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[1]) begin
        if (5'h1f == io_write_in_addr) begin
          gprs_31_1 <= io_write_in_data[15:8];
        end
      end
    end
    if (reset) begin
      gprs_31_2 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[2]) begin
        if (5'h1f == io_write_in_addr) begin
          gprs_31_2 <= io_write_in_data[23:16];
        end
      end
    end
    if (reset) begin
      gprs_31_3 <= 8'h0;
    end else if (_T_41) begin
      if (io_write_in_w_en[3]) begin
        if (5'h1f == io_write_in_addr) begin
          gprs_31_3 <= io_write_in_data[31:24];
        end
      end
    end
  end
endmodule
module InstrFetch(
  input         clock,
  input         reset,
  output        io_wb_if_ready,
  input         io_wb_if_valid,
  output [31:0] io_pc,
  output        io_if_id_valid,
  output [31:0] io_if_id_bits_instr
);
  wire  dev_clock; // @[InstrFetch.scala 25:21]
  wire  dev_reset; // @[InstrFetch.scala 25:21]
  wire  dev_in_req_ready; // @[InstrFetch.scala 25:21]
  wire  dev_in_req_valid; // @[InstrFetch.scala 25:21]
  wire  dev_in_req_bits_is_cached; // @[InstrFetch.scala 25:21]
  wire [31:0] dev_in_req_bits_addr; // @[InstrFetch.scala 25:21]
  wire [1:0] dev_in_req_bits_len; // @[InstrFetch.scala 25:21]
  wire [3:0] dev_in_req_bits_strb; // @[InstrFetch.scala 25:21]
  wire [31:0] dev_in_req_bits_data; // @[InstrFetch.scala 25:21]
  wire  dev_in_req_bits_func; // @[InstrFetch.scala 25:21]
  wire  dev_in_resp_ready; // @[InstrFetch.scala 25:21]
  wire  dev_in_resp_valid; // @[InstrFetch.scala 25:21]
  wire [31:0] dev_in_resp_bits_data; // @[InstrFetch.scala 25:21]
  reg [31:0] pc_reg; // @[InstrFetch.scala 16:25]
  reg [31:0] _RAND_0;
  reg  wb_if_fire; // @[InstrFetch.scala 22:29]
  reg [31:0] _RAND_1;
  wire [31:0] _T_6 = pc_reg + 32'h4; // @[InstrFetch.scala 39:25]
  SimDev dev ( // @[InstrFetch.scala 25:21]
    .clock(dev_clock),
    .reset(dev_reset),
    .in_req_ready(dev_in_req_ready),
    .in_req_valid(dev_in_req_valid),
    .in_req_bits_is_cached(dev_in_req_bits_is_cached),
    .in_req_bits_addr(dev_in_req_bits_addr),
    .in_req_bits_len(dev_in_req_bits_len),
    .in_req_bits_strb(dev_in_req_bits_strb),
    .in_req_bits_data(dev_in_req_bits_data),
    .in_req_bits_func(dev_in_req_bits_func),
    .in_resp_ready(dev_in_resp_ready),
    .in_resp_valid(dev_in_resp_valid),
    .in_resp_bits_data(dev_in_resp_bits_data)
  );
  assign io_wb_if_ready = 1'h1; // @[InstrFetch.scala 14:20]
  assign io_pc = pc_reg; // @[InstrFetch.scala 17:11]
  assign io_if_id_valid = wb_if_fire; // @[InstrFetch.scala 43:20]
  assign io_if_id_bits_instr = dev_in_resp_bits_data; // @[InstrFetch.scala 42:25]
  assign dev_clock = clock; // @[InstrFetch.scala 26:18]
  assign dev_reset = reset; // @[InstrFetch.scala 27:18]
  assign dev_in_req_valid = 1'h1; // @[InstrFetch.scala 36:25]
  assign dev_in_req_bits_is_cached = 1'h0;
  assign dev_in_req_bits_addr = io_pc; // @[InstrFetch.scala 34:29]
  assign dev_in_req_bits_len = 2'h0; // @[InstrFetch.scala 35:28]
  assign dev_in_req_bits_strb = 4'h0;
  assign dev_in_req_bits_data = 32'h0;
  assign dev_in_req_bits_func = 1'h0; // @[InstrFetch.scala 33:29]
  assign dev_in_resp_ready = 1'h0;
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  pc_reg = _RAND_0[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  wb_if_fire = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      pc_reg <= 32'hbfc00000;
    end else if (wb_if_fire) begin
      pc_reg <= _T_6;
    end
    wb_if_fire <= io_wb_if_ready & io_wb_if_valid;
  end
endmodule
module InstrDecode(
  input         clock,
  output        io_if_id_ready,
  input         io_if_id_valid,
  input  [31:0] io_if_id_bits_instr,
  output [4:0]  io_out_gpr_read_rs_addr,
  output [4:0]  io_out_gpr_read_rt_addr,
  output        io_id_isu_valid,
  output [4:0]  io_id_isu_bits_rd_addr,
  output [15:0] io_id_isu_bits_imm,
  output        io_id_isu_bits_shamt_rs_sel,
  output [4:0]  io_id_isu_bits_shamt,
  output        io_id_isu_bits_sign_ext,
  output [3:0]  io_id_isu_bits_op,
  output        io_id_isu_bits_imm_rt_sel
);
  wire  _T = io_if_id_ready & io_if_id_valid; // @[Decoupled.scala 40:37]
  reg  if_id_fire; // @[InstrDecode.scala 15:29]
  reg [31:0] _RAND_0;
  reg [31:0] if_id_reg_instr; // @[util.scala 13:20]
  reg [31:0] _RAND_1;
  wire [4:0] rt = if_id_reg_instr[20:16]; // @[InstrDecode.scala 21:29]
  wire [4:0] rd = if_id_reg_instr[15:11]; // @[InstrDecode.scala 22:29]
  wire [31:0] _T_4 = if_id_reg_instr & 32'hffe00000; // @[Lookup.scala 31:38]
  wire  _T_5 = 32'h3c000000 == _T_4; // @[Lookup.scala 31:38]
  wire [31:0] _T_6 = if_id_reg_instr & 32'hfc0007ff; // @[Lookup.scala 31:38]
  wire  _T_7 = 32'h20 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_9 = 32'h21 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_11 = 32'h22 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_13 = 32'h23 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_15 = 32'h2a == _T_6; // @[Lookup.scala 31:38]
  wire  _T_17 = 32'h2b == _T_6; // @[Lookup.scala 31:38]
  wire  _T_19 = 32'h24 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_21 = 32'h25 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_25 = 32'h27 == _T_6; // @[Lookup.scala 31:38]
  wire [31:0] _T_26 = if_id_reg_instr & 32'hfc000000; // @[Lookup.scala 31:38]
  wire  _T_27 = 32'h28000000 == _T_26; // @[Lookup.scala 31:38]
  wire  _T_29 = 32'h2c000000 == _T_26; // @[Lookup.scala 31:38]
  wire [31:0] _T_30 = if_id_reg_instr & 32'hffe0003f; // @[Lookup.scala 31:38]
  wire  _T_31 = 32'h3 == _T_30; // @[Lookup.scala 31:38]
  wire  _T_33 = 32'h2 == _T_30; // @[Lookup.scala 31:38]
  wire  _T_35 = 32'h0 == _T_30; // @[Lookup.scala 31:38]
  wire  _T_37 = 32'h7 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_39 = 32'h6 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_41 = 32'h4 == _T_6; // @[Lookup.scala 31:38]
  wire  _T_43 = 32'h20000000 == _T_26; // @[Lookup.scala 31:38]
  wire  _T_45 = 32'h24000000 == _T_26; // @[Lookup.scala 31:38]
  wire  _T_47 = 32'h30000000 == _T_26; // @[Lookup.scala 31:38]
  wire  _T_49 = 32'h34000000 == _T_26; // @[Lookup.scala 31:38]
  wire  _T_51 = 32'h38000000 == _T_26; // @[Lookup.scala 31:38]
  wire [4:0] _T_63 = _T_29 ? rt : rd; // @[Lookup.scala 33:37]
  wire [4:0] _T_64 = _T_27 ? rt : _T_63; // @[Lookup.scala 33:37]
  wire [4:0] _T_65 = _T_25 ? rd : _T_64; // @[Lookup.scala 33:37]
  wire [4:0] _T_66 = _T_19 ? rd : _T_65; // @[Lookup.scala 33:37]
  wire [4:0] _T_67 = _T_21 ? rd : _T_66; // @[Lookup.scala 33:37]
  wire [4:0] _T_68 = _T_19 ? rd : _T_67; // @[Lookup.scala 33:37]
  wire [4:0] _T_69 = _T_17 ? rd : _T_68; // @[Lookup.scala 33:37]
  wire [4:0] _T_70 = _T_15 ? rd : _T_69; // @[Lookup.scala 33:37]
  wire [4:0] _T_71 = _T_13 ? rd : _T_70; // @[Lookup.scala 33:37]
  wire [4:0] _T_72 = _T_11 ? rd : _T_71; // @[Lookup.scala 33:37]
  wire [4:0] _T_73 = _T_9 ? rd : _T_72; // @[Lookup.scala 33:37]
  wire [4:0] _T_74 = _T_7 ? rd : _T_73; // @[Lookup.scala 33:37]
  wire  _T_86 = _T_39 | _T_41; // @[Lookup.scala 33:37]
  wire  _T_87 = _T_37 | _T_86; // @[Lookup.scala 33:37]
  wire  _T_88 = _T_35 ? 1'h0 : _T_87; // @[Lookup.scala 33:37]
  wire  _T_89 = _T_33 ? 1'h0 : _T_88; // @[Lookup.scala 33:37]
  wire  _T_90 = _T_31 ? 1'h0 : _T_89; // @[Lookup.scala 33:37]
  wire  _T_91 = _T_29 ? 1'h0 : _T_90; // @[Lookup.scala 33:37]
  wire  _T_93 = _T_27 ? 1'h0 : _T_91; // @[Lookup.scala 33:37]
  wire  _T_95 = _T_25 ? 1'h0 : _T_93; // @[Lookup.scala 33:37]
  wire  _T_97 = _T_19 ? 1'h0 : _T_95; // @[Lookup.scala 33:37]
  wire  _T_99 = _T_21 ? 1'h0 : _T_97; // @[Lookup.scala 33:37]
  wire  _T_101 = _T_19 ? 1'h0 : _T_99; // @[Lookup.scala 33:37]
  wire  _T_103 = _T_17 ? 1'h0 : _T_101; // @[Lookup.scala 33:37]
  wire  _T_105 = _T_15 ? 1'h0 : _T_103; // @[Lookup.scala 33:37]
  wire  _T_107 = _T_13 ? 1'h0 : _T_105; // @[Lookup.scala 33:37]
  wire  _T_109 = _T_11 ? 1'h0 : _T_107; // @[Lookup.scala 33:37]
  wire  _T_111 = _T_9 ? 1'h0 : _T_109; // @[Lookup.scala 33:37]
  wire  _T_113 = _T_7 ? 1'h0 : _T_111; // @[Lookup.scala 33:37]
  wire  _T_120 = _T_43 | _T_45; // @[Lookup.scala 33:37]
  wire  _T_121 = _T_41 ? 1'h0 : _T_120; // @[Lookup.scala 33:37]
  wire  _T_123 = _T_39 ? 1'h0 : _T_121; // @[Lookup.scala 33:37]
  wire  _T_125 = _T_37 ? 1'h0 : _T_123; // @[Lookup.scala 33:37]
  wire  _T_127 = _T_35 ? 1'h0 : _T_125; // @[Lookup.scala 33:37]
  wire  _T_129 = _T_33 ? 1'h0 : _T_127; // @[Lookup.scala 33:37]
  wire  _T_131 = _T_31 ? 1'h0 : _T_129; // @[Lookup.scala 33:37]
  wire  _T_133 = _T_29 | _T_131; // @[Lookup.scala 33:37]
  wire  _T_134 = _T_27 | _T_133; // @[Lookup.scala 33:37]
  wire  _T_135 = _T_25 ? 1'h0 : _T_134; // @[Lookup.scala 33:37]
  wire  _T_137 = _T_19 ? 1'h0 : _T_135; // @[Lookup.scala 33:37]
  wire  _T_139 = _T_21 ? 1'h0 : _T_137; // @[Lookup.scala 33:37]
  wire  _T_141 = _T_19 ? 1'h0 : _T_139; // @[Lookup.scala 33:37]
  wire  _T_143 = _T_17 ? 1'h0 : _T_141; // @[Lookup.scala 33:37]
  wire  _T_145 = _T_15 ? 1'h0 : _T_143; // @[Lookup.scala 33:37]
  wire  _T_147 = _T_13 ? 1'h0 : _T_145; // @[Lookup.scala 33:37]
  wire  _T_149 = _T_11 ? 1'h0 : _T_147; // @[Lookup.scala 33:37]
  wire  _T_151 = _T_9 ? 1'h0 : _T_149; // @[Lookup.scala 33:37]
  wire  _T_153 = _T_7 ? 1'h0 : _T_151; // @[Lookup.scala 33:37]
  wire [3:0] _T_179 = _T_51 ? 4'h9 : 4'h2; // @[Lookup.scala 33:37]
  wire [3:0] _T_180 = _T_49 ? 4'h6 : _T_179; // @[Lookup.scala 33:37]
  wire [3:0] _T_181 = _T_47 ? 4'h4 : _T_180; // @[Lookup.scala 33:37]
  wire [3:0] _T_182 = _T_45 ? 4'h0 : _T_181; // @[Lookup.scala 33:37]
  wire [3:0] _T_183 = _T_43 ? 4'he : _T_182; // @[Lookup.scala 33:37]
  wire [3:0] _T_184 = _T_41 ? 4'h8 : _T_183; // @[Lookup.scala 33:37]
  wire [3:0] _T_185 = _T_39 ? 4'ha : _T_184; // @[Lookup.scala 33:37]
  wire [3:0] _T_186 = _T_37 ? 4'hb : _T_185; // @[Lookup.scala 33:37]
  wire [3:0] _T_187 = _T_35 ? 4'h8 : _T_186; // @[Lookup.scala 33:37]
  wire [3:0] _T_188 = _T_33 ? 4'ha : _T_187; // @[Lookup.scala 33:37]
  wire [3:0] _T_189 = _T_31 ? 4'hb : _T_188; // @[Lookup.scala 33:37]
  wire [3:0] _T_190 = _T_29 ? 4'h7 : _T_189; // @[Lookup.scala 33:37]
  wire [3:0] _T_191 = _T_27 ? 4'h5 : _T_190; // @[Lookup.scala 33:37]
  wire [3:0] _T_192 = _T_25 ? 4'h3 : _T_191; // @[Lookup.scala 33:37]
  wire [3:0] _T_193 = _T_19 ? 4'h9 : _T_192; // @[Lookup.scala 33:37]
  wire [3:0] _T_194 = _T_21 ? 4'h6 : _T_193; // @[Lookup.scala 33:37]
  wire [3:0] _T_195 = _T_19 ? 4'h4 : _T_194; // @[Lookup.scala 33:37]
  wire [3:0] _T_196 = _T_17 ? 4'h7 : _T_195; // @[Lookup.scala 33:37]
  wire [3:0] _T_197 = _T_15 ? 4'h5 : _T_196; // @[Lookup.scala 33:37]
  wire [3:0] _T_198 = _T_13 ? 4'h1 : _T_197; // @[Lookup.scala 33:37]
  wire [3:0] _T_199 = _T_11 ? 4'hf : _T_198; // @[Lookup.scala 33:37]
  wire [3:0] _T_200 = _T_9 ? 4'h0 : _T_199; // @[Lookup.scala 33:37]
  wire [3:0] _T_201 = _T_7 ? 4'he : _T_200; // @[Lookup.scala 33:37]
  wire  _T_210 = _T_35 | _T_87; // @[Lookup.scala 33:37]
  wire  _T_211 = _T_33 | _T_210; // @[Lookup.scala 33:37]
  wire  _T_212 = _T_31 | _T_211; // @[Lookup.scala 33:37]
  wire  _T_213 = _T_29 ? 1'h0 : _T_212; // @[Lookup.scala 33:37]
  wire  _T_214 = _T_27 ? 1'h0 : _T_213; // @[Lookup.scala 33:37]
  wire  _T_215 = _T_25 | _T_214; // @[Lookup.scala 33:37]
  wire  _T_216 = _T_19 | _T_215; // @[Lookup.scala 33:37]
  wire  _T_217 = _T_21 | _T_216; // @[Lookup.scala 33:37]
  wire  _T_218 = _T_19 | _T_217; // @[Lookup.scala 33:37]
  wire  _T_219 = _T_17 | _T_218; // @[Lookup.scala 33:37]
  wire  _T_220 = _T_15 | _T_219; // @[Lookup.scala 33:37]
  wire  _T_221 = _T_13 | _T_220; // @[Lookup.scala 33:37]
  wire  _T_222 = _T_11 | _T_221; // @[Lookup.scala 33:37]
  wire  _T_223 = _T_9 | _T_222; // @[Lookup.scala 33:37]
  wire  _T_224 = _T_7 | _T_223; // @[Lookup.scala 33:37]
  assign io_if_id_ready = 1'h1; // @[InstrDecode.scala 14:20]
  assign io_out_gpr_read_rs_addr = if_id_reg_instr[25:21]; // @[InstrDecode.scala 65:21]
  assign io_out_gpr_read_rt_addr = if_id_reg_instr[20:16]; // @[InstrDecode.scala 65:21]
  assign io_id_isu_valid = if_id_fire; // @[InstrDecode.scala 64:21]
  assign io_id_isu_bits_rd_addr = _T_5 ? rd : _T_74; // @[InstrDecode.scala 58:28]
  assign io_id_isu_bits_imm = if_id_reg_instr[15:0]; // @[InstrDecode.scala 25:24]
  assign io_id_isu_bits_shamt_rs_sel = _T_5 ? 1'h0 : _T_113; // @[InstrDecode.scala 59:33]
  assign io_id_isu_bits_shamt = if_id_reg_instr[10:6]; // @[InstrDecode.scala 26:26]
  assign io_id_isu_bits_sign_ext = _T_5 ? 1'h0 : _T_153; // @[InstrDecode.scala 60:29]
  assign io_id_isu_bits_op = _T_5 ? 4'h2 : _T_201; // @[InstrDecode.scala 62:23]
  assign io_id_isu_bits_imm_rt_sel = _T_5 ? 1'h0 : _T_224; // @[InstrDecode.scala 63:31]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  if_id_fire = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  if_id_reg_instr = _RAND_1[31:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if_id_fire <= io_if_id_ready & io_if_id_valid;
    if (_T) begin
      if_id_reg_instr <= io_if_id_bits_instr;
    end
  end
endmodule
module ISU(
  input         clock,
  input  [31:0] io_gpr_data_rs_data,
  input  [31:0] io_gpr_data_rt_data,
  output        io_id_isu_ready,
  input         io_id_isu_valid,
  input  [4:0]  io_id_isu_bits_rd_addr,
  input  [15:0] io_id_isu_bits_imm,
  input         io_id_isu_bits_shamt_rs_sel,
  input  [4:0]  io_id_isu_bits_shamt,
  input         io_id_isu_bits_sign_ext,
  input  [3:0]  io_id_isu_bits_op,
  input         io_id_isu_bits_imm_rt_sel,
  output        io_isu_alu_valid,
  output [3:0]  io_isu_alu_bits_alu_op,
  output [31:0] io_isu_alu_bits_operand_1,
  output [31:0] io_isu_alu_bits_operand_2,
  output [4:0]  io_isu_alu_bits_rd_addr
);
  wire  _T = io_id_isu_ready & io_id_isu_valid; // @[Decoupled.scala 40:37]
  reg  id_isu_fire; // @[ISU.scala 16:30]
  reg [31:0] _RAND_0;
  reg [4:0] reg_id_isu_rd_addr; // @[util.scala 13:20]
  reg [31:0] _RAND_1;
  reg [15:0] reg_id_isu_imm; // @[util.scala 13:20]
  reg [31:0] _RAND_2;
  reg  reg_id_isu_shamt_rs_sel; // @[util.scala 13:20]
  reg [31:0] _RAND_3;
  reg [4:0] reg_id_isu_shamt; // @[util.scala 13:20]
  reg [31:0] _RAND_4;
  reg  reg_id_isu_sign_ext; // @[util.scala 13:20]
  reg [31:0] _RAND_5;
  reg [3:0] reg_id_isu_op; // @[util.scala 13:20]
  reg [31:0] _RAND_6;
  reg  reg_id_isu_imm_rt_sel; // @[util.scala 13:20]
  reg [31:0] _RAND_7;
  wire  _T_5 = ~reg_id_isu_imm_rt_sel; // @[Conditional.scala 37:30]
  wire [15:0] _T_8 = reg_id_isu_imm[15] ? 16'hffff : 16'h0; // @[Bitwise.scala 72:12]
  wire [31:0] _T_9 = {_T_8,reg_id_isu_imm}; // @[Cat.scala 30:58]
  wire [31:0] _T_11 = {16'h0,reg_id_isu_imm}; // @[Cat.scala 30:58]
  wire [31:0] _GEN_8 = reg_id_isu_sign_ext ? _T_9 : _T_11; // @[ISU.scala 32:46]
  assign io_id_isu_ready = 1'h1; // @[ISU.scala 15:21]
  assign io_isu_alu_valid = id_isu_fire; // @[ISU.scala 28:30]
  assign io_isu_alu_bits_alu_op = reg_id_isu_op; // @[ISU.scala 29:36 ISU.scala 42:29]
  assign io_isu_alu_bits_operand_1 = reg_id_isu_shamt_rs_sel ? {{27'd0}, reg_id_isu_shamt} : io_gpr_data_rs_data; // @[ISU.scala 42:29]
  assign io_isu_alu_bits_operand_2 = _T_5 ? _GEN_8 : io_gpr_data_rt_data; // @[ISU.scala 42:29]
  assign io_isu_alu_bits_rd_addr = reg_id_isu_rd_addr; // @[ISU.scala 42:29]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  id_isu_fire = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  reg_id_isu_rd_addr = _RAND_1[4:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  reg_id_isu_imm = _RAND_2[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  reg_id_isu_shamt_rs_sel = _RAND_3[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  reg_id_isu_shamt = _RAND_4[4:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  reg_id_isu_sign_ext = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  reg_id_isu_op = _RAND_6[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  reg_id_isu_imm_rt_sel = _RAND_7[0:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    id_isu_fire <= io_id_isu_ready & io_id_isu_valid;
    if (_T) begin
      reg_id_isu_rd_addr <= io_id_isu_bits_rd_addr;
    end
    if (_T) begin
      reg_id_isu_imm <= io_id_isu_bits_imm;
    end
    if (_T) begin
      reg_id_isu_shamt_rs_sel <= io_id_isu_bits_shamt_rs_sel;
    end
    if (_T) begin
      reg_id_isu_shamt <= io_id_isu_bits_shamt;
    end
    if (_T) begin
      reg_id_isu_sign_ext <= io_id_isu_bits_sign_ext;
    end
    if (_T) begin
      reg_id_isu_op <= io_id_isu_bits_op;
    end
    if (_T) begin
      reg_id_isu_imm_rt_sel <= io_id_isu_bits_imm_rt_sel;
    end
  end
endmodule
module adder(
  input  [31:0] io_A_in,
  input  [31:0] io_B_in,
  input         io_Cin,
  output        io_Carry,
  output        io_Overflow,
  output        io_Negative,
  output [31:0] io_O_out
);
  wire [32:0] _T = io_A_in + io_B_in; // @[EXEC.scala 31:32]
  wire [32:0] _GEN_0 = {{32'd0}, io_Cin}; // @[EXEC.scala 31:43]
  wire [33:0] res = _T + _GEN_0; // @[EXEC.scala 31:43]
  wire  _T_6 = ~io_A_in[31]; // @[EXEC.scala 34:22]
  wire  _T_8 = ~io_B_in[31]; // @[EXEC.scala 34:39]
  wire  _T_9 = _T_6 & _T_8; // @[EXEC.scala 34:36]
  wire  _T_11 = _T_9 & res[31]; // @[EXEC.scala 34:53]
  wire  _T_14 = io_A_in[31] & io_B_in[31]; // @[EXEC.scala 34:82]
  wire  _T_16 = ~res[31]; // @[EXEC.scala 34:99]
  wire  _T_17 = _T_14 & _T_16; // @[EXEC.scala 34:96]
  assign io_Carry = res[32]; // @[EXEC.scala 32:14]
  assign io_Overflow = _T_11 | _T_17; // @[EXEC.scala 34:17]
  assign io_Negative = res[31]; // @[EXEC.scala 35:17]
  assign io_O_out = res[31:0]; // @[EXEC.scala 36:14]
endmodule
module AluPart(
  input  [31:0] io_A_in,
  input  [31:0] io_B_in,
  input  [3:0]  io_ALU_op,
  output [31:0] io_ALU_out,
  output        io_Less,
  output        io_Overflow_out
);
  wire [31:0] Adder_io_A_in; // @[EXEC.scala 94:23]
  wire [31:0] Adder_io_B_in; // @[EXEC.scala 94:23]
  wire  Adder_io_Cin; // @[EXEC.scala 94:23]
  wire  Adder_io_Carry; // @[EXEC.scala 94:23]
  wire  Adder_io_Overflow; // @[EXEC.scala 94:23]
  wire  Adder_io_Negative; // @[EXEC.scala 94:23]
  wire [31:0] Adder_io_O_out; // @[EXEC.scala 94:23]
  wire  op3 = io_ALU_op[3]; // @[EXEC.scala 86:33]
  wire  op2 = io_ALU_op[2]; // @[EXEC.scala 87:33]
  wire  op1 = io_ALU_op[1]; // @[EXEC.scala 88:33]
  wire  op0 = io_ALU_op[0]; // @[EXEC.scala 89:33]
  wire  _T_4 = ~op3; // @[EXEC.scala 90:19]
  wire  _T_5 = ~op1; // @[EXEC.scala 90:26]
  wire  _T_6 = _T_4 & _T_5; // @[EXEC.scala 90:24]
  wire  _T_8 = _T_4 & op2; // @[EXEC.scala 90:38]
  wire  _T_9 = _T_8 & op0; // @[EXEC.scala 90:44]
  wire  _T_10 = _T_6 | _T_9; // @[EXEC.scala 90:31]
  wire  _T_11 = op3 & op1; // @[EXEC.scala 90:56]
  wire  ALU_ctr_2 = _T_10 | _T_11; // @[EXEC.scala 90:50]
  wire  _T_14 = ~op2; // @[EXEC.scala 91:26]
  wire  _T_15 = _T_4 & _T_14; // @[EXEC.scala 91:24]
  wire  _T_17 = _T_15 & _T_5; // @[EXEC.scala 91:31]
  wire  _T_19 = op3 & _T_14; // @[EXEC.scala 91:44]
  wire  _T_20 = ~op0; // @[EXEC.scala 91:53]
  wire  _T_21 = _T_19 & _T_20; // @[EXEC.scala 91:51]
  wire  _T_22 = _T_17 | _T_21; // @[EXEC.scala 91:38]
  wire  _T_23 = op2 & op1; // @[EXEC.scala 91:64]
  wire  _T_25 = _T_23 & _T_20; // @[EXEC.scala 91:70]
  wire  _T_26 = _T_22 | _T_25; // @[EXEC.scala 91:58]
  wire  ALU_ctr_1 = _T_26 | _T_11; // @[EXEC.scala 91:76]
  wire  _T_31 = _T_14 & _T_5; // @[EXEC.scala 92:24]
  wire  _T_35 = _T_31 | _T_9; // @[EXEC.scala 92:31]
  wire  _T_36 = op3 & op2; // @[EXEC.scala 92:56]
  wire  _T_37 = _T_36 & op1; // @[EXEC.scala 92:62]
  wire  ALU_ctr_0 = _T_35 | _T_37; // @[EXEC.scala 92:50]
  wire [31:0] _T_41 = op0 ? 32'hffffffff : 32'h0; // @[Bitwise.scala 72:12]
  wire  _T_44 = io_ALU_op == 4'h7; // @[EXEC.scala 99:20]
  wire  _T_45 = ~Adder_io_Carry; // @[EXEC.scala 100:20]
  wire  _T_46 = Adder_io_Overflow ^ Adder_io_Negative; // @[EXEC.scala 102:38]
  wire  _T_48 = io_ALU_op[3:1] == 3'h7; // @[EXEC.scala 105:25]
  wire [2:0] _T_50 = {ALU_ctr_2,ALU_ctr_1,ALU_ctr_0}; // @[EXEC.scala 114:26]
  wire  _T_51 = 3'h0 == _T_50; // @[Conditional.scala 37:30]
  wire [31:0] _T_55 = io_A_in ^ _T_41; // @[EXEC.scala 116:41]
  wire [1:0] _T_90 = _T_55[2] ? 2'h2 : {{1'd0}, _T_55[1]}; // @[EXEC.scala 117:53]
  wire [1:0] _T_91 = _T_55[3] ? 2'h3 : _T_90; // @[EXEC.scala 117:53]
  wire [2:0] _T_92 = _T_55[4] ? 3'h4 : {{1'd0}, _T_91}; // @[EXEC.scala 117:53]
  wire [2:0] _T_93 = _T_55[5] ? 3'h5 : _T_92; // @[EXEC.scala 117:53]
  wire [2:0] _T_94 = _T_55[6] ? 3'h6 : _T_93; // @[EXEC.scala 117:53]
  wire [2:0] _T_95 = _T_55[7] ? 3'h7 : _T_94; // @[EXEC.scala 117:53]
  wire [3:0] _T_96 = _T_55[8] ? 4'h8 : {{1'd0}, _T_95}; // @[EXEC.scala 117:53]
  wire [3:0] _T_97 = _T_55[9] ? 4'h9 : _T_96; // @[EXEC.scala 117:53]
  wire [3:0] _T_98 = _T_55[10] ? 4'ha : _T_97; // @[EXEC.scala 117:53]
  wire [3:0] _T_99 = _T_55[11] ? 4'hb : _T_98; // @[EXEC.scala 117:53]
  wire [3:0] _T_100 = _T_55[12] ? 4'hc : _T_99; // @[EXEC.scala 117:53]
  wire [3:0] _T_101 = _T_55[13] ? 4'hd : _T_100; // @[EXEC.scala 117:53]
  wire [3:0] _T_102 = _T_55[14] ? 4'he : _T_101; // @[EXEC.scala 117:53]
  wire [3:0] _T_103 = _T_55[15] ? 4'hf : _T_102; // @[EXEC.scala 117:53]
  wire [4:0] _T_104 = _T_55[16] ? 5'h10 : {{1'd0}, _T_103}; // @[EXEC.scala 117:53]
  wire [4:0] _T_105 = _T_55[17] ? 5'h11 : _T_104; // @[EXEC.scala 117:53]
  wire [4:0] _T_106 = _T_55[18] ? 5'h12 : _T_105; // @[EXEC.scala 117:53]
  wire [4:0] _T_107 = _T_55[19] ? 5'h13 : _T_106; // @[EXEC.scala 117:53]
  wire [4:0] _T_108 = _T_55[20] ? 5'h14 : _T_107; // @[EXEC.scala 117:53]
  wire [4:0] _T_109 = _T_55[21] ? 5'h15 : _T_108; // @[EXEC.scala 117:53]
  wire [4:0] _T_110 = _T_55[22] ? 5'h16 : _T_109; // @[EXEC.scala 117:53]
  wire [4:0] _T_111 = _T_55[23] ? 5'h17 : _T_110; // @[EXEC.scala 117:53]
  wire [4:0] _T_112 = _T_55[24] ? 5'h18 : _T_111; // @[EXEC.scala 117:53]
  wire [4:0] _T_113 = _T_55[25] ? 5'h19 : _T_112; // @[EXEC.scala 117:53]
  wire [4:0] _T_114 = _T_55[26] ? 5'h1a : _T_113; // @[EXEC.scala 117:53]
  wire [4:0] _T_115 = _T_55[27] ? 5'h1b : _T_114; // @[EXEC.scala 117:53]
  wire [4:0] _T_116 = _T_55[28] ? 5'h1c : _T_115; // @[EXEC.scala 117:53]
  wire [4:0] _T_117 = _T_55[29] ? 5'h1d : _T_116; // @[EXEC.scala 117:53]
  wire [4:0] _T_118 = _T_55[30] ? 5'h1e : _T_117; // @[EXEC.scala 117:53]
  wire [4:0] _T_119 = _T_55[31] ? 5'h1f : _T_118; // @[EXEC.scala 117:53]
  wire [4:0] _T_121 = 5'h1f - _T_119; // @[EXEC.scala 117:32]
  wire  _T_122 = 3'h1 == _T_50; // @[Conditional.scala 37:30]
  wire [31:0] _T_123 = io_A_in ^ io_B_in; // @[EXEC.scala 121:35]
  wire  _T_124 = 3'h2 == _T_50; // @[Conditional.scala 37:30]
  wire [31:0] _T_125 = io_A_in | io_B_in; // @[EXEC.scala 124:35]
  wire  _T_126 = 3'h3 == _T_50; // @[Conditional.scala 37:30]
  wire [31:0] _T_128 = ~_T_125; // @[EXEC.scala 127:27]
  wire  _T_129 = 3'h4 == _T_50; // @[Conditional.scala 37:30]
  wire [31:0] _T_130 = io_A_in & io_B_in; // @[EXEC.scala 130:35]
  wire  _T_131 = 3'h5 == _T_50; // @[Conditional.scala 37:30]
  wire  _T_134 = 3'h6 == _T_50; // @[Conditional.scala 37:30]
  wire [15:0] _T_138 = io_B_in[15] ? 16'hffff : 16'h0; // @[Bitwise.scala 72:12]
  wire [31:0] _T_140 = {_T_138,io_B_in[15:0]}; // @[Cat.scala 30:58]
  wire [23:0] _T_143 = io_B_in[7] ? 24'hffffff : 24'h0; // @[Bitwise.scala 72:12]
  wire [31:0] _T_145 = {_T_143,io_B_in[7:0]}; // @[Cat.scala 30:58]
  wire [31:0] _T_146 = op0 ? _T_140 : _T_145; // @[EXEC.scala 138:30]
  wire  _T_147 = 3'h7 == _T_50; // @[Conditional.scala 37:30]
  wire [31:0] _GEN_2 = _T_147 ? Adder_io_O_out : 32'h0; // @[Conditional.scala 39:67]
  wire [31:0] _GEN_3 = _T_134 ? _T_146 : _GEN_2; // @[Conditional.scala 39:67]
  wire [31:0] _GEN_4 = _T_131 ? {{31'd0}, io_Less} : _GEN_3; // @[Conditional.scala 39:67]
  wire [31:0] _GEN_5 = _T_129 ? _T_130 : _GEN_4; // @[Conditional.scala 39:67]
  wire [31:0] _GEN_6 = _T_126 ? _T_128 : _GEN_5; // @[Conditional.scala 39:67]
  wire [31:0] _GEN_7 = _T_124 ? _T_125 : _GEN_6; // @[Conditional.scala 39:67]
  wire [31:0] _GEN_8 = _T_122 ? _T_123 : _GEN_7; // @[Conditional.scala 39:67]
  adder Adder ( // @[EXEC.scala 94:23]
    .io_A_in(Adder_io_A_in),
    .io_B_in(Adder_io_B_in),
    .io_Cin(Adder_io_Cin),
    .io_Carry(Adder_io_Carry),
    .io_Overflow(Adder_io_Overflow),
    .io_Negative(Adder_io_Negative),
    .io_O_out(Adder_io_O_out)
  );
  assign io_ALU_out = _T_51 ? {{27'd0}, _T_121} : _GEN_8; // @[EXEC.scala 113:16 EXEC.scala 117:24 EXEC.scala 121:24 EXEC.scala 124:24 EXEC.scala 127:24 EXEC.scala 130:24 EXEC.scala 133:24 EXEC.scala 138:24 EXEC.scala 141:24]
  assign io_Less = _T_44 ? _T_45 : _T_46; // @[EXEC.scala 100:17 EXEC.scala 102:17]
  assign io_Overflow_out = _T_48 & Adder_io_Overflow; // @[EXEC.scala 106:25 EXEC.scala 108:25]
  assign Adder_io_A_in = io_A_in; // @[EXEC.scala 95:19]
  assign Adder_io_B_in = io_B_in ^ _T_41; // @[EXEC.scala 96:19]
  assign Adder_io_Cin = io_ALU_op[0]; // @[EXEC.scala 97:18]
endmodule
module barrelShifter(
  input  [31:0] io_shift_in,
  input  [4:0]  io_shift_amount,
  input  [1:0]  io_shift_op,
  output [31:0] io_shift_out
);
  wire  _T = 2'h0 == io_shift_op; // @[Conditional.scala 37:30]
  wire [62:0] _GEN_4 = {{31'd0}, io_shift_in}; // @[EXEC.scala 48:42]
  wire [62:0] _T_1 = _GEN_4 << io_shift_amount; // @[EXEC.scala 48:42]
  wire  _T_3 = 2'h1 == io_shift_op; // @[Conditional.scala 37:30]
  wire [31:0] _T_4 = io_shift_in >> io_shift_amount; // @[EXEC.scala 51:41]
  wire  _T_5 = 2'h2 == io_shift_op; // @[Conditional.scala 37:30]
  wire [31:0] _T_8 = $signed(io_shift_in) >>> io_shift_amount; // @[EXEC.scala 54:77]
  wire  _T_9 = 2'h3 == io_shift_op; // @[Conditional.scala 37:30]
  wire [5:0] _GEN_5 = {{1'd0}, io_shift_amount}; // @[EXEC.scala 57:51]
  wire [5:0] _T_11 = 6'h20 - _GEN_5; // @[EXEC.scala 57:51]
  wire [94:0] _GEN_6 = {{63'd0}, io_shift_in}; // @[EXEC.scala 57:42]
  wire [94:0] _T_12 = _GEN_6 << _T_11; // @[EXEC.scala 57:42]
  wire [94:0] _GEN_7 = {{63'd0}, _T_4}; // @[EXEC.scala 57:71]
  wire [94:0] _T_14 = _T_12 | _GEN_7; // @[EXEC.scala 57:71]
  wire [94:0] _GEN_0 = _T_9 ? _T_14 : 95'h0; // @[Conditional.scala 39:67]
  wire [94:0] _GEN_1 = _T_5 ? {{63'd0}, _T_8} : _GEN_0; // @[Conditional.scala 39:67]
  wire [94:0] _GEN_2 = _T_3 ? {{63'd0}, _T_4} : _GEN_1; // @[Conditional.scala 39:67]
  wire [94:0] _GEN_3 = _T ? {{63'd0}, _T_1[31:0]} : _GEN_2; // @[Conditional.scala 40:58]
  assign io_shift_out = _GEN_3[31:0]; // @[EXEC.scala 45:18 EXEC.scala 48:26 EXEC.scala 51:26 EXEC.scala 54:26 EXEC.scala 57:26]
endmodule
module ALU(
  input         clock,
  output        io_isu_alu_ready,
  input         io_isu_alu_valid,
  input  [3:0]  io_isu_alu_bits_alu_op,
  input  [31:0] io_isu_alu_bits_operand_1,
  input  [31:0] io_isu_alu_bits_operand_2,
  input  [4:0]  io_isu_alu_bits_rd_addr,
  output [31:0] io_out_ALU_out,
  output        io_out_Overflow_out,
  output        io_exec_wb_valid,
  output [4:0]  io_exec_wb_bits_w_addr
);
  wire [31:0] alu_io_A_in; // @[EXEC.scala 164:21]
  wire [31:0] alu_io_B_in; // @[EXEC.scala 164:21]
  wire [3:0] alu_io_ALU_op; // @[EXEC.scala 164:21]
  wire [31:0] alu_io_ALU_out; // @[EXEC.scala 164:21]
  wire  alu_io_Less; // @[EXEC.scala 164:21]
  wire  alu_io_Overflow_out; // @[EXEC.scala 164:21]
  wire [31:0] shift_io_shift_in; // @[EXEC.scala 165:23]
  wire [4:0] shift_io_shift_amount; // @[EXEC.scala 165:23]
  wire [1:0] shift_io_shift_op; // @[EXEC.scala 165:23]
  wire [31:0] shift_io_shift_out; // @[EXEC.scala 165:23]
  wire  _T = io_isu_alu_ready & io_isu_alu_valid; // @[Decoupled.scala 40:37]
  reg  isu_alu_fire; // @[EXEC.scala 154:31]
  reg [31:0] _RAND_0;
  reg [3:0] r_alu_op; // @[util.scala 13:20]
  reg [31:0] _RAND_1;
  reg [31:0] r_operand_1; // @[util.scala 13:20]
  reg [31:0] _RAND_2;
  reg [31:0] r_operand_2; // @[util.scala 13:20]
  reg [31:0] _RAND_3;
  wire  _T_3 = 4'h8 == r_alu_op; // @[Conditional.scala 37:30]
  wire  _T_4 = 4'ha == r_alu_op; // @[Conditional.scala 37:30]
  wire  _T_5 = 4'hb == r_alu_op; // @[Conditional.scala 37:30]
  wire [1:0] _GEN_5 = _T_4 ? 2'h1 : 2'h2; // @[Conditional.scala 39:67]
  wire  _T_11 = _T_3 | _T_4; // @[EXEC.scala 184:60]
  wire  _T_12 = _T_11 | _T_5; // @[EXEC.scala 184:60]
  AluPart alu ( // @[EXEC.scala 164:21]
    .io_A_in(alu_io_A_in),
    .io_B_in(alu_io_B_in),
    .io_ALU_op(alu_io_ALU_op),
    .io_ALU_out(alu_io_ALU_out),
    .io_Less(alu_io_Less),
    .io_Overflow_out(alu_io_Overflow_out)
  );
  barrelShifter shift ( // @[EXEC.scala 165:23]
    .io_shift_in(shift_io_shift_in),
    .io_shift_amount(shift_io_shift_amount),
    .io_shift_op(shift_io_shift_op),
    .io_shift_out(shift_io_shift_out)
  );
  assign io_isu_alu_ready = 1'h1; // @[EXEC.scala 153:22]
  assign io_out_ALU_out = _T_12 ? shift_io_shift_out : alu_io_ALU_out; // @[EXEC.scala 197:20]
  assign io_out_Overflow_out = _T_12 ? 1'h0 : alu_io_Overflow_out; // @[EXEC.scala 199:25]
  assign io_exec_wb_valid = isu_alu_fire; // @[EXEC.scala 205:22]
  assign io_exec_wb_bits_w_addr = io_isu_alu_bits_rd_addr; // @[EXEC.scala 202:28]
  assign alu_io_A_in = r_operand_1; // @[EXEC.scala 182:17]
  assign alu_io_B_in = r_operand_2; // @[EXEC.scala 183:17]
  assign alu_io_ALU_op = r_alu_op; // @[EXEC.scala 181:19]
  assign shift_io_shift_in = r_operand_2; // @[EXEC.scala 168:23]
  assign shift_io_shift_amount = r_operand_1[4:0]; // @[EXEC.scala 167:27]
  assign shift_io_shift_op = _T_3 ? 2'h0 : _GEN_5; // @[EXEC.scala 172:31 EXEC.scala 175:31 EXEC.scala 178:31]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  isu_alu_fire = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  r_alu_op = _RAND_1[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  r_operand_1 = _RAND_2[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  r_operand_2 = _RAND_3[31:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    isu_alu_fire <= io_isu_alu_ready & io_isu_alu_valid;
    if (_T) begin
      r_alu_op <= io_isu_alu_bits_alu_op;
    end
    if (_T) begin
      r_operand_1 <= io_isu_alu_bits_operand_1;
    end
    if (_T) begin
      r_operand_2 <= io_isu_alu_bits_operand_2;
    end
  end
endmodule
module WriteBack(
  input         clock,
  output        io_exec_wb_ready,
  input         io_exec_wb_valid,
  input  [4:0]  io_exec_wb_bits_w_addr,
  input  [31:0] io_alu_output_ALU_out,
  input         io_alu_output_Overflow_out,
  output        io_wb_if_valid,
  output [3:0]  io_gpr_wr_w_en,
  output [4:0]  io_gpr_wr_addr,
  output [31:0] io_gpr_wr_data
);
  wire  _T = io_exec_wb_ready & io_exec_wb_valid; // @[Decoupled.scala 40:37]
  reg  exec_wb_fire; // @[WriteBack.scala 16:31]
  reg [31:0] _RAND_0;
  reg [31:0] reg_alu_output_ALU_out; // @[util.scala 13:20]
  reg [31:0] _RAND_1;
  reg  reg_alu_output_Overflow_out; // @[util.scala 13:20]
  reg [31:0] _RAND_2;
  reg [4:0] reg_exec_wb_w_addr; // @[util.scala 13:20]
  reg [31:0] _RAND_3;
  wire  _T_5 = ~reg_alu_output_Overflow_out; // @[WriteBack.scala 28:10]
  assign io_exec_wb_ready = 1'h1; // @[WriteBack.scala 15:22]
  assign io_wb_if_valid = exec_wb_fire; // @[WriteBack.scala 34:20]
  assign io_gpr_wr_w_en = {{3'd0}, _T_5}; // @[WriteBack.scala 21:20 WriteBack.scala 31:24]
  assign io_gpr_wr_addr = reg_exec_wb_w_addr; // @[WriteBack.scala 29:24]
  assign io_gpr_wr_data = reg_alu_output_ALU_out; // @[WriteBack.scala 30:24]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  exec_wb_fire = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  reg_alu_output_ALU_out = _RAND_1[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  reg_alu_output_Overflow_out = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  reg_exec_wb_w_addr = _RAND_3[4:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    exec_wb_fire <= io_exec_wb_ready & io_exec_wb_valid;
    if (_T) begin
      reg_alu_output_ALU_out <= io_alu_output_ALU_out;
    end
    if (_T) begin
      reg_alu_output_Overflow_out <= io_alu_output_Overflow_out;
    end
    if (_T) begin
      reg_exec_wb_w_addr <= io_exec_wb_bits_w_addr;
    end
  end
endmodule
module verilator_top(
  input         clock,
  input         reset,
  output        io_commit_valid,
  output [31:0] io_commit_pc,
  output [31:0] io_commit_instr,
  output        io_commit_ip7,
  output [31:0] io_commit_gpr_0,
  output [31:0] io_commit_gpr_1,
  output [31:0] io_commit_gpr_2,
  output [31:0] io_commit_gpr_3,
  output [31:0] io_commit_gpr_4,
  output [31:0] io_commit_gpr_5,
  output [31:0] io_commit_gpr_6,
  output [31:0] io_commit_gpr_7,
  output [31:0] io_commit_gpr_8,
  output [31:0] io_commit_gpr_9,
  output [31:0] io_commit_gpr_10,
  output [31:0] io_commit_gpr_11,
  output [31:0] io_commit_gpr_12,
  output [31:0] io_commit_gpr_13,
  output [31:0] io_commit_gpr_14,
  output [31:0] io_commit_gpr_15,
  output [31:0] io_commit_gpr_16,
  output [31:0] io_commit_gpr_17,
  output [31:0] io_commit_gpr_18,
  output [31:0] io_commit_gpr_19,
  output [31:0] io_commit_gpr_20,
  output [31:0] io_commit_gpr_21,
  output [31:0] io_commit_gpr_22,
  output [31:0] io_commit_gpr_23,
  output [31:0] io_commit_gpr_24,
  output [31:0] io_commit_gpr_25,
  output [31:0] io_commit_gpr_26,
  output [31:0] io_commit_gpr_27,
  output [31:0] io_commit_gpr_28,
  output [31:0] io_commit_gpr_29,
  output [31:0] io_commit_gpr_30,
  output [31:0] io_commit_gpr_31,
  output [4:0]  io_commit_rd_idx,
  output [31:0] io_commit_wdata,
  output        io_commit_wen,
  input         io_can_log_now
);
  wire  gprs_clock; // @[top.scala 18:22]
  wire  gprs_reset; // @[top.scala 18:22]
  wire [4:0] gprs_io_read_in_rs_addr; // @[top.scala 18:22]
  wire [4:0] gprs_io_read_in_rt_addr; // @[top.scala 18:22]
  wire [3:0] gprs_io_write_in_w_en; // @[top.scala 18:22]
  wire [4:0] gprs_io_write_in_addr; // @[top.scala 18:22]
  wire [31:0] gprs_io_write_in_data; // @[top.scala 18:22]
  wire [31:0] gprs_io_read_out_rs_data; // @[top.scala 18:22]
  wire [31:0] gprs_io_read_out_rt_data; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_0; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_1; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_2; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_3; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_4; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_5; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_6; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_7; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_8; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_9; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_10; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_11; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_12; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_13; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_14; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_15; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_16; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_17; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_18; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_19; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_20; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_21; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_22; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_23; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_24; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_25; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_26; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_27; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_28; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_29; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_30; // @[top.scala 18:22]
  wire [31:0] gprs_io_gpr_commit_31; // @[top.scala 18:22]
  wire  instr_fetch_clock; // @[top.scala 20:29]
  wire  instr_fetch_reset; // @[top.scala 20:29]
  wire  instr_fetch_io_wb_if_ready; // @[top.scala 20:29]
  wire  instr_fetch_io_wb_if_valid; // @[top.scala 20:29]
  wire [31:0] instr_fetch_io_pc; // @[top.scala 20:29]
  wire  instr_fetch_io_if_id_valid; // @[top.scala 20:29]
  wire [31:0] instr_fetch_io_if_id_bits_instr; // @[top.scala 20:29]
  wire  instr_decode_clock; // @[top.scala 21:30]
  wire  instr_decode_io_if_id_ready; // @[top.scala 21:30]
  wire  instr_decode_io_if_id_valid; // @[top.scala 21:30]
  wire [31:0] instr_decode_io_if_id_bits_instr; // @[top.scala 21:30]
  wire [4:0] instr_decode_io_out_gpr_read_rs_addr; // @[top.scala 21:30]
  wire [4:0] instr_decode_io_out_gpr_read_rt_addr; // @[top.scala 21:30]
  wire  instr_decode_io_id_isu_valid; // @[top.scala 21:30]
  wire [4:0] instr_decode_io_id_isu_bits_rd_addr; // @[top.scala 21:30]
  wire [15:0] instr_decode_io_id_isu_bits_imm; // @[top.scala 21:30]
  wire  instr_decode_io_id_isu_bits_shamt_rs_sel; // @[top.scala 21:30]
  wire [4:0] instr_decode_io_id_isu_bits_shamt; // @[top.scala 21:30]
  wire  instr_decode_io_id_isu_bits_sign_ext; // @[top.scala 21:30]
  wire [3:0] instr_decode_io_id_isu_bits_op; // @[top.scala 21:30]
  wire  instr_decode_io_id_isu_bits_imm_rt_sel; // @[top.scala 21:30]
  wire  instr_shoot_clock; // @[top.scala 22:29]
  wire [31:0] instr_shoot_io_gpr_data_rs_data; // @[top.scala 22:29]
  wire [31:0] instr_shoot_io_gpr_data_rt_data; // @[top.scala 22:29]
  wire  instr_shoot_io_id_isu_ready; // @[top.scala 22:29]
  wire  instr_shoot_io_id_isu_valid; // @[top.scala 22:29]
  wire [4:0] instr_shoot_io_id_isu_bits_rd_addr; // @[top.scala 22:29]
  wire [15:0] instr_shoot_io_id_isu_bits_imm; // @[top.scala 22:29]
  wire  instr_shoot_io_id_isu_bits_shamt_rs_sel; // @[top.scala 22:29]
  wire [4:0] instr_shoot_io_id_isu_bits_shamt; // @[top.scala 22:29]
  wire  instr_shoot_io_id_isu_bits_sign_ext; // @[top.scala 22:29]
  wire [3:0] instr_shoot_io_id_isu_bits_op; // @[top.scala 22:29]
  wire  instr_shoot_io_id_isu_bits_imm_rt_sel; // @[top.scala 22:29]
  wire  instr_shoot_io_isu_alu_valid; // @[top.scala 22:29]
  wire [3:0] instr_shoot_io_isu_alu_bits_alu_op; // @[top.scala 22:29]
  wire [31:0] instr_shoot_io_isu_alu_bits_operand_1; // @[top.scala 22:29]
  wire [31:0] instr_shoot_io_isu_alu_bits_operand_2; // @[top.scala 22:29]
  wire [4:0] instr_shoot_io_isu_alu_bits_rd_addr; // @[top.scala 22:29]
  wire  alu_clock; // @[top.scala 24:21]
  wire  alu_io_isu_alu_ready; // @[top.scala 24:21]
  wire  alu_io_isu_alu_valid; // @[top.scala 24:21]
  wire [3:0] alu_io_isu_alu_bits_alu_op; // @[top.scala 24:21]
  wire [31:0] alu_io_isu_alu_bits_operand_1; // @[top.scala 24:21]
  wire [31:0] alu_io_isu_alu_bits_operand_2; // @[top.scala 24:21]
  wire [4:0] alu_io_isu_alu_bits_rd_addr; // @[top.scala 24:21]
  wire [31:0] alu_io_out_ALU_out; // @[top.scala 24:21]
  wire  alu_io_out_Overflow_out; // @[top.scala 24:21]
  wire  alu_io_exec_wb_valid; // @[top.scala 24:21]
  wire [4:0] alu_io_exec_wb_bits_w_addr; // @[top.scala 24:21]
  wire  write_back_clock; // @[top.scala 26:28]
  wire  write_back_io_exec_wb_ready; // @[top.scala 26:28]
  wire  write_back_io_exec_wb_valid; // @[top.scala 26:28]
  wire [4:0] write_back_io_exec_wb_bits_w_addr; // @[top.scala 26:28]
  wire [31:0] write_back_io_alu_output_ALU_out; // @[top.scala 26:28]
  wire  write_back_io_alu_output_Overflow_out; // @[top.scala 26:28]
  wire  write_back_io_wb_if_valid; // @[top.scala 26:28]
  wire [3:0] write_back_io_gpr_wr_w_en; // @[top.scala 26:28]
  wire [4:0] write_back_io_gpr_wr_addr; // @[top.scala 26:28]
  wire [31:0] write_back_io_gpr_wr_data; // @[top.scala 26:28]
  GPR gprs ( // @[top.scala 18:22]
    .clock(gprs_clock),
    .reset(gprs_reset),
    .io_read_in_rs_addr(gprs_io_read_in_rs_addr),
    .io_read_in_rt_addr(gprs_io_read_in_rt_addr),
    .io_write_in_w_en(gprs_io_write_in_w_en),
    .io_write_in_addr(gprs_io_write_in_addr),
    .io_write_in_data(gprs_io_write_in_data),
    .io_read_out_rs_data(gprs_io_read_out_rs_data),
    .io_read_out_rt_data(gprs_io_read_out_rt_data),
    .io_gpr_commit_0(gprs_io_gpr_commit_0),
    .io_gpr_commit_1(gprs_io_gpr_commit_1),
    .io_gpr_commit_2(gprs_io_gpr_commit_2),
    .io_gpr_commit_3(gprs_io_gpr_commit_3),
    .io_gpr_commit_4(gprs_io_gpr_commit_4),
    .io_gpr_commit_5(gprs_io_gpr_commit_5),
    .io_gpr_commit_6(gprs_io_gpr_commit_6),
    .io_gpr_commit_7(gprs_io_gpr_commit_7),
    .io_gpr_commit_8(gprs_io_gpr_commit_8),
    .io_gpr_commit_9(gprs_io_gpr_commit_9),
    .io_gpr_commit_10(gprs_io_gpr_commit_10),
    .io_gpr_commit_11(gprs_io_gpr_commit_11),
    .io_gpr_commit_12(gprs_io_gpr_commit_12),
    .io_gpr_commit_13(gprs_io_gpr_commit_13),
    .io_gpr_commit_14(gprs_io_gpr_commit_14),
    .io_gpr_commit_15(gprs_io_gpr_commit_15),
    .io_gpr_commit_16(gprs_io_gpr_commit_16),
    .io_gpr_commit_17(gprs_io_gpr_commit_17),
    .io_gpr_commit_18(gprs_io_gpr_commit_18),
    .io_gpr_commit_19(gprs_io_gpr_commit_19),
    .io_gpr_commit_20(gprs_io_gpr_commit_20),
    .io_gpr_commit_21(gprs_io_gpr_commit_21),
    .io_gpr_commit_22(gprs_io_gpr_commit_22),
    .io_gpr_commit_23(gprs_io_gpr_commit_23),
    .io_gpr_commit_24(gprs_io_gpr_commit_24),
    .io_gpr_commit_25(gprs_io_gpr_commit_25),
    .io_gpr_commit_26(gprs_io_gpr_commit_26),
    .io_gpr_commit_27(gprs_io_gpr_commit_27),
    .io_gpr_commit_28(gprs_io_gpr_commit_28),
    .io_gpr_commit_29(gprs_io_gpr_commit_29),
    .io_gpr_commit_30(gprs_io_gpr_commit_30),
    .io_gpr_commit_31(gprs_io_gpr_commit_31)
  );
  InstrFetch instr_fetch ( // @[top.scala 20:29]
    .clock(instr_fetch_clock),
    .reset(instr_fetch_reset),
    .io_wb_if_ready(instr_fetch_io_wb_if_ready),
    .io_wb_if_valid(instr_fetch_io_wb_if_valid),
    .io_pc(instr_fetch_io_pc),
    .io_if_id_valid(instr_fetch_io_if_id_valid),
    .io_if_id_bits_instr(instr_fetch_io_if_id_bits_instr)
  );
  InstrDecode instr_decode ( // @[top.scala 21:30]
    .clock(instr_decode_clock),
    .io_if_id_ready(instr_decode_io_if_id_ready),
    .io_if_id_valid(instr_decode_io_if_id_valid),
    .io_if_id_bits_instr(instr_decode_io_if_id_bits_instr),
    .io_out_gpr_read_rs_addr(instr_decode_io_out_gpr_read_rs_addr),
    .io_out_gpr_read_rt_addr(instr_decode_io_out_gpr_read_rt_addr),
    .io_id_isu_valid(instr_decode_io_id_isu_valid),
    .io_id_isu_bits_rd_addr(instr_decode_io_id_isu_bits_rd_addr),
    .io_id_isu_bits_imm(instr_decode_io_id_isu_bits_imm),
    .io_id_isu_bits_shamt_rs_sel(instr_decode_io_id_isu_bits_shamt_rs_sel),
    .io_id_isu_bits_shamt(instr_decode_io_id_isu_bits_shamt),
    .io_id_isu_bits_sign_ext(instr_decode_io_id_isu_bits_sign_ext),
    .io_id_isu_bits_op(instr_decode_io_id_isu_bits_op),
    .io_id_isu_bits_imm_rt_sel(instr_decode_io_id_isu_bits_imm_rt_sel)
  );
  ISU instr_shoot ( // @[top.scala 22:29]
    .clock(instr_shoot_clock),
    .io_gpr_data_rs_data(instr_shoot_io_gpr_data_rs_data),
    .io_gpr_data_rt_data(instr_shoot_io_gpr_data_rt_data),
    .io_id_isu_ready(instr_shoot_io_id_isu_ready),
    .io_id_isu_valid(instr_shoot_io_id_isu_valid),
    .io_id_isu_bits_rd_addr(instr_shoot_io_id_isu_bits_rd_addr),
    .io_id_isu_bits_imm(instr_shoot_io_id_isu_bits_imm),
    .io_id_isu_bits_shamt_rs_sel(instr_shoot_io_id_isu_bits_shamt_rs_sel),
    .io_id_isu_bits_shamt(instr_shoot_io_id_isu_bits_shamt),
    .io_id_isu_bits_sign_ext(instr_shoot_io_id_isu_bits_sign_ext),
    .io_id_isu_bits_op(instr_shoot_io_id_isu_bits_op),
    .io_id_isu_bits_imm_rt_sel(instr_shoot_io_id_isu_bits_imm_rt_sel),
    .io_isu_alu_valid(instr_shoot_io_isu_alu_valid),
    .io_isu_alu_bits_alu_op(instr_shoot_io_isu_alu_bits_alu_op),
    .io_isu_alu_bits_operand_1(instr_shoot_io_isu_alu_bits_operand_1),
    .io_isu_alu_bits_operand_2(instr_shoot_io_isu_alu_bits_operand_2),
    .io_isu_alu_bits_rd_addr(instr_shoot_io_isu_alu_bits_rd_addr)
  );
  ALU alu ( // @[top.scala 24:21]
    .clock(alu_clock),
    .io_isu_alu_ready(alu_io_isu_alu_ready),
    .io_isu_alu_valid(alu_io_isu_alu_valid),
    .io_isu_alu_bits_alu_op(alu_io_isu_alu_bits_alu_op),
    .io_isu_alu_bits_operand_1(alu_io_isu_alu_bits_operand_1),
    .io_isu_alu_bits_operand_2(alu_io_isu_alu_bits_operand_2),
    .io_isu_alu_bits_rd_addr(alu_io_isu_alu_bits_rd_addr),
    .io_out_ALU_out(alu_io_out_ALU_out),
    .io_out_Overflow_out(alu_io_out_Overflow_out),
    .io_exec_wb_valid(alu_io_exec_wb_valid),
    .io_exec_wb_bits_w_addr(alu_io_exec_wb_bits_w_addr)
  );
  WriteBack write_back ( // @[top.scala 26:28]
    .clock(write_back_clock),
    .io_exec_wb_ready(write_back_io_exec_wb_ready),
    .io_exec_wb_valid(write_back_io_exec_wb_valid),
    .io_exec_wb_bits_w_addr(write_back_io_exec_wb_bits_w_addr),
    .io_alu_output_ALU_out(write_back_io_alu_output_ALU_out),
    .io_alu_output_Overflow_out(write_back_io_alu_output_Overflow_out),
    .io_wb_if_valid(write_back_io_wb_if_valid),
    .io_gpr_wr_w_en(write_back_io_gpr_wr_w_en),
    .io_gpr_wr_addr(write_back_io_gpr_wr_addr),
    .io_gpr_wr_data(write_back_io_gpr_wr_data)
  );
  assign io_commit_valid = 1'h0;
  assign io_commit_pc = instr_fetch_io_pc; // @[top.scala 49:18]
  assign io_commit_instr = 32'h0;
  assign io_commit_ip7 = 1'h0;
  assign io_commit_gpr_0 = gprs_io_gpr_commit_0; // @[top.scala 47:26]
  assign io_commit_gpr_1 = gprs_io_gpr_commit_1; // @[top.scala 47:26]
  assign io_commit_gpr_2 = gprs_io_gpr_commit_2; // @[top.scala 47:26]
  assign io_commit_gpr_3 = gprs_io_gpr_commit_3; // @[top.scala 47:26]
  assign io_commit_gpr_4 = gprs_io_gpr_commit_4; // @[top.scala 47:26]
  assign io_commit_gpr_5 = gprs_io_gpr_commit_5; // @[top.scala 47:26]
  assign io_commit_gpr_6 = gprs_io_gpr_commit_6; // @[top.scala 47:26]
  assign io_commit_gpr_7 = gprs_io_gpr_commit_7; // @[top.scala 47:26]
  assign io_commit_gpr_8 = gprs_io_gpr_commit_8; // @[top.scala 47:26]
  assign io_commit_gpr_9 = gprs_io_gpr_commit_9; // @[top.scala 47:26]
  assign io_commit_gpr_10 = gprs_io_gpr_commit_10; // @[top.scala 47:26]
  assign io_commit_gpr_11 = gprs_io_gpr_commit_11; // @[top.scala 47:26]
  assign io_commit_gpr_12 = gprs_io_gpr_commit_12; // @[top.scala 47:26]
  assign io_commit_gpr_13 = gprs_io_gpr_commit_13; // @[top.scala 47:26]
  assign io_commit_gpr_14 = gprs_io_gpr_commit_14; // @[top.scala 47:26]
  assign io_commit_gpr_15 = gprs_io_gpr_commit_15; // @[top.scala 47:26]
  assign io_commit_gpr_16 = gprs_io_gpr_commit_16; // @[top.scala 47:26]
  assign io_commit_gpr_17 = gprs_io_gpr_commit_17; // @[top.scala 47:26]
  assign io_commit_gpr_18 = gprs_io_gpr_commit_18; // @[top.scala 47:26]
  assign io_commit_gpr_19 = gprs_io_gpr_commit_19; // @[top.scala 47:26]
  assign io_commit_gpr_20 = gprs_io_gpr_commit_20; // @[top.scala 47:26]
  assign io_commit_gpr_21 = gprs_io_gpr_commit_21; // @[top.scala 47:26]
  assign io_commit_gpr_22 = gprs_io_gpr_commit_22; // @[top.scala 47:26]
  assign io_commit_gpr_23 = gprs_io_gpr_commit_23; // @[top.scala 47:26]
  assign io_commit_gpr_24 = gprs_io_gpr_commit_24; // @[top.scala 47:26]
  assign io_commit_gpr_25 = gprs_io_gpr_commit_25; // @[top.scala 47:26]
  assign io_commit_gpr_26 = gprs_io_gpr_commit_26; // @[top.scala 47:26]
  assign io_commit_gpr_27 = gprs_io_gpr_commit_27; // @[top.scala 47:26]
  assign io_commit_gpr_28 = gprs_io_gpr_commit_28; // @[top.scala 47:26]
  assign io_commit_gpr_29 = gprs_io_gpr_commit_29; // @[top.scala 47:26]
  assign io_commit_gpr_30 = gprs_io_gpr_commit_30; // @[top.scala 47:26]
  assign io_commit_gpr_31 = gprs_io_gpr_commit_31; // @[top.scala 47:26]
  assign io_commit_rd_idx = 5'h0;
  assign io_commit_wdata = 32'h0;
  assign io_commit_wen = 1'h0;
  assign gprs_clock = clock;
  assign gprs_reset = reset;
  assign gprs_io_read_in_rs_addr = instr_decode_io_out_gpr_read_rs_addr; // @[top.scala 33:21]
  assign gprs_io_read_in_rt_addr = instr_decode_io_out_gpr_read_rt_addr; // @[top.scala 33:21]
  assign gprs_io_write_in_w_en = write_back_io_gpr_wr_w_en; // @[top.scala 43:22]
  assign gprs_io_write_in_addr = write_back_io_gpr_wr_addr; // @[top.scala 43:22]
  assign gprs_io_write_in_data = write_back_io_gpr_wr_data; // @[top.scala 43:22]
  assign instr_fetch_clock = clock;
  assign instr_fetch_reset = reset;
  assign instr_fetch_io_wb_if_valid = write_back_io_wb_if_valid; // @[top.scala 29:26]
  assign instr_decode_clock = clock;
  assign instr_decode_io_if_id_valid = instr_fetch_io_if_id_valid; // @[top.scala 31:27]
  assign instr_decode_io_if_id_bits_instr = instr_fetch_io_if_id_bits_instr; // @[top.scala 31:27]
  assign instr_shoot_clock = clock;
  assign instr_shoot_io_gpr_data_rs_data = gprs_io_read_out_rs_data; // @[top.scala 36:29]
  assign instr_shoot_io_gpr_data_rt_data = gprs_io_read_out_rt_data; // @[top.scala 36:29]
  assign instr_shoot_io_id_isu_valid = instr_decode_io_id_isu_valid; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_rd_addr = instr_decode_io_id_isu_bits_rd_addr; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_imm = instr_decode_io_id_isu_bits_imm; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_shamt_rs_sel = instr_decode_io_id_isu_bits_shamt_rs_sel; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_shamt = instr_decode_io_id_isu_bits_shamt; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_sign_ext = instr_decode_io_id_isu_bits_sign_ext; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_op = instr_decode_io_id_isu_bits_op; // @[top.scala 35:27]
  assign instr_shoot_io_id_isu_bits_imm_rt_sel = instr_decode_io_id_isu_bits_imm_rt_sel; // @[top.scala 35:27]
  assign alu_clock = clock;
  assign alu_io_isu_alu_valid = instr_shoot_io_isu_alu_valid; // @[top.scala 38:20]
  assign alu_io_isu_alu_bits_alu_op = instr_shoot_io_isu_alu_bits_alu_op; // @[top.scala 38:20]
  assign alu_io_isu_alu_bits_operand_1 = instr_shoot_io_isu_alu_bits_operand_1; // @[top.scala 38:20]
  assign alu_io_isu_alu_bits_operand_2 = instr_shoot_io_isu_alu_bits_operand_2; // @[top.scala 38:20]
  assign alu_io_isu_alu_bits_rd_addr = instr_shoot_io_isu_alu_bits_rd_addr; // @[top.scala 38:20]
  assign write_back_clock = clock;
  assign write_back_io_exec_wb_valid = alu_io_exec_wb_valid; // @[top.scala 40:27]
  assign write_back_io_exec_wb_bits_w_addr = alu_io_exec_wb_bits_w_addr; // @[top.scala 40:27]
  assign write_back_io_alu_output_ALU_out = alu_io_out_ALU_out; // @[top.scala 41:30]
  assign write_back_io_alu_output_Overflow_out = alu_io_out_Overflow_out; // @[top.scala 41:30]
endmodule
