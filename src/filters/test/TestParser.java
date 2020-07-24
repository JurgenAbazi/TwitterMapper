package filters.test;

import filters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the parser.
 */
public class TestParser {
    @Test
    public void testBasic() throws StringParseException {
        Filter f = new Parser("trump").parse();
        assertTrue(f instanceof BasicFilter);
        assertEquals("trump", ((BasicFilter) f).getWord());
    }

    @Test
    public void testHairy() throws StringParseException {
        Filter x = new Parser("trump and (evil or blue) and red or green and not not purple").parse();
        assertEquals("(((trump and (evil or blue)) and red) or (green and not not purple))", x.toString());
    }
}
