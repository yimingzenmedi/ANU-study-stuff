/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 *
 * Question:
 * Write a parse to recognize the language specified by the following grammar:
 * S := () ; S := (S) ; S := SS ;
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

		if (parseSingleS()) {
			return parseExp();
		}
		if (parseBracketS()) {
			return parseExp();
		}

		// END YOUR CODE

		return false;
    }

    public boolean parseBracketS() {
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}
//		System.out.println("parseBracketS: " + _tokeniser.next().originalTokenStr);
		if (
				_tokeniser.next().type == Token.Type.LEFT_BRACKET &&
				_tokeniser.nextNext().type == Token.Type.LEFT_BRACKET
		) {
			_tokeniser.takeNext();
			if (!parseSingleS()) {
				parseBracketS();
			} else {
				if (_tokeniser.next().type == Token.Type.RIGHT_BRACKET) {
					_tokeniser.takeNext();
					return true;
				} else {
//					while () {}
					if (parseExp() && _tokeniser.next().type == Token.Type.RIGHT_BRACKET) {
						_tokeniser.takeNext();
						return true;
					}
				}
			}

		}
		return false;
	}


    public boolean parseSingleS() {
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}
		if (
				_tokeniser.next().type == Token.Type.LEFT_BRACKET
		) {
			if (_tokeniser.nextNext().type == Token.Type.RIGHT_BRACKET) {
				_tokeniser.takeNext();
				_tokeniser.takeNext();
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
