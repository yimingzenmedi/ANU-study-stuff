/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 * 
 * Implement next() method in Tokeniser.java to extract the SQL commands as Tokens as follows:
 * 
 * Token 1:
 * originalTokenStr: INSERT INTO table_name (column1, column2, column3, ...)
 * type: INSERT_INTO
 * value: table_name (column1, column2, column3, ...)
 * 
 * Token 2:
 * originalTokenStr: VALUES (value1, value2, value3, ...)
 * type: VALUES
 * value: (value1, value2, value3, ...)
 * 
 * Some brackets in the SQL commands may be missing. Please return null if some brackets are missing
 *
 * Please see test cases in TokeniserTest.java.
 */
public class Tokeniser {
	private String _buffer;

	public Tokeniser(String buffer) {
		this._buffer = buffer;
		int idx = this._buffer.indexOf(";");
		if (idx > 0) {
			this._buffer = this._buffer.substring(0, idx);
		}
	}

	/**
	 * Return the next token without changing the buffer
	 * 
	 * @return the next token OR null when there is no more token or the next token
	 *         is invalid.
	 */
	public Token next() {
		if (_buffer.isEmpty())
			return null;

		// TODO: Complete this method
		// START YOUR CODE

//		System.out.println("\nBuffer: " + _buffer);

		int flag1 = _buffer.strip().indexOf("INSERT INTO ");
		if (flag1 == 0) {
			int flag2 = _buffer.indexOf("VALUES (");
			if (flag2 > -1) {
				String originalTokenStr = _buffer.substring(0, flag2).strip();
//				System.out.println("Str: " + originalTokenStr);

				int b1 = 0, b2 = 0;
				for (int i = 0; i < originalTokenStr.length(); i++) {
					char c = originalTokenStr.toCharArray()[i];
					if (c == '(') {
						b1 += 1;
					}
					if (c == ')') {
						b2 += 1;
					}
				}
				if (b1 == 0 || b2 == 0) {
					return null;
				}
				String value = originalTokenStr.substring("INSERT INTO".length()).strip();
				return new Token(Token.Type.INSERT_INTO, value, originalTokenStr);
			} else {
				return null;
			}
		} else {
			int flag2 = _buffer.indexOf("VALUES (");
			if (flag2 > -1) {
				String originalTokenStr = _buffer;
				int b1 = 0, b2 = 0;
				for (int i = 0; i < originalTokenStr.length(); i++) {
					char c = originalTokenStr.toCharArray()[i];
					if (c == '(') {
						b1 += 1;
					}
					if (c == ')') {
						b2 += 1;
					}
				}
				if (b1 == 0 || b2 == 0) {
					return null;
				}
				String value = originalTokenStr.substring("VALUES".length()).strip();
				return new Token(Token.Type.VALUES, value, originalTokenStr);
			} else {
				return null;
			}
		}


		// You are allowed to remove the following 'return null' if necessary

		// END YOUR CODE
	}

	/**
	 * Return the next token and remove it from the buffer
	 * 
	 * @return the next token OR null when there is no more token or the next token
	 *         is invalid.
	 */
	public Token takeNext() {
		Token nextToken = next();
		if (nextToken == null)
			return null;

		if (nextToken.originalTokenStr.length() < _buffer.length()) {
			_buffer = _buffer.substring(nextToken.originalTokenStr.length()).trim();
		} else {
			_buffer = "";
		}

		return nextToken;
	}

	/**
	 * @return whether there is another token to parse in the buffer
	 */
	public boolean hasNext() {
		return next() != null;
	}
}