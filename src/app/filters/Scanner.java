package app.filters;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple scanner for a language consisting of words and parentheses.
 */
public class Scanner {
    /**
     * List of tokens.
     */
    private final List<String> tokens;

    /**
     * Constructor.
     *
     * @param input The String being scanned.
     */
    public Scanner(String input) {
        tokens = new LinkedList<>();
        removesWhiteSpacesFromTokens(input);
    }

    /**
     * Throws away any white space.
     * This simple scanner scans the entire input in its constructor, building a list of tokens
     * which it then returns as necessary in response to calls to its peek and advance methods.
     * The tokenPattern matches words ([a-zA-Z]+), left and right parenthesis, and whitespaces.
     *
     * @param input The input string.
     */
    private void removesWhiteSpacesFromTokens(String input) {
        Pattern tokenPattern = Pattern.compile("\\(|\\)|[a-zA-Z]+|\\s+");
        Matcher matcher = tokenPattern.matcher(input);
        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches("\\s+")) {
                continue;
            }
            tokens.add(token);
        }
    }

    /**
     * Return the first token remaining, without changing anything.
     * A second call to peek without an intervening call to advance, will return this same token again.
     *
     * @return The first remaining token in the input, or null if no tokens remain
     */
    public String peek() {
        return tokens.size() > 0 ? tokens.get(0) : null;
    }

    /**
     * Advance the input, consuming the current token, and return the first remaining token in the input.
     *
     * @return The first remaining token in the input after advancing, or null if no tokens remain
     */
    public String advance() {
        tokens.remove(0);
        return peek();
    }
}
