.PHONY: sim-% project update vivado bitstream clean

ZB_OBJDIR := $(abspath $(CORE_OBJ_DIR))
ZB_TOP_V  := $(ZB_OBJDIR)/src/zedboard_top.v
ZB_XPR    := $(ZB_OBJDIR)/zedboard.xpr

$(ZB_TOP_V): $(CORE_TOP_V)
	@mkdir -p $(@D)
	@cp $< $@
	@sed -i "s/_\(aw\|ar\|r\|w\|b\)_/_\1/g" $@

$(ZB_XPR):
	@rm -rf $(ZB_OBJDIR)
	@cp -r uncore/zedboard $(ZB_OBJDIR)

sim-%: compile-%

project: $(ZB_XPR) $(ZB_TOP_V)
update: $(ZB_TOP_V)
vivado: $(ZB_XPR)
	@cd $(dir $(ZB_XPR)) && nohup $(VIVADO) $(ZB_XPR) &
bitstream: project
	@SOC_XPR=mycpu.xpr SOC_DIR=$(dir $(ZB_XPR)) \
	  $(VIVADO) -mode batch -source $(UNCORE_DIR)/mk.tcl

clean:
	rm -rf $(ZB_OBJDIR)
