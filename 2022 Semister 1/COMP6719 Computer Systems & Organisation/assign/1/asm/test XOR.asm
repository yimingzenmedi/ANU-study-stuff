; test ILDR:

movl r1, 0xFF
movl r2, 0xAB

xor r3, r1, r2 ; r3 should be 0x54.

movl r1, 0
movl r2, 0xAB

xor r3, r1, r2 ; r3 should be 0xAB