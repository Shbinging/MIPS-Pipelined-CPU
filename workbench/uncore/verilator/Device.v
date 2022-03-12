import "DPI-C" function void device_io
(
  input  bit  in_req_valid,
  input  int  in_req_bits_addr,
  input  int  in_req_bits_len,
  input  int  in_req_bits_data,
  input  byte in_req_bits_func,
  input  byte in_req_bits_strb,
  output int  in_resp_bits_data
);

module SimDev(
  input         clock,
  input         reset,
  output        in_req_ready,
  input         in_req_valid,
  input  [0:0]  in_req_bits_is_cached,
  input  [31:0] in_req_bits_addr,
  input  [1:0]  in_req_bits_len,
  input  [31:0] in_req_bits_data,
  input  [0:0]  in_req_bits_func,
  input  [3:0]  in_req_bits_strb,
  input         in_resp_ready,
  output        in_resp_valid,
  output [31:0] in_resp_bits_data
);

reg resp_valid;
int __in_resp_bits_data;
reg [31:0] resp_data;
wire in_req_fire;

assign in_resp_bits_data = resp_data;
assign in_req_ready = !resp_valid;
assign in_resp_valid = resp_valid;
assign in_req_fire = in_req_valid && in_req_ready;

always @(posedge clock)
begin
  if (!reset) begin
    device_io(
      in_req_fire,
      in_req_bits_addr,
      {30'b0, in_req_bits_len},
      in_req_bits_data,
      {7'b0, in_req_bits_func},
      {4'b0, in_req_bits_strb},
      __in_resp_bits_data
    );
    if (in_req_fire) begin
      resp_valid <= 1'b1;
      resp_data <= __in_resp_bits_data;
    end else if (resp_valid && in_resp_ready) begin
      resp_valid <= 1'b0;
    end
  end else begin
    resp_valid <= 1'b0;
  end
end

endmodule
