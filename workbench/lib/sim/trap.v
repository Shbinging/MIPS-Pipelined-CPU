module trap (                     
    input             aclk,          
    input             aresetn,     
    // read and write from cpu
    //ar
    input  [3 :0] arid   ,
    input  [31:0] araddr ,
    input  [7 :0] arlen  ,
    input  [2 :0] arsize ,
    input  [1 :0] arburst,
    input  [1 :0] arlock ,
    input  [3 :0] arcache,
    input  [2 :0] arprot ,
    input         arvalid,
    output        arready,
    //r
    output [3 :0] rid    ,
    output [31:0] rdata  ,
    output [1 :0] rresp  ,
    output        rlast  ,
    output        rvalid ,
    input         rready ,
    //aw
    input  [3 :0] awid   ,
    input  [31:0] awaddr ,
    input  [7 :0] awlen  ,
    input  [2 :0] awsize ,
    input  [1 :0] awburst,
    input  [1 :0] awlock ,
    input  [3 :0] awcache,
    input  [2 :0] awprot ,
    input         awvalid,
    output        awready,
    //w
    input  [3 :0] wid    ,
    input  [31:0] wdata  ,
    input  [3 :0] wstrb  ,
    input         wlast  ,
    input         wvalid ,
    output        wready ,
    //b
    output [3 :0] bid    ,
    output [1 :0] bresp  ,
    output        bvalid ,
    input         bready
);

assign arready = 1'd1;
assign rvalid = 1'd0;
assign awready = 1'd1;
assign wready = 1'd1;
assign bvalid = 1'd0;

reg w_retire;
reg aw_retire;
reg [31:0]buf_wdata;
reg [31:0]buf_waddr;

always @(posedge aclk)
begin
  if (!aresetn) begin
    w_retire <= 1'd0;
    aw_retire <= 1'd0;
    buf_wdata <= 32'd0;
    buf_waddr <= 32'd0;
  end else begin
    if (wvalid) begin
      w_retire <= 1'd1;
      buf_wdata <= wdata;
    end
    if (awvalid) begin
      aw_retire <= 1'd1;
      buf_waddr <= awaddr;
    end
    
    if (w_retire && aw_retire) begin
      if (buf_waddr != 32'h10000000) begin
        $display("BAD TRAP ADDR 0x%08x\n", buf_waddr);
      end else begin
        if (buf_wdata == 32'd0) begin
          $display("HIT GOOD TRAP");
        end else begin
          $display("HIT BAD TRAP");
        end
      end
      $finish;
    end
  end
end

endmodule
