SCALA_DIR   := core
SCALA_FILES != find $(SCALA_DIR) -name "*.scala"

CORE_OBJ_DIR := $(OBJ_DIR)/$(UNCORE)
CORE_TOP_MODULE ?= $(UNCORE)_top
CORE_TOP_V := $(CORE_OBJ_DIR)/$(CORE_TOP_MODULE).v

$(CORE_TOP_V): $(SCALA_FILES)
	@mkdir -p $(@D)
	@$(SBT) "run $(CORE_TOP_MODULE) -td $(@D) --output-file $(@F)"
	@sed -i 1i'`define RANDOMIZE_GARBAGE_ASSIGN' $@
	@sed -i 1i'`define RANDOMIZE_INVALID_ASSIGN' $@
	@sed -i 1i'`define RANDOMIZE_REG_INIT' $@
	@sed -i 1i'`define RANDOMIZE_MEM_INIT' $@
