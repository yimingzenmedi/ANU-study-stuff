import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testEmptyBrackets() {
        assertTrue(new Parser(new Tokeniser("()")).parseExp());
        System.out.println("1 Pass");
        assertFalse(new Parser(new Tokeniser("())")).parseExp());
    }

    @Test
    public void testEmptyBracketsWithNextExp() {
        assertFalse(new Parser(new Tokeniser("()())")).parseExp());
        assertTrue(new Parser(new Tokeniser("()()")).parseExp());
    }

    @Test
    public void testNonEmptyBrackets() {
        assertTrue(new Parser(new Tokeniser(")()(")).parseExp());
        assertFalse(new Parser(new Tokeniser(")()((")).parseExp());
    }

    @Test
    public void testNonEmptyBracketsWithNextExp() {
        assertFalse(new Parser(new Tokeniser(")()((")).parseExp());
//        System.out.println("1");
        assertTrue(new Parser(new Tokeniser(")()(()")).parseExp());
    }

    @Test
    public void testNestedExpressionsAndInvalidTokens() {
        assertTrue(new Parser(new Tokeniser("))()()((")).parseExp());
//        assertFalse(new Parser(new Tokeniser("))()()((())")).parseExp());
//        assertFalse(new Parser(new Tokeniser("x))()()((())")).parseExp());
//        assertFalse(new Parser(new Tokeniser("))()()((())x")).parseExp());
//        assertFalse(new Parser(new Tokeniser(")y(")).parseExp());
    }

//    @Test
//    public void test() {
//        Parser p = new Parser(new Tokeniser("))()()(("));
//        p._tokeniser.nextNext();
//        p._tokeniser.nextNext();
//        System.out.println(p._tokeniser.next().originalTokenStr);
//        p._tokeniser.takeNext();
//        System.out.println(p._tokeniser.next().originalTokenStr);
//
//    }
}
