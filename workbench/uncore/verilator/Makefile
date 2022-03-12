.PHONY: run-% project update clean

EMU_OBJ_DIR    := $(CORE_OBJ_DIR)
EMU_TOP_MODULE := $(CORE_TOP_MODULE)
EMU_TOP_V      := $(CORE_TOP_V)
EMU_SRC_DIR    := $(abspath uncore/$(UNCORE))
EMU_PREFIX     := $(UNCORE)_top
EMU_MK         := $(EMU_OBJ_DIR)/$(EMU_PREFIX).mk
EMU_BIN        := $(EMU_OBJ_DIR)/emulator
EMU_LIB_V      != find $(EMU_SRC_DIR) -name "*.v"
EMU_CXXFILES   != find $(EMU_SRC_DIR) -name "*.cpp"

ifeq ($(ASAN),1)
ASAN_CFLAGS := -fsanitize=address,undefined -Wformat -Werror=format-security -Werror=array-bounds
ASAN_LDFLAGS := -fsanitize=address,undefined
endif

EMU_CFLAGS := -I. -I $(MIPS32_NEMU_HOME)/include $(ASAN_CFLAGS)
EMU_LDFLAGS := $(MIPS32_NEMU_LIB) -lpthread -lreadline -lSDL $(ASAN_LDFLAGS)

$(EMU_BIN): $(EMU_MK) $(EMU_CXXFILES)
	@echo + $(EMU_BIN)
	@cd $(@D) && make -s -f $(notdir $<)
	@touch $<

$(EMU_MK): $(EMU_TOP_V) $(EMU_CXXFILES) $(EMU_LIB_V)
	@mkdir -p $(@D)
	@verilator -Wno-lint --cc --exe \
	  --top-module $(EMU_TOP_MODULE) \
	  -o $(notdir $(EMU_BIN)) -Mdir $(@D) \
	  -CFLAGS "$(EMU_CFLAGS)" -LDFLAGS "$(EMU_LDFLAGS)" \
	  --prefix $(EMU_PREFIX) $^ 

project: $(EMU_BIN)

run-%: project $$($$*_ELF)
	@cd $($*_OBJDIR) && \
	  ln -sf $(abspath $(EMU_BIN)) emulator && \
	  ./emulator -b -e ./$*.elf 2> npc.out

update: $(EMU_MK)
	@rm -rf $(EMU_BIN)
	@echo + $(EMU_BIN)
	@cd $(dir $(EMU_BIN)) && make -s -f $(notdir $<)
	@touch $<

clean:
	rm -rI $(OBJ_DIR)/$(UNCORE)
