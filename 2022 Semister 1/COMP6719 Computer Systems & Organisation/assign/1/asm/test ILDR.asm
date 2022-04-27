; test ILDR:

movl r1, 0xF0
movl r2, 0xAB

str r2, [r1]

ildr r3, 0xF0 ; r3 should be 0xAB

movl r3, 0
ildrz r3, 0xF0 ; r3 should be 0

movl r4, 0
movl r3, 0

add r4, r4, r4 ; set FLAG_Z to 1


ildrz r3, 0xF0 ; r3 should be 0xAB


