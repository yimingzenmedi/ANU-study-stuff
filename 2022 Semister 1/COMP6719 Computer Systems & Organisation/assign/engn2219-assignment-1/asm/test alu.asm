; test ALU:
movl r1, 0x02
movl r2, 0x03

;  flag_z = 0: do not execute
addz r3, r1, r2

notz r4, r3

movl r1, 0xFF
xorz r4, r3, r1

decz r4, r3
incz r4, r3


; flag_z = 1: execute
sub r4, r4, r4
addz r3, r1, r2

sub r4, r4, r4
notz r4, r3

movl r1, 0xFF
; sub r4, r4, r4
xorz r4, r3, r1

sub r4, r4, r4
decz r4, r3

sub r4, r4, r4
incz r4, r3

; ignore flag_z: execute
sub r4, r4, r4
add r3, r1, r2

sub r4, r4, r4
not r4, r3


movl r1, 0xFF
; sub r4, r4, r4
xor r4, r3, r1

sub r4, r4, r4
dec r4, r3

sub r4, r4, r4
inc r4, r3