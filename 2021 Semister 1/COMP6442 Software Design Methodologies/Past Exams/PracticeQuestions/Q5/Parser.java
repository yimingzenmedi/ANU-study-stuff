/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 *
 * Question:
 * Write a parse to recognize the language specified by the following grammar:
 * X := + | - | * | / ; Y := 0 | 2 | 4 | 6 | 8 ; S := SXY | Y ;
 *
 * 
 */
public class Parser {
	Tokeniser _tokeniser;

	public Parser(Tokeniser tokeniser) {
		_tokeniser = tokeniser;
	}

	/**
	 * @return True if the given tokens conform with the grammar. False, otherwise.
	 */
	public boolean parseExp() {
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

		// TODO: Complete this method
		// START YOUR CODE
		System.out.println("token 1: " + _tokeniser.next().originalTokenStr);

		if (parseY()) {
			if (parseX()) {
//				if (parseY()) {		//	S
					return parseExp();
//				} else {
//					return false;		//	XX
//				}
			} else {
				return false; 	// YY
			}
		}
//		else {
//			return false;		// X
//		}

		// END YOUR CODE
		return false;
	}

//	public boolean parseS() {
//		if (!_tokeniser.isNextValid()) return false;
//		if (!_tokeniser.hasNext()) {
//			return true;
//		}
//		if (_tokeniser.next().type == Token.Type.EVEN_NUMBER) {
//			_tokeniser.takeNext();
//			return true;
//		}
//		return parseY();
//	}

	public boolean parseY() {

		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			_tokeniser.takeNext();
			return true;
		}
		System.out.println("token y: " + _tokeniser.next().originalTokenStr);

		if (_tokeniser.next().type == Token.Type.EVEN_NUMBER) {
			_tokeniser.takeNext();
			return true;
		}
		return false;
	}

	public boolean parseX() {
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			_tokeniser.takeNext();
			return true;
		}
		System.out.println("token x: " + _tokeniser.next().originalTokenStr);

		if (_tokeniser.next().type == Token.Type.MATH_OPERATION) {
			_tokeniser.takeNext();
			return true;
		}
		return false;
	}

}
