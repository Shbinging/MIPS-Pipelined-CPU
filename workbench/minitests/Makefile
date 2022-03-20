EMU_BIN  := "../output/verilator/emulator"

CFLAGS += -c -O2 -EL -g
# CFLAGS += -fno-pic -fno-builtin -nostdlib
# CFLAGS += -mno-llsc -mno-imadd -mno-mad
# CFLAGS += -mno-abicalls -g -Iinclude -I.

run-%: build/%.elf $(EMU_BIN)
	$(EMU_BIN) -e $<

$(EMU_BIN):
	cd .. && make 


build/%.elf: build/%.o
	mips-linux-gnu-ld $^ --gc-sections -EL -T ./loader.ld -e _start -o $@

build/%.o: src/%.S
	mkdir -p build
	mips-linux-gnu-gcc $(CFLAGS) $^ -o $@

clean: 
	rm -r build