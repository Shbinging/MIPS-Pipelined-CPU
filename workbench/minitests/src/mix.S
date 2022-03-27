    .globl _start
_start:
    lui $3, 0xffe8
    lui $2, 0xffe8
    addiu $5, $0, 0x1234
    addu $4, $3, $5
    addi $4, $3, 0x3211
    or $4, $2, $3
    and $4, $2, $3
    xor $4, $2, $3
    nor $4, $2, $3
    slt $4, $2, $3
    slti $5, $3, 0x1234
    sltiu $5, $2, 0x1234
