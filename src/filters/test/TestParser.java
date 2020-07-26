package filters.test;

import filters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the parser.
 */
public class TestParser {
    @Test
    public void testParserOnBasicFilter() throws StringParseException {
        Filter f = new Parser("trump").parse();
        assertTrue(f instanceof BasicFilter);
        assertEquals("trump", ((BasicFilter) f).getWord());
    }

    @Test
    public void testParserOnNotFilter() throws StringParseException {
        Filter f = new Parser("not bad").parse();
        assertTrue(f instanceof NotFilter);
        assertEquals("not bad", f.toString());
    }

    @Test
    public void testParserOnAndFilter() throws StringParseException {
        Filter f = new Parser("war and peace").parse();
        assertTrue(f instanceof AndFilter);
        assertEquals("(war and peace)", f.toString());
    }

    @Test
    public void testParserOnOrFilter() throws StringParseException {
        Filter f = new Parser("war or peace").parse();
        assertTrue(f instanceof OrFilter);
        assertEquals("(war or peace)", f.toString());
    }

    @Test
    public void testParserOnCompositeFilter() throws StringParseException {
        Filter x = new Parser("trump and (evil or blue) and red or green and not not purple").parse();
        assertEquals("(((trump and (evil or blue)) and red) or (green and not not purple))", x.toString());
    }

    @Test
    public void testParserOnIncorrectInput() {
        try {
            new Parser("wrong input").parse();
            fail("The test expected an exception to be thrown");
        } catch (StringParseException ignored) {
        }
    }
}
