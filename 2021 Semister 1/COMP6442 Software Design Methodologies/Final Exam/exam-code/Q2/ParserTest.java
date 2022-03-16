/**
 * These test cases are provided to assist your understanding.
 * But these test cases are not used in the actual marking.
 * Please DO NOT only rely on the given test cases for debugging.
 * You should write your own test cases to debug your code.
 */

 import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {

	@Test(timeout = 1000)
	public void testSimpleAddSub() {
		Tokenizer tokenizer = new MyTokenizer("1 + 2 + 3 + 4 - 5");
		Exp exp = new Parser(tokenizer).parseExp();
		assertEquals(5, exp.evaluate());
	}

	@Test(timeout = 1000)
	public void testSimpleMulDivExpo() {
		Tokenizer tokenizer = new MyTokenizer("(2 * 16 / 4)^2");
		Exp exp = new Parser(tokenizer).parseExp();
		assertEquals(64, exp.evaluate());
	}

	@Test(timeout = 1000)
	public void testSimpleCase() {
		Tokenizer tokenizer = new MyTokenizer("3 * 4 + 9 / 3^2");
		Exp exp = new Parser(tokenizer).parseExp();
		assertEquals("incorrect display format", "((3 * 4) + (9 / (3 ^ 2)))", exp.show());
		assertEquals("incorrect evaluate value", 13, exp.evaluate());
	}

	@Test(timeout = 1000)
	public void testMediumCase() {
		Tokenizer tokenizer = new MyTokenizer("(2 + 3)^(1 + 3) * 10 - (5 - 2)^(2 * 2)");
		Exp exp = new Parser(tokenizer).parseExp();
		assertEquals("incorrect display format", "((((2 + 3) ^ (1 + 3)) * 10) - ((5 - 2) ^ (2 * 2)))", exp.show());
		assertEquals("incorrect evaluate value", 6169, exp.evaluate());
	}

	@Test(timeout = 1000)
	public void testComplexCase() {
		Tokenizer tokenizer = new MyTokenizer("((10 - 8) * (10 / 5) + 1)^(1 + (1 + 1)^(3^3 - 4 * 6))");
		Exp exp = new Parser(tokenizer).parseExp();
		assertEquals("incorrect display format", "((((10 - 8) * (10 / 5)) + 1) ^ (1 + ((1 + 1) ^ ((3 ^ 3) - (4 * 6)))))", exp.show());
		assertEquals("incorrect evaluate value", 1953125, exp.evaluate());
	}
}
