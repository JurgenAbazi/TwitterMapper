package filters;

/**
 * The exception thrown when parsing a string fails.
 */
public class StringParseException extends Exception {
    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public StringParseException(String message) {
        super(message);
    }
}
