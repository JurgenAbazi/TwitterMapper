package filters.test;

import filters.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import twitter4j.*;

import java.util.List;

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
    public void testBasicFilter() {
        Filter f = new BasicFilter("fred");
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the BasicFilter list of terms.
     */
    @Test
    public void testBasicFilterTerms() {
        Filter filter = new BasicFilter("fred");
        List<String> listOfTerms = filter.getTerms();

        assertTrue(listOfTerms.contains("fred"));
        assertFalse(listOfTerms.contains("ed"));
    }

    /**
     * Unit Test for the NotFilter.
     */
    @Test
    public void testNotFilter() {
        Filter f = new NotFilter(new BasicFilter("fred"));
        assertFalse(f.matches(makeStatus("Fred Flintstone")));
        assertFalse(f.matches(makeStatus("fred Flintstone")));
        assertTrue(f.matches(makeStatus("Red Skelton")));
        assertTrue(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the NotFilter list of terms.
     */
    @Test
    public void testNotFilterTerms() {
        Filter f = new NotFilter(new BasicFilter("fred"));
        List<String> listOfTerms = f.getTerms();

        assertTrue(listOfTerms.contains("fred"));
        assertFalse(listOfTerms.contains("ed"));
    }

    /**
     * Unit Test for the AndFilter.
     */
    @Test
    public void testAndFilter() {
        Filter f = new AndFilter(new BasicFilter("fred"), new BasicFilter("Flintstone"));
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertFalse(f.matches(makeStatus("Fred Skelton")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the AndFilter list of terms.
     */
    @Test
    public void testAndFilterTerms() {
        Filter f = new AndFilter(new BasicFilter("fred"), new BasicFilter("flintstone"));
        List<String> terms = f.getTerms();

        assertTrue(terms.contains("fred"));
        assertTrue(terms.contains("flintstone"));
        assertFalse(terms.contains("Fred"));
        assertFalse(terms.contains("stone"));
    }

    /**
     * Unit Test for the OrFilter list of terms.
     */
    @Test
    public void testOrFilter() {
        Filter f = new OrFilter(new BasicFilter("fred"), new BasicFilter("Flintstone"));
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertTrue(f.matches(makeStatus("Fred Skelton")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    /**
     * Unit Test for the BasicFilter.
     */
    @Test
    public void testOrFilterTerms() {
        Filter f = new OrFilter(new BasicFilter("fred"), new BasicFilter("flintstone"));
        List<String> listOfTerms = f.getTerms();

        assertTrue(listOfTerms.contains("fred"));
        assertTrue(listOfTerms.contains("flintstone"));
        assertFalse(listOfTerms.contains("Fred"));
        assertFalse(listOfTerms.contains("stone"));
    }

    /**
     * Creates a dummy status object.
     *
     * @param text The text message of the status.
     * @return The status.
     */
    private Status makeStatus(String text) {
        Status mockStatus = Mockito.mock(Status.class);
        Mockito.when(mockStatus.getText()).thenReturn(text);
        return mockStatus;
    }
}
