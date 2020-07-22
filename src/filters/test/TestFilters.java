package filters.test;

import filters.*;
import org.junit.jupiter.api.Test;
import twitter4j.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class used for testing the different type of Filters.
 */
public class TestFilters {
    /**
     * Unit Test for the BasicFilter.
     */
    @Test
    public void testBasic() {
        Filter f = new BasicFilter("fred");
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the NotFilter.
     */
    @Test
    public void testNot() {
        Filter f = new NotFilter(new BasicFilter("fred"));
        assertFalse(f.matches(makeStatus("Fred Flintstone")));
        assertFalse(f.matches(makeStatus("fred Flintstone")));
        assertTrue(f.matches(makeStatus("Red Skelton")));
        assertTrue(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the AndFilter.
     */
    @Test
    public void testAnd() {
        Filter f = new AndFilter(new BasicFilter("fred"), new BasicFilter("Flintstone"));
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertFalse(f.matches(makeStatus("Fred Skelton")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the OrFilter.
     */
    @Test
    public void testOr() {
        Filter f = new OrFilter(new BasicFilter("fred"), new BasicFilter("Flintstone"));
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertTrue(f.matches(makeStatus("Fred Skelton")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Creates a dummy status object.
     *
     * @param text The text message of the status.
     * @return The status.
     */
    private Status makeStatus(String text) {
        return new DummyStatus(text);
    }
}
