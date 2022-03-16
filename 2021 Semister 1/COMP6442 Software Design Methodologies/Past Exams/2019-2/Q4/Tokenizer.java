//package parser;
/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 * 
 * 
 */
public class Tokenizer {
    private String _buffer;
    private final char COMMAND_SEPARATOR = ';'; 
    private Token current;
    
    public Tokenizer(String buffer) {
        this._buffer = buffer;
        this.next();
    }
    
    /**
     *  Extract the next token from {@code _buffer}
     */
    public void next() {
        _buffer = _buffer.trim();

        System.out.println("_buffer: " + _buffer);

        if (_buffer.isEmpty()) {
            current = null;
        	return;
        }

        
        if (_buffer.startsWith(Token.Type.LEFT.toString())) {
            current = new Token(Token.Type.LEFT);
            int flag = _buffer.indexOf(";");
            _buffer = _buffer.substring(flag + 1);
        }

        if (_buffer.startsWith(Token.Type.RIGHT.toString())) {
            current = new Token(Token.Type.RIGHT);
            int flag = _buffer.indexOf(";");
            _buffer = _buffer.substring(flag + 1);
        }
        
        // TODO: Implement "FORWARD_TO_END" and "BACK_TO_END" tokenization.

        if (_buffer.startsWith("FORWARD_TO_END")) {
            current = new Token(Token.Type.FORWARD_TO_END);
            int flag = _buffer.indexOf(";");
            _buffer = _buffer.substring(flag + 1);
            return;
        }

        if (_buffer.startsWith("BACK_TO_END")) {
            current = new Token(Token.Type.BACK_TO_END);
            int flag = _buffer.indexOf(";");
            _buffer = _buffer.substring(flag + 1);
            return;
        }


        // TODO: Implement "FORWARD" and "BACK" tokenization.
        if (_buffer.startsWith("FORWARD")) {
            char[] chars = _buffer.toCharArray();
            int value = 0;
            for (char c : chars) {
                if (c == ')') {
                    break;
                }
                if (Character.isDigit(c)) {
                    value = value * 10 + Integer.parseInt(String.valueOf(c));
                }
            }

            int flag = _buffer.indexOf(";");

            String originalTokenStr = _buffer.substring(0, flag).trim().toUpperCase();
            current = new Token(Token.Type.FORWARD, originalTokenStr, value);

            _buffer = _buffer.substring(flag + 1);
        }


        if (_buffer.startsWith("BACK")) {
            char[] chars = _buffer.toCharArray();
            int value = 0;
            for (char c : chars) {
                if (c == ')') {
                    break;
                }
                if (Character.isDigit(c)) {
                    value = value * 10 + Integer.parseInt(String.valueOf(c));
                }
            }

            int flag = _buffer.indexOf(";");

            String originalTokenStr = _buffer.substring(0, flag).trim().toUpperCase();
            current = new Token(Token.Type.BACK, originalTokenStr, value);

            _buffer = _buffer.substring(flag + 1);
        }

        // TODO: Implement "PENUP" and "PENDOWN" tokenization.

        if (_buffer.startsWith("PENUP")) {
            current = new Token(Token.Type.PENUP);
            int flag = _buffer.indexOf(";");
            _buffer = _buffer.substring(flag + 1);
        }

        if (_buffer.startsWith("PENDOWN")) {
            current = new Token(Token.Type.PENDOWN);
            int flag = _buffer.indexOf(";");
            _buffer = _buffer.substring(flag + 1);
        }
        // hints:
        // - Do not consider the case where text is syntactically incorrect.        
        // - Careful if you use String.startsWith. FORWARD_TO_END and FORWARD starts with
        //   the same string "FORWARD". Same for the BACK cases.
        // - Character.isDigit() may be useful in extracting the forward or back value from the buffer.
        // - Use new Token(<type>, <original token str>, <value>) to return these tokens
        // - Check the expected outcome in TokenizerTest.java

 
    }
    

    /**
     * Return the token in {@code _current}
     * @return the next token
     */
    public Token current() {
        return current;
    }

    /**
     * @return whether there is another token in {@code _current}
     */
    public boolean hasNext() {
        if(current == null){
            return false;
        }
        return true;
    }

}