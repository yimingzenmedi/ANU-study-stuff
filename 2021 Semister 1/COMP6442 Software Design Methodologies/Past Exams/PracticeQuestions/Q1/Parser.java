/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 *
 * Question:
 * Write a parse to recognize the language specified by the following grammar:
 * S := () ; S := )S( ; S := SS ;
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
		System.out.println("parseExp");

		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

        // TODO: Complete this method
		// START YOUR CODE

        return parse();

//		if (parseS()) {
//			return parseExp();
//		}
//		else if (parseS2()) {
//			return parseExp();
//		}
////		else if (parseS3()) {
////			return parseExp();
////		}
//
//		// END YOUR CODE
//		return false;
    }

    private boolean parse() {
	    Token token = _tokeniser.next();
	    if (token.type == Token.Type.LEFT_BRACKET) {
	        Token nextNext = _tokeniser.nextNext();
	        if (nextNext == null || nextNext.type == Token.Type.LEFT_BRACKET) {
	            return false;
            } else if (nextNext.type == Token.Type.RIGHT_BRACKET) {
	            _tokeniser.takeNext();
	            _tokeniser.takeNext();
	            return parseExp();
            } else {
	            return false;
            }
        } else if (token.type == Token.Type.RIGHT_BRACKET) {
	        _tokeniser.takeNext();
	        if (parseExp()) {
	            return false;
            } else if (_tokeniser.next() == null) {
	            return false;
            } else if (_tokeniser.next().type == Token.Type.LEFT_BRACKET) {
	            _tokeniser.takeNext();
	            return parseExp();
            } else {
	            return false;
            }
        }
	    return false;
    }

    private boolean parseS3() {
		System.out.println("parseS3");
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

		if (parseS() || parseS2() || parseS3()) {
			return parseS() || parseS2() || parseS3();
		}
		return false;
	}

    private boolean parseS2() {
		System.out.println("parseS2");
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}
		if (_tokeniser.next().type == Token.Type.RIGHT_BRACKET) {
			_tokeniser.takeNext();
			if ((parseS() || parseS2() || parseS3() || parseExp()) && _tokeniser.hasNext()) {
				if (_tokeniser.next().type == Token.Type.LEFT_BRACKET) {
					_tokeniser.takeNext();
					System.out.println("parseS2: true");
					return true;
				} else {
					System.out.println("flag 2");
				}
			} else {
				System.out.println("flag 1");
			}
		}
		System.out.println("parseS2: false");
		return false;
	}

    private boolean parseS() {
		System.out.println("parseS");
		if (!_tokeniser.isNextValid()) return false;
		if (!_tokeniser.hasNext()) {
			return true;
		}

		if (
				_tokeniser.next().type == Token.Type.LEFT_BRACKET &&
				_tokeniser.nextNext() != null &&
				_tokeniser.nextNext().type == Token.Type.RIGHT_BRACKET
		) {
			_tokeniser.takeNext();
			_tokeniser.takeNext();
			System.out.println("parseS: true");

			return true;
		}
		System.out.println("parseS: false");
		return false;
	}

}
