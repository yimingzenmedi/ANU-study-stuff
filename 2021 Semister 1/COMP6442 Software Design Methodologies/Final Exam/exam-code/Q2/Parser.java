/**
 * The given code is provided to assist you to complete the required tasks. But
 * the given code is often incomplete. You have to read and understand the given
 * code carefully, before you can apply the code properly. You might need to
 * implement additional procedures, such as error checking and handling, in
 * order to apply the code properly.
 */

public class Parser {

	private final Tokenizer tokenizer;

	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public Exp parseExp() {

		Exp result = null;

		// ########## YOUR CODE STARTS HERE ##########

		while (tokenizer.hasNext()) {
			String token = tokenizer.current().token();
			Token.Type type = tokenizer.current().type();
			System.out.println(
					"exp current status: exp: " + (result == null ? null : result.show())
							+ ", token: " + token + ", type: " + type
			);
			if (type == Token.Type.ADD) {
				tokenizer.next();
				result = new AddExp(result, parseExp());
			} else if (type == Token.Type.SUB) {
				tokenizer.next();
				result = new SubExp(result, parseExp());
			} else if (type == Token.Type.RBRA) {
				break;
			} else {
				result = parseTerm();
			}
		}

		System.out.println("result return: " + (result == null ? null : result.show()));

		// ########## YOUR CODE ENDS HERE ##########

		return result;
	}


	public Exp parseTerm() {
		// TODO: Implement parse function for <term>
		// ########## YOUR CODE STARTS HERE ##########
		System.out.println("\nparseTerm");
		Exp term = null;

		while (tokenizer.hasNext()) {
			String token = tokenizer.current().token();
			Token.Type type = tokenizer.current().type();
			System.out.println(
					"term current status: term: " + (term == null ? null : term.show())
							+ ", token: " + token + ", type: " + type
			);

			if (type == Token.Type.ADD || type == Token.Type.SUB) {
				break;
			}
			else if (type == Token.Type.EXPO) {
				tokenizer.next();
				term = new ExpoExp(term, parseTerm());
			}
			else if (type == Token.Type.MUL) {
				tokenizer.next();
				term = new MulExp(term, parseTerm());
			} else if (type == Token.Type.DIV) {
				tokenizer.next();
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

		while (tokenizer.hasNext()) {
			String token = tokenizer.current().token();
			Token.Type type = tokenizer.current().type();
			System.out.println(
					"factor current status: factor: " + (factor == null ? null : factor.show())
							+ ", token: " + token + ", type: " + type
			);
			if (type == Token.Type.INT) {
				Integer val = Integer.valueOf(token);
				System.out.println("Int val: " + val);
				factor = new IntExp(val);
				tokenizer.next();
			} else if (type == Token.Type.LBRA) {
				tokenizer.next();
				factor = parseExp();
				System.out.println("(exp): " + factor.show());
				tokenizer.next();
				return factor;
			} else if (type == Token.Type.RBRA) {
//                tokenizer.next();
				break;
			} else {
				break;
			}
		}
		System.out.println("factor return: " + (factor == null ? null : factor.show()));
		return factor;
		// ########## YOUR CODE ENDS HERE ##########
	}

}
