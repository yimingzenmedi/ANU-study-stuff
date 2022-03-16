/**
 * These test cases are provided to assist your understanding.
 * But these test cases are not used in the actual marking.
 * Please DO NOT only rely on the given test cases for debugging.
 * You should write your own test cases to debug your code.
 */

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class MyTokenizerTest {

	private static void generalTest(Tokenizer tokenizer, String text, Token.Type type) {
		assertEquals(text, tokenizer.current().token());
		assertEquals(type, tokenizer.current().type());

		tokenizer.next();
	}

	@Test(timeout = 1000)
	public void testSymbolsToken() {
		Tokenizer tokenizer = new MyTokenizer("+-*/^");

		List<Token> tokens = new LinkedList<>();
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("-", Token.Type.SUB));
		tokens.add(new Token("*", Token.Type.MUL));
		tokens.add(new Token("/", Token.Type.DIV));
		tokens.add(new Token("^", Token.Type.EXPO));

		for (Token t : tokens) {
			generalTest(tokenizer, t.token(), t.type());
		}
	}

	@Test(timeout = 1000)
	public void testAddSub() {
		Tokenizer tokenizer = new MyTokenizer("3 + 5 - 6 + 1");

		List<Token> tokens = new LinkedList<>();
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("5", Token.Type.INT));
		tokens.add(new Token("-", Token.Type.SUB));
		tokens.add(new Token("6", Token.Type.INT));
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("1", Token.Type.INT));

		for (Token t : tokens) {
			generalTest(tokenizer, t.token(), t.type());
		}
	}

	@Test(timeout = 1000)
	public void testMulDivExpo() {
		Tokenizer tokenizer = new MyTokenizer("5 * 4 / 2^3");

		List<Token> tokens = new LinkedList<>();
		tokens.add(new Token("5", Token.Type.INT));
		tokens.add(new Token("*", Token.Type.MUL));
		tokens.add(new Token("4", Token.Type.INT));
		tokens.add(new Token("/", Token.Type.DIV));
		tokens.add(new Token("2", Token.Type.INT));
		tokens.add(new Token("^", Token.Type.EXPO));
		tokens.add(new Token("3", Token.Type.INT));

		for (Token t : tokens) {
			generalTest(tokenizer, t.token(), t.type());
		}
	}

	@Test(timeout = 1000)
	public void testComplexExpression1() {
		Tokenizer tokenizer = new MyTokenizer("(((3 + 5) * 4 / 2)^3 - 13) * 21 / 3");

		List<Token> tokens = new LinkedList<>();
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("5", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("*", Token.Type.MUL));
		tokens.add(new Token("4", Token.Type.INT));
		tokens.add(new Token("/", Token.Type.DIV));
		tokens.add(new Token("2", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("^", Token.Type.EXPO));
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("-", Token.Type.SUB));
		tokens.add(new Token("13", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("*", Token.Type.MUL));
		tokens.add(new Token("21", Token.Type.INT));
		tokens.add(new Token("/", Token.Type.DIV));
		tokens.add(new Token("3", Token.Type.INT));

		for (Token t : tokens) {
			generalTest(tokenizer, t.token(), t.type());
		}
	}

	@Test(timeout = 1000)
	public void testComplexExpression2() {
		Tokenizer tokenizer = new MyTokenizer("(((3 + 5) * 4 / 2)^3 - 13) * 21 / 3 + 5^3 - (5 + 6)^2");

		List<Token> tokens = new LinkedList<>();
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("5", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("*", Token.Type.MUL));
		tokens.add(new Token("4", Token.Type.INT));
		tokens.add(new Token("/", Token.Type.DIV));
		tokens.add(new Token("2", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("^", Token.Type.EXPO));
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("-", Token.Type.SUB));
		tokens.add(new Token("13", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("*", Token.Type.MUL));
		tokens.add(new Token("21", Token.Type.INT));
		tokens.add(new Token("/", Token.Type.DIV));
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("5", Token.Type.INT));
		tokens.add(new Token("^", Token.Type.EXPO));
		tokens.add(new Token("3", Token.Type.INT));
		tokens.add(new Token("-", Token.Type.SUB));
		tokens.add(new Token("(", Token.Type.LBRA));
		tokens.add(new Token("5", Token.Type.INT));
		tokens.add(new Token("+", Token.Type.ADD));
		tokens.add(new Token("6", Token.Type.INT));
		tokens.add(new Token(")", Token.Type.RBRA));
		tokens.add(new Token("^", Token.Type.EXPO));
		tokens.add(new Token("2", Token.Type.INT));

		for (Token t : tokens) {
			generalTest(tokenizer, t.token(), t.type());
		}
	}
}
