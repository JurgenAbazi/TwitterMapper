package query.test;

import org.junit.jupiter.api.Test;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import query.Query;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Different tests for the Query class.
 */
public class TestQuery {
    @Test
    public void testQueryTermination() {
        Query query = new Query("Twitter Mapper", Color.GREEN, new JMapViewer());
        query.terminate();
        assertFalse(query.getVisible());
        assertEquals(0, query.getMap().getMapMarkerList().size());
    }

    @Test
    public void testGetQueryString() {
        Query query = new Query("Twitter Mapper", Color.GREEN, new JMapViewer());
        assertEquals("Twitter Mapper", query.getQueryString());
        assertNotNull(query.getQueryString());
    }

    @Test
    public void testQueryFilterIsCreatedOnConstruction() {
        Query query = new Query("Twitter Mapper", Color.GREEN, new JMapViewer());
        assertNotNull(query.getFilter());
    }

    @Test
    public void testQueryConstructionWithIncorrectParameters() {
        try {
            new Query(null, Color.RED, new JMapViewer());
            fail("Query should have not been correctly constructed.");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testQueryConstructionWithEmptyText() {
        try {
            new Query("", Color.RED, new JMapViewer());
            fail("Query is not correctly constructed. Parser should have thrown Exception!");
        } catch (Exception ignored) {
        }
    }

}