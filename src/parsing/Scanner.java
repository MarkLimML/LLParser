package parsing;

import java.util.ArrayList;
import java.util.List;

/*
 * Lexical analyzer for Scheme-like minilanguage:
 * (define (foo x) (bar (baz x)))
 */
public class Scanner {

    public static enum Type {
        TERMINAL, SYMBOL, UNION, LPAREN, RPAREN, EOF, UNK
    }

    public static class Token {
        public final Type t;
        public final String s;

        public Token(String s) {
            if(s.equals("$"))
                this.t = Type.EOF;
            else
                this.t = identifyType(s);
            this.s = s;
        }

        public static Type identifyType(String s) {

            String state = "S1";
            if(s.length()==0)
                return null;

            for(int i=0; i<s.length(); i++) {
                switch(state) {
                    case "S1":
                        if(isLetter(s.charAt(i)) || isNumeric(s.charAt(i))) {
                            state = "S4";
                        }
                        else {
                            switch(s.charAt(i)) {
                                case '(':
                                    state = "S2"; break;
                                case ')':
                                    state = "S3"; break;
                                case '*': case '+': case '?':
                                    state = "S5"; break;
                                case 'U':
                                    state = "S6"; break;
                                default:
                                    state = "UNK"; break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            switch(state) {
                case "S2":
                    return Type.LPAREN;
                case "S3":
                    return Type.RPAREN;
                case "S4":
                    return Type.TERMINAL;
                case "S5":
                    return Type.SYMBOL;
                case "S6":
                    return Type.UNION;
                case "UNK":
                    return Type.UNK;
                default:
                    return null;
            }
        }

        public static boolean isLetter(char c) { return Character.isLetter(c) && !Character.isUpperCase(c); }
        public static boolean isNumeric(char c) { return Character.isDigit(c); }
    }

    public List<Token> getTokens(String s) {
        List<Token> tokens = new ArrayList<>();
        s = s.replaceAll(" ", "");
        for (int i = 0; i < s.length(); i++) {
            Token t = new Token("");
            //System.out.println(i+" "+s.charAt(i));
            while(t.t == null)
                t = new Token(String.valueOf(s.charAt(i)));
            //System.out.println(t.t+" "+t.s+" "+i);
            tokens.add(t);
        }
        return tokens;
    }

    public Boolean isToken(Token t) {
        return t.t != Type.UNK;
    }
}