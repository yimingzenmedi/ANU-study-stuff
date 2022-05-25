; test MAX （find bigger register and store in rd):
movl r1, 0x02
movl r2, 0x03

sub r3, r1, r2

max r4, r1, r2