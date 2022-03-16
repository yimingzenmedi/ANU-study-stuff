//package models.parser;
//
//import enums.TokenType;
//import models.parser.exps.Exp;
//import models.parser.exps.NameExp;
//
//public class Parser {
//    private Tokenizer tokenizer;
//
//    public Parser(Tokenizer tokenizer) {
//        this.tokenizer = tokenizer;
//    }
//
//    public Exp parseExp() {
//        System.out.println("\nparseExp");
//
//        Exp exp = null;
//
//        while (tokenizer.hasNext()) {
//            String token = tokenizer.current().getToken();
//            TokenType type = tokenizer.current().getType();
//
//            if (type == TokenType.GO) {
//                tokenizer.nextLine();
//
//            }
//        }
//    }
//
//    private Exp parseGoExp() {
//        Exp exp = null;
//
//        while (tokenizer.hasNext()) {
//            String token = tokenizer.current().getToken();
////            String
//        }
//    }
//
//    private Exp parseName() {
//        return new NameExp(tokenizer.current().getToken());
//    }
//}
