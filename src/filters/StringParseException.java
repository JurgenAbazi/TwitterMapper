package filters;

/**
 * The exception thrown when parsing a string fails.
 */
public class StringParseException extends Exception {
    /**
     * Default Constructor.
     */
    public StringParseException() {
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public StringParseException(String message) {
        super(message);
    }
}
