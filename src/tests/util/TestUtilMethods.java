package util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import twitter4j.GeoLocation;
import app.util.Util;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtilMethods {

    @Test
    public void testGetCoordinateFromGeoLocation() {
        GeoLocation geoLocation = Mockito.mock(GeoLocation.class);
        Mockito.when(geoLocation.getLatitude()).thenReturn(0d);
        Mockito.when(geoLocation.getLongitude()).thenReturn(0d);

        Coordinate coordinate = new Coordinate(0d, 0d);
        assertEquals(coordinate, Util.getCoordinateFromGeoLocation(geoLocation));
    }

    @Test
    public void testGetDistanceBetweenSamePoint() {
        ICoordinate c1 = Mockito.mock(ICoordinate.class);
        Mockito.when(c1.getLat()).thenReturn(0d);
        Mockito.when(c1.getLon()).thenReturn(0d);

        assertEquals(0d, Util.getDistanceBetweenPoints(c1, c1));
    }

    @Test
    public void testGetDistanceBetweenDifferentPoints() {
        ICoordinate c1 = Mockito.mock(ICoordinate.class);
        Mockito.when(c1.getLat()).thenReturn(0d);
        Mockito.when(c1.getLon()).thenReturn(0d);

        ICoordinate c2 = Mockito.mock(ICoordinate.class);
        Mockito.when(c2.getLat()).thenReturn(10d);
        Mockito.when(c2.getLon()).thenReturn(10d);

        assertTrue(Util.getDistanceBetweenPoints(c1, c2) != 0d);
    }

    @Test
    public void testGetImageFromURLWithCorrectImageLink() {
        BufferedImage norm = Util.getImageFromURL("https://upload.wikimedia.org/wikipedia/en/2/26/Led_Zeppelin_-_Led_Zeppelin_IV.jpg");
        assertNotNull(norm, "The test should have returned the image!");
    }

    @Test
    public void testGetImageFromURLWithIncorrectImageLink() {
        BufferedImage norm = Util.getImageFromURL("https://www.google.com");
        assertNotNull(norm, "The test should have returned the default image!");
    }
}
