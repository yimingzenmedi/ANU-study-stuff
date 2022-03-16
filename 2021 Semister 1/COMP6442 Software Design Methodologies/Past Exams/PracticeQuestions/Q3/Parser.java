/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 *
 * Question:
 * Write a parse to recognize the language specified by the following grammar:
 * X := 1 | 3 | 5 | 7 | 9 ; Y := 0 | 2 | 4 | 6 | 8 ; X := YX ; Y := XY ; S := X | Y ;
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
		if (!_tokeniser.isNextOrLastValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

        // TODO: Complete this method
		// START YOUR CODE

		if (_tokeniser.next().type == Token.Type.ODD_NUMBER) {
			return parseX();
		}

		if (_tokeniser.next().type == Token.Type.EVEN_NUMBER) {
			return parseY();
		}


		// END YOUR CODE
		return false;
	}

	public boolean parseX() {
		if (!_tokeniser.isNextOrLastValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

		if (_tokeniser.next().type == Token.Type.ODD_NUMBER) {
			_tokeniser.takeNext();
			return parseY();
		}

		else {
			_tokeniser.takeNext();
			if (!_tokeniser.hasNext()) {
				return false;
			}
			return parseX();
		}
//		return false;
	}

	public boolean parseY() {
		if (!_tokeniser.isNextOrLastValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

		if (_tokeniser.next().type == Token.Type.EVEN_NUMBER) {
			_tokeniser.takeNext();
			return parseX();
		}

		else {
			_tokeniser.takeNext();
			if (!_tokeniser.hasNext()) {
				return false;
			}
			return parseY();
		}
//		return false;
	}
}
