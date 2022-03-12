.PHONY: run-% prj

run-%: $(MIPS32_NEMU) $$($$*_ELF)
	@cd $($*_OBJDIR) && $< -b -e ./$*.elf

project: $(MIPS32_NEMU)
update: ;
clean: ;
