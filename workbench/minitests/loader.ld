MEMORY {
  ddr : ORIGIN = 0xbfc00000, LENGTH = 1M
}

SECTIONS {
  .text : {
    *(loader)
    *(.text)
    etext = .;
    _etext = .;
  } > ddr
  .data : {
    *(.data)
    edata = .;
    _edata = .;
  } > ddr
  .bss : {
    *(.bss*)
    *(.sbss)
    *(.scommon)
    end = .;
    _end = .;
  } > ddr
  .MIPS.abiflags : {
	*(.MIPS.abiflags)
  } > ddr
  _heap_start = ALIGN(4096);
  _stack_pointer = ORIGIN(ddr) + LENGTH(ddr) - 32;
  _heap_end = _stack_pointer - 4 * 1024 * 1024;
}
