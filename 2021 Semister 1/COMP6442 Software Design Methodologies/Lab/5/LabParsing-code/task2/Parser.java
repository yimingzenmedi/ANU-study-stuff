/**
 * Name: Parser.java
 *
 *  The main objective of this class is to implement a simple parser.
 *  It should be able to parser the following grammar rule:
 *  <exp>    ::= <term> | <term> + <exp> | <term> - <exp>
 *  <term>   ::=  <factor> | <factor> * <term> | <factor> / <term>
 *  <factor> ::= <unsigned integer> | ( <exp> )
 *
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly. You might need to implement
 * additional procedures, such as error checking and handling, in order to apply the
 * code properly.
 */

public class Parser {

    MyTokenizer _tokenizer;

    public Parser(MyTokenizer tokenizer) {
        _tokenizer = tokenizer;
    }

    /*
    <exp>    ::= <term> | <term> + <exp> | <term> - <exp>
     */
    public Exp parseExp() {
        // TODO: Implement parse function for <exp>
        // ########## YOUR CODE STARTS HERE ##########
        System.out.println("\nparseExp");

        Exp exp = null;

        while (_tokenizer.hasNext()) {
            String token = _tokenizer.current().token();
            Token.Type type = _tokenizer.current().type();
            System.out.println(
                    "exp current status: exp: " + (exp == null ? null : exp.show())
                            + ", token: " + token + ", type: " + type
            );
            if (type == Token.Type.ADD) {
                _tokenizer.next();
                exp = new AddExp(exp, parseExp());
            } else if (type == Token.Type.SUB) {
                _tokenizer.next();
                exp = new SubExp(exp, parseExp());
            } else if (type == Token.Type.RBRA) {
                break;
            } else {
                exp = parseTerm();
            }
        }

        System.out.println("exp return: " + (exp == null ? null : exp.show()));

        return exp;

        // ########## YOUR CODE ENDS HERE ##########
    }

    //<term>   ::=  <factor> | <factor> * <term> | <factor> / <term>
    public Exp parseTerm() {
        // TODO: Implement parse function for <term>
        // ########## YOUR CODE STARTS HERE ##########
        System.out.println("\nparseTerm");
        Exp term = null;

        while (_tokenizer.hasNext()) {
            String token = _tokenizer.current().token();
            Token.Type type = _tokenizer.current().type();
            System.out.println(
                    "term current status: term: " + (term == null ? null : term.show())
                            + ", token: " + token + ", type: " + type
            );
            if (type == Token.Type.ADD || type == Token.Type.SUB) {
                break;
            } else if (type == Token.Type.MUL) {
                _tokenizer.next();
                term = new MultExp(term, parseTerm());
            } else if (type == Token.Type.DIV) {
                _tokenizer.next();
                term = new DivExp(term, parseTerm());
            } else if (type == Token.Type.RBRA) {
                break;
            } else {
                term = parseFactor();
            }
        }

        System.out.println("term return: " + (term == null ? null : term.show()));

        return term;
        // ########## YOUR CODE ENDS HERE ##########
    }

    //<factor> ::= <unsigned integer> | ( <exp> )
    public Exp parseFactor() {
        // TODO: Implement parse function for <factor>
        // ########## YOUR CODE STARTS HERE ##########
        System.out.println("\nparseFactor");
        Exp factor = null;

        while (_tokenizer.hasNext()) {
            String token = _tokenizer.current().token();
            Token.Type type = _tokenizer.current().type();
            System.out.println(
                    "factor current status: factor: " + (factor == null ? null : factor.show())
                            + ", token: " + token + ", type: " + type
                    );
            if (type == Token.Type.INT) {
                Integer val = Integer.valueOf(token);
                System.out.println("Int val: " + val);
                factor = new IntExp(val);
                _tokenizer.next();
            } else if (type == Token.Type.LBRA) {
                _tokenizer.next();
                factor = parseExp();
                System.out.println("(exp): " + factor.show());
                _tokenizer.next();
                return factor;
            } else if (type == Token.Type.RBRA) {
//                _tokenizer.next();
                break;
            } else {
                break;
            }
        }
        System.out.println("factor return: " + (factor == null ? null : factor.show()));
        return factor;
        // ########## YOUR CODE ENDS HERE ##########
    }

    public static void main(String[] args) {
        MyTokenizer mathTokenizer = new MyTokenizer("2+1");
        Exp t1 = new Parser(mathTokenizer).parseExp();
        System.out.println(t1.show());
        System.out.println(t1.evaluate());
    }
}
