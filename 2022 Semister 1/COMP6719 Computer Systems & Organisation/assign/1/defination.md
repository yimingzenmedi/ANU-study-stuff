## I-Mode instructions:

| Machine Code | Syntax | Comment |
| ---- | ---- | ---- |
| 0000 z&lt;rd&gt; &lt;imm8&gt; | movl rd, imm8 |
| 0001 z&lt;rd&gt; &lt;imm8&gt; | seth rd, imm8 |
| 0010 xxxx xxxx xxxx| - |
| 0011 z&lt;rd&gt; &lt;imm8&gt; | ildr rd, imm8 | I-Mode Memory Instructions |


## R-Mode Memory Instructions:

| Machine Code | Syntax | Comment |
| ---- | ---- | ---- |
| 0100 z&lt;rd&gt; 0&lt;ra&gt; 0000 | str rd, [ra] |
| 0101 z&lt;rd&gt; 0&lt;ra&gt; 0000 | ldr rd, [ra] |
| 0110 xxxx xxxx xxxx| - |
| 0111 z&lt;rd&gt; 0&lt;ra&gt; 0&lt;rb&gt;| max rd, ra, rb |

## R-Mode ALU Instructions:

| Machine Code | Syntax | Comment |
| ---- | ---- | ---- |
| 1000 z&lt;rd&gt; 0&lt;ra&gt; 0&lt;rb&gt; | add rd, ra, rb |
| 1001 z&lt;rd&gt; 0&lt;ra&gt; 0&lt;rb&gt; | sub rd, ra, rb |
| 1010 z&lt;rd&gt; 0&lt;ra&gt; 0&lt;rb&gt; | and rd, ra, rb |
| 1011 z&lt;rd&gt; 0&lt;ra&gt; 0&lt;rb&gt; | orr rd, ra, rb |
| 1100 z&lt;rd&gt; 0&lt;ra&gt; 0&lt;rb&gt; | xor rd, ra, rb | ALU Instruction XOR |
| 1101 z&lt;rd&gt; 0&lt;ra&gt; m imm3 | lsft rd, ra, imm3 | ALU bit shift to left for imm3 bits. Set mode (m) to 0 for rotate mode, 1 for logical mode. |
| 1110 z&lt;rd&gt; 0&lt;ra&gt; m imm3 | rsft rd, ra, imm3 | ALU bit shift to right for imm3 bits. Set mode (m) to 0 for rotate mode, 1 for logical mode. |
| 1111 z&lt;rd&gt; 0&lt;ra&gt; 0000 | not rd, ra | ALU Instruction NOT |
| 1111 z&lt;rd&gt; 0&lt;ra&gt; 0010 | inc rd, ra | ALU Instruction INC |
| 1111 z&lt;rd&gt; 0&lt;ra&gt; 0011 | dec rd, ra | ALU Instruction DEC |

