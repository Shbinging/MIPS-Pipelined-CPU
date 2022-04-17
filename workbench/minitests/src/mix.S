    .globl _start
_start:
    lui	$1, 0x800f
    nop 
    nop 
    nop
 	ori	$1,$1,0x52a4
    nop 
    nop 
    nop 
    beq $0, $0, __label1
    sw  $2,15068($1)
__label1:
    addiu   $2,$1,4
    addiu   $3,$4,5
    addiu   $10,$11,324
    addiu   $12,$13,412
    addiu   $14,$15,123
    beq $0, $0, __label2
    mult    $2, $1
__label2:
    addiu   $3,$4,5
    addiu   $10,$11,324
    addiu   $12,$13,412
    addiu   $14,$15,123
    nop 
    nop 
    nop 
    nop 
