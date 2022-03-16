import java.util.Locale;

public class Tokeniser {

	private String buffer; // save text
	private Token currentToken; // save token extracted from next()

	public Tokeniser(String text) {
		buffer = text; // save input text (string)
		next(); // extracts the first token.
	}

	/**
	 * This function will find and extract a next token from {@code buffer} and save
	 * the token to {@code currentToken}.
	 */
	public void next() {

		buffer = buffer.trim(); // remove whitespace
		if (buffer.isEmpty()) {
			currentToken = null; // if there's no string left, set currentToken null and return
			return;
		}

		// TODO
		// ########## YOUR CODE STARTS HERE ##########

		System.out.println("\n>> buffer: " + buffer);

		String [] arr = buffer.split("\\s+");
//		for(String ss : arr){
//			System.out.println(ss);
//		}

//		String cmd = arr[3];
		String cmd = arr[0];

		int termIndex = cmd.indexOf(";");
		if (termIndex == 0) {
			currentToken = new Token(Token.Type.TERMINATOR, ";");
			buffer = buffer.substring(1);
			return;
		} else if (termIndex > 0) {
			cmd = buffer.substring(0, termIndex);
//			buffer = buffer.substring(termIndex);
		}

		String txt = cmd;
		cmd = cmd.toUpperCase();

		if (cmd.equals("RETRIEVE")) {
			currentToken = new Token(Token.Type.RETRIEVE, txt);
		} else if (cmd.equals("FROM")) {
			currentToken = new Token(Token.Type.FROM, txt);
		} else if (cmd.equals("STORE")) {
			currentToken = new Token(Token.Type.STORE, txt);
		} else if (cmd.equals("TO")) {
			currentToken = new Token(Token.Type.TO, txt);
		} else {
			currentToken = new Token(Token.Type.PARAMETER, txt);
		}

		// ########## YOUR CODE ENDS HERE ##########

		// Remove the extracted token from buffer
		int tokenLen = currentToken.getValue().length();
		buffer = buffer.substring(tokenLen);
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