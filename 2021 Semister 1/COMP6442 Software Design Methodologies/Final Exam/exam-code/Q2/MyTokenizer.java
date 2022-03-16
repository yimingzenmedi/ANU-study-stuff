/**
 * The given code is provided to assist you to complete the required tasks. But
 * the given code is often incomplete. You have to read and understand the given
 * code carefully, before you can apply the code properly. You might need to
 * implement additional procedures, such as error checking and handling, in
 * order to apply the code properly.
 */

public class MyTokenizer extends Tokenizer {

	private String _buffer; // save text
	private Token currentToken; // save token extracted from next()

	/**
	 * Tokenizer class constructor The constructor extracts the first token and save
	 * it to currentToken **** please do not modify this part ****
	 */
	public MyTokenizer(String text) {
		this._buffer = text; // save input text (string)
		next(); // extracts the first token.
	}

	/**
	 * This function will find and extract a next token from {@code _buffer} and
	 * save the token to {@code currentToken}.
	 */
	public void next() {
		this._buffer = this._buffer.trim(); // remove whitespace

		if (this._buffer.isEmpty()) {
			this.currentToken = null; // if there's no string left, set currentToken null and return
			return;
		}

		// ########## YOUR CODE STARTS HERE ##########

		char firstChar = _buffer.charAt(0);
		System.out.println("is digit: " + Character.isDigit(firstChar));
		if(firstChar == '+')
			currentToken = new Token("+", Token.Type.ADD);
		if(firstChar == '-')
			currentToken = new Token("-", Token.Type.SUB);

		if (firstChar == '*') {
			currentToken = new Token("*", Token.Type.MUL);
		}

		if (firstChar == '/') {
			currentToken = new Token("/", Token.Type.DIV);
		}

		if (firstChar == '^') {
			currentToken = new Token("^", Token.Type.EXPO);
		}

		if (firstChar == '(') {
			currentToken = new Token("(", Token.Type.LBRA);
		}

		if (firstChar == ')') {
			currentToken = new Token(")", Token.Type.RBRA);
		}

		StringBuilder numStr = new StringBuilder();
		int counter = 0;
		while (Character.isDigit(firstChar) && counter < _buffer.length()) {
			numStr.append(firstChar);
			counter += 1;
			if (counter == _buffer.length()) {
				break;
			}
			firstChar = _buffer.charAt(counter);
		}

		if (!numStr.toString().isEmpty()) {
			System.out.println("INT!!! " + numStr);
			currentToken = new Token(numStr.toString(), Token.Type.INT);
		} else {
			System.out.println("not int...");
		}

		// ########## YOUR CODE ENDS HERE ##########

		// Remove the extracted token from buffer
		int tokenLen = this.currentToken.token().length();
		this._buffer = this._buffer.substring(tokenLen);
	}

	/**
	 * returned the current token extracted by {@code next()} **** please do not
	 * modify this part ****
	 * 
	 * @return type: Token
	 */
	public Token current() {
		return currentToken;
	}

	/**
	 * check whether there still exists another tokens in the buffer or not ****
	 * please do not modify this part ****
	 * 
	 * @return type: boolean
	 */
	public boolean hasNext() {
		return currentToken != null;
	}
}