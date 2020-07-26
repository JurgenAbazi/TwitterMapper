package app.filters;

/**
 * Parse a string in the filter language and return the filter.
 * Throws a SyntaxError exception on failure.
 * <p>
 * This is a top-down recursive descent parser (a.k.a., LL(1))
 * <p>
 * The really short explanation is that an LL(1) grammar can be parsed by a collection
 * of mutually recursive methods, each of which is able to recognize a single grammar symbol.
 * <p>
 * The grammar (EBNF) for our filter language is:
 * <p>
 * goal    ::= expr
 * expr    ::= orexpr
 * orexpr  ::= andexpr ( "or" andexpr )*
 * andexpr ::= notexpr ( "and" notexpr )*
 * notexpr ::= prim | "not" notexpr
 * prim    ::= word | "(" expr ")"
 * <p>
 * The reason for writing it this way is that it respects the "natural" precedence of boolean
 * expressions, where the precedence order (decreasing) is:
 * parens
 * not
 * and
 * or
 * This allows an expression like:
 * blue or green and not red or yellow and purple
 * To be parsed like:
 * blue or (green and (not red)) or (yellow and purple)
 */
public class Parser {
    private final Scanner scanner;
    private static final String LPAREN;
    private static final String RPAREN;
    private static final String OR;
    private static final String AND;
    private static final String NOT;

    static {
        LPAREN = "(";
        RPAREN = ")";
        OR = "or";
        AND = "and";
        NOT = "not";
    }

    public Parser(String input) {
        scanner = new Scanner(input);
    }

    public Filter parse() throws StringParseException {
        Filter ans = expr();
        if (scanner.peek() != null) {
            throw new StringParseException("Extra stuff at end of input");
        }
        return ans;
    }

    private Filter expr() throws StringParseException {
        return orexpr();
    }

    private Filter orexpr() throws StringParseException {
        Filter sub = andexpr();
        String token = scanner.peek();
        while (token != null && token.equals(OR)) {
            scanner.advance();
            Filter right = andexpr();
            sub = new OrFilter(sub, right);
            token = scanner.peek();
        }
        return sub;
    }

    private Filter andexpr() throws StringParseException {
        Filter sub = notexpr();
        String token = scanner.peek();
        while (token != null && token.equals(AND)) {
            scanner.advance();
            Filter right = notexpr();
            sub = new AndFilter(sub, right);
            token = scanner.peek();
        }
        return sub;
    }

    private Filter notexpr() throws StringParseException {
        String token = scanner.peek();
        if (token.equals(NOT)) {
            scanner.advance();
            Filter sub = notexpr();
            return new NotFilter(sub);
        } else {
            return prim();
        }
    }

    private Filter prim() throws StringParseException {
        String token = scanner.peek();
        if (token.equals(LPAREN)) {
            scanner.advance();
            Filter sub = orexpr();
            if (!scanner.peek().equals(RPAREN)) {
                throw new StringParseException("Expected ')'");
            }
            scanner.advance();
            return sub;
        } else {
            Filter sub = new BasicFilter(token);
            scanner.advance();
            return sub;
        }
    }
}
