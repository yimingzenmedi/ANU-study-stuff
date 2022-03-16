//package parser;
/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 * 
 * 
 */
public class Parser {
	private Tokenizer _tokenizer;
	private Screen _screen;

	public Parser(Tokenizer tokenizer, Screen screen) {
		_tokenizer = tokenizer;
		_screen = screen;
	}

    /**
     * This should parse all the tokens, turn the pointer and mark its visited positions for
     * every move.
     *
     * If there is no more token, this should return the current screen
     *
     * @return a screen object containing pointer's trace information.
     * @throws Exception
     */
    public Screen parse() throws OutOfScreenException {
		// TODO: Add your implementation here.
		// Hints: Check {@link Screen} and {@link Pointer} classes to see 
    	//        there is any methods/functions you can take advantage of.
    	//        Check the expected outcome in ParserTest.java
    	//        You can make additional methods if needed

		System.out.println("\nCurrent status:\n" + _screen.trace());

		if (_tokenizer.current().type == Token.Type.PENDOWN) {
			_screen.pointer.isPenDown = true;
			_screen.markVisistedPos(_screen.pointer.position);
		}

		if (_tokenizer.current().type == Token.Type.PENUP) {
			_screen.pointer.isPenDown = false;
		}

		if (_tokenizer.current().type == Token.Type.LEFT) {
			_screen.pointer.turnLeft();
		}

		if (_tokenizer.current().type == Token.Type.RIGHT) {
			_screen.pointer.turnRight();
		}

		if (_tokenizer.current().type == Token.Type.FORWARD) {

		}

		if (_tokenizer.current().type == Token.Type.BACK) {

		}

		if (_tokenizer.current().type == Token.Type.FORWARD_TO_END) {
			Direction direction = _screen.pointer.direction;
			int x = _screen.pointer.position.x;
			int y = _screen.pointer.position.y;
			int screenI = _screen.noOfRows;
			int screenJ = _screen.noOfColumns;

		}

		if (_tokenizer.current().type == Token.Type.BACK_TO_END) {
			Direction direction = _screen.pointer.direction;
			int x = _screen.pointer.position.x;
			int y = _screen.pointer.position.y;
			int screenI = _screen.noOfRows;
			int screenJ = _screen.noOfColumns;


		}

		System.out.println("New Screen: \n" + _screen.trace());

		_tokenizer.next();
		while (_tokenizer.hasNext()) {
			parse();
		}
		return _screen;
	}
}
