# note INPUT=REF is designed for microbench
# arg1 dir
# arg2 name eg. videotest microbench
# arg3 objdir default: obj/name
# arg4 additional env
# arg5 orig_app
# arg6 orig_app deps
define test_template
ifneq ($(5),)
$(2)_ORIG_APP := $(1)/$(5)
else
$(2)_ORIG_APP := $(1)/build/$(2)-$(ARCH).elf
endif

ifneq ($(3),)
$(2)_OBJDIR := $(3)
else
$(2)_OBJDIR := $(OBJ_DIR)/$(2)
endif

ifeq ($(6),!)
$(2)_DEPS :=
else
$(2)_DEPS != find $(1) -regex ".*\.\(c\|h\|cc\|cpp\|S\)"
endif

$(2)_ELF  := $$($(2)_OBJDIR)/$(2).elf

.PHONY: compile-$(2) clean-$(2)

$$($(2)_ORIG_APP): $$($(2)_DEPS)
	@make -s -C $(1) ARCH=$(ARCH)

compile-$(2): $$($(2)_OBJDIR)/$(2).elf

$$($(2)_ELF): $$($(2)_ORIG_APP)
	@mkdir -p $$($(2)_OBJDIR)
	@cd $$($(2)_OBJDIR); ln -sf $$^ $$(@F);  \
		$(CROSS_COMPILE)objdump -d $$(@F) > $(2).S

$$($(2)_OBJDIR)/bram.coe $$($(2)_OBJDIR)/ddr.coe: \
  $$($(2)_ELF) $(ELF2COE)
	@cd $$($(2)_OBJDIR) && \
	  $(abspath $(ELF2COE)) -e $(2).elf \
		-s ddr.coe:0x80000000:1048576 \
		-s bram.coe:0xbfc00000:1048576

$$($(2)_OBJDIR)/trace.txt: $$($(2)_ELF)
	@cd $$($(2)_OBJDIR) && \
	  $(MIPS32_NEMU) -b -e $(2).elf -c 2> $$(@F)

clean-$(2):
	@make -s -C $(1) ARCH=$(ARCH) clean
endef
