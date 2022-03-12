.PHONY: clean-all minicom

.SUFFIXES:
.SECONDEXPANSION:

export ARCH             := mips32-npc
export CROSS_COMPILE    := mips-linux-gnu-
export AM_HOME          := $(PWD)/../nexus-am
export MIPS32_NEMU_HOME := $(PWD)/../nemu-mips32
export MIPS_TEST_HOME   := $(PWD)/../mipstest
export U_BOOT_HOME      := $(PWD)/../u-boot
export LINUX_HOME       := $(PWD)/../linux
export NANOS_HOME       := $(PWD)/../nanos

UNCORE     ?= verilator
UNCORE_DIR := uncore/$(UNCORE)
VIVADO     := vivado -nolog -nojournal -notrace
SBT        := sbt -mem 1000
OBJ_DIR    := output
ELF2COE    := $(OBJ_DIR)/elf2coe

$(ELF2COE): scripts/elf2coe.cc
	@g++ $< -o $@

include rules/core.mk
include rules/nemu.mk
include rules/testcases.mk
include $(UNCORE_DIR)/Makefile

.DEFAULT_GOAL := project

minicom:
	@cd $(OBJ_DIR) && sudo minicom -D /dev/ttyUSB1 \
	  -b 115200 -c on -C serial.log \
	  -S $(PWD)/scripts/minicom.script

clean-all:
	rm -rI $(OBJ_DIR)
