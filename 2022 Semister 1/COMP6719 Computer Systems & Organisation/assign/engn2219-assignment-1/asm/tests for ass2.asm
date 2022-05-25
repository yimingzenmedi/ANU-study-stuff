movl r1, 0xf    ; 0x0109
movl r2, 0x2
; movl r3, 1 
; ldr r3, [r1]
add r4, r2, r1
sub r4, r2, r1
sub r4, r1, r2
and r4, r2, r1
orr r4, r2, r1
str r4, [r1]

.word 0
.word 0
.word 0
.word 0
.word 0xFF