package models.parser;

import enums.TokenType;

public class Tokenizer {
    private String buffer;
    private Token currentToken;

    public Tokenizer(String txt) {
        buffer = txt;
        next();
    }


    public boolean hasNext() {
        return currentToken != null;
    }

    public Token current() {
        return currentToken;
    }

    public void next() {
//        pre process:
        buffer = buffer.trim().toLowerCase();
//        System.out.println("buffer to process: " + buffer);

        if (buffer.isEmpty()) {
            currentToken = null;
            return;
        }

//        for (int i = buffer.length() - 1; i >= 0; i--) {
//            if (Character.isLetterOrDigit(buffer.charAt(i))) {
//                buffer = buffer.substring(0, i);
//            } else {
//                break;
//            }
//        }

//        process:

        String GO = "go to ";
        String TALK = "talk to ";
        String INTERACT = "interact with ";
        String FIGHT = "fight with ";
        String USE = "use ";
        String SHOW_TASK = "show tasks";
        String SHOW_CURRENT_TASK = "show current tasks";
        String SHOW_BACKPACK = "show backpack";
        String SHOW_STATUS = "show status";
        String SHOW_MY_STATUS = "show my status";
        String CHECK = "check around";
        String EXIT_GAME = "exit game";
        String EXIT = "exit";
//        String CANCEL = "cancel";

        String marker = ",";

        int markerPosition = buffer.indexOf(marker);
//        int bufferLength = buffer.length();
        String value;
        if (buffer.startsWith(marker)) {
            buffer = buffer.substring(1);
            next();
        } else {
            if (buffer.startsWith(GO)) {
                if (markerPosition > -1) {
                    value = buffer.substring(GO.length(), markerPosition);
                } else {
                    value = buffer.substring(GO.length());
                }
                currentToken = new Token(GO + value, TokenType.GO, value);
            } else if (buffer.startsWith(TALK)) {
                if (markerPosition > -1) {
                    value = buffer.substring(TALK.length(), markerPosition);
                } else {
                    value = buffer.substring(TALK.length());
                }
                currentToken = new Token(TALK + value, TokenType.TALK, value);
            } else if (buffer.startsWith(INTERACT)) {
                if (markerPosition > -1) {
                    value = buffer.substring(INTERACT.length(), markerPosition);
                } else {
                    value = buffer.substring(INTERACT.length());
                }
                currentToken = new Token(INTERACT + value, TokenType.INTERACT, value);
            } else if (buffer.startsWith(FIGHT)) {
                if (markerPosition > -1) {
                    value = buffer.substring(FIGHT.length(), markerPosition);
                } else {
                    value = buffer.substring(FIGHT.length());
                }
                currentToken = new Token(FIGHT + value, TokenType.FIGHT, value);
            } else if (buffer.startsWith(USE)) {
                if (markerPosition > -1) {
                    value = buffer.substring(USE.length(), markerPosition);
                } else {
                    value = buffer.substring(USE.length());
                }
                currentToken = new Token(USE + value, TokenType.USE, value);
            } else if (buffer.startsWith(SHOW_TASK)) {
                currentToken = new Token(SHOW_TASK, TokenType.SHOW_TASK);
            } else if (buffer.startsWith(SHOW_CURRENT_TASK)) {
                currentToken = new Token(SHOW_CURRENT_TASK, TokenType.SHOW_TASK);
            } else if (buffer.startsWith(SHOW_BACKPACK)) {
                currentToken = new Token(SHOW_BACKPACK, TokenType.SHOW_BACKPACK);
            } else if (buffer.startsWith(SHOW_STATUS)) {
                currentToken = new Token(SHOW_STATUS, TokenType.SHOW_STATUS);
            } else if (buffer.startsWith(SHOW_MY_STATUS)) {
                currentToken = new Token(SHOW_MY_STATUS, TokenType.SHOW_STATUS);
            } else if (buffer.startsWith(CHECK)) {
                currentToken = new Token(CHECK, TokenType.CHECK);
            } else if (buffer.startsWith(EXIT_GAME)) {
                currentToken = new Token(EXIT_GAME, TokenType.EXIT);
            } else if (buffer.startsWith(EXIT)) {
                currentToken = new Token(EXIT, TokenType.EXIT);
            }
//        else if (buffer.startsWith(CANCEL)) {
//            currentToken = new Token(CANCEL, TokenType.CANCEL);
////            buffer = buffer.substring(currentToken.getToken().length());
//            buffer = buffer.substring(buffer.indexOf(MARK));
//        }
            else {
                currentToken = new Token(buffer, TokenType.UNKNOWN);
            }

//            if (markerPosition > -1 && markerPosition < buffer.length()) {
//                buffer = buffer.substring(buffer.indexOf(marker));
//            } else
            if (markerPosition == -1) {
//                System.out.println("No marker, token: " + currentToken.getToken() + ", Type: " + currentToken.getType() + ", Value: " + currentToken.getValue());
                buffer = buffer.substring(currentToken.getToken().length());
            } else {
//                System.out.println("With marker, token: " + currentToken.getToken() + ", Type: " + currentToken.getType() + ", Value: " + currentToken.getValue());

                buffer = buffer.substring(markerPosition);
            }
        }
    }
}
