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
        Exp term = null, exp = null;
        System.out.println("\nparseExp");
        int round = 1;
        while (_tokenizer.hasNext()) {
            Token current = _tokenizer.current();
            Token.Type type = current.type();
            String token = current.token();
            System.out.println("\n" + round++ + " exp current token:" + token + ", type: " + type);
            System.out.println("exp status: exp: " + (exp == null ? null : exp.show()) + ", term: " + (term == null ? null : term.show()));


            if (type == Token.Type.ADD) {
                System.out.println("> next");
                _tokenizer.next();
                System.out.println("exp ADD: next: " + (_tokenizer.current() == null ? null : _tokenizer.current().token()));
                System.out.println("New exp_ init state: " + _tokenizer.current());
                Exp exp_ = parseExp();
                System.out.println("exp_:" + exp_.show());
                exp = new AddExp(term, exp_);
            } else if (type == Token.Type.SUB) {
                System.out.println("exp SUB:");
                System.out.println("> next");
                _tokenizer.next();
                exp = new SubExp(term, parseExp());
            } else {
                System.out.println("exp Others: ");
                term = parseTerm();
            }
//            System.out.println("exp res: exp: " + (exp == null ? null : exp.show()) + ", term: " + (term == null ? null : term.show()));
        }

        System.out.println("exp return: " +  (exp == null ? term == null? null : "term@ " + term.show() : "exp@ " + exp.show()));

        return exp == null ? term : exp;
        // ########## YOUR CODE ENDS HERE ##########
    }

    //<term>   ::=  <factor> | <factor> * <term> | <factor> / <term>
    public Exp parseTerm() {
        // TODO: Implement parse function for <term>
        // ########## YOUR CODE STARTS HERE ##########
        System.out.println("\nparseTerm");
        int round = 1;
        Exp term = null, factor = null;
        while (_tokenizer.hasNext()) {
            Token current = _tokenizer.current();
            Token.Type type = current.type();
            String token = current.token();
            System.out.println("\n" + round++ + " term current token:" + token + ", type: " + type);
            System.out.println("term status: term: " + (term == null ? null : term.show()) + ", factor: " + (factor == null ? null : factor.show()));

            if (type == Token.Type.ADD || type == Token.Type.SUB) {
                System.out.println("term back:");
//                System.out.println("term return: " +  (term == null ? factor == null? null : "factor@ " + factor.show() : "term@ " + term.show()));
//                return term;
                break;
            } else if (type == Token.Type.MUL) {
                System.out.println("term MUL:");
                System.out.println("> next");
                _tokenizer.next();
                term = new MultExp(factor, parseTerm());
            } else if (type == Token.Type.DIV) {
                System.out.println("term DIV:");
                System.out.println("> next");
                _tokenizer.next();
                term = new DivExp(factor, parseTerm());
            } else {
                System.out.println("term Others: ");
                factor = parseFactor();
            }
//            System.out.println("term res: term: " + (term == null ? null : term.show()) + ", factor: " + (factor == null ? null : factor.show()));
        }

//        System.out.println(-2);
        System.out.println("term return: " +  (term == null ? factor == null? null : "factor@ " + factor.show() : "term@ " + term.show()));

        return term == null ? factor : term;
        // ########## YOUR CODE ENDS HERE ##########
    }

    //<factor> ::= <unsigned integer> | ( <exp> )
    public Exp parseFactor() {
        // TODO: Implement parse function for <factor>
        // ########## YOUR CODE STARTS HERE ##########
        System.out.println("\nparseFactor");
        Exp factor = null, exp = null;
        int round = 1;
        while (_tokenizer.hasNext()) {
            Token current = _tokenizer.current();
            Token.Type type = current.type();
            String token = current.token();
            System.out.println("\n" + round++ + " factor current token:" + token + ", type: " + type);
            System.out.println("factor status: factor: " + (factor == null ? null : factor.show()) + ", exp: " + (exp == null ? null : exp.show()));

            if (type == Token.Type.INT) {
                System.out.println("factor Int");
                Integer val = Integer.parseInt(token);
                factor = new IntExp(val);
                System.out.println("> next");
                _tokenizer.next();
            } else if (type == Token.Type.LBRA) {
                System.out.println("factor LBRA");
                System.out.println("> next");
                _tokenizer.next();
                exp = parseExp();
            } else if (type == Token.Type.RBRA) {
                System.out.println("factor RBRA");
                System.out.println("> next");
                _tokenizer.next();
                return exp;
            } else {
                System.out.println("factor Others: break");
                break;
            }
        }
//        System.out.println("factor res: factor: " + (factor == null ? null : factor.show()) + ", exp: " + (exp == null ? null : exp.show()));
        System.out.println("factor return: " +  (factor == null ? exp == null? null : "exp@ " + exp.show() : "factor@ " + factor.show()));

//        System.out.println(-3);

        return factor == null ? exp : factor;
        // ########## YOUR CODE ENDS HERE ##########
    }

    public static void main(String[] args) {
        MyTokenizer mathTokenizer = new MyTokenizer("2+1");
        Exp t1 = new Parser(mathTokenizer).parseExp();
        System.out.println(t1.show());
        System.out.println(t1.evaluate());
    }
}
