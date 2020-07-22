package util;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import twitter4j.GeoLocation;
import twitter4j.Status;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * Helpful methods that don't clearly fit anywhere else.
 */
public class Util {
    public static BufferedImage defaultImage;

    static {
        defaultImage = imageFromURL("http://png-2.findicons.com/files/icons/1995/web_application/48/smiley.png");
    }

    public static Coordinate getCoordinateFromGeoLocation(GeoLocation geoLocation) {
        return new Coordinate(geoLocation.getLatitude(), geoLocation.getLongitude());
    }

    public static GeoLocation getStatusGeoLocation(Status status) {
        return new GeoLocation(getStatusLatitude(status), getStatusLongitude(status));
    }

    public static Coordinate getStatusCoordinates(Status status) {
        return new Coordinate(getStatusLatitude(status), getStatusLongitude(status));
    }

    private static double getStatusLatitude(Status status) {
        GeoLocation bottomRight = status.getPlace().getBoundingBoxCoordinates()[0][0];
        GeoLocation topLeft = status.getPlace().getBoundingBoxCoordinates()[0][2];
        return (bottomRight.getLatitude() + topLeft.getLatitude()) / 2;
    }

    private static double getStatusLongitude(Status status) {
        GeoLocation bottomRight = status.getPlace().getBoundingBoxCoordinates()[0][0];
        GeoLocation topLeft = status.getPlace().getBoundingBoxCoordinates()[0][2];
        return (bottomRight.getLongitude() + topLeft.getLongitude()) / 2;
    }

    public static BufferedImage imageFromURL(String url) {
        try {
            BufferedImage img = ImageIO.read(new URL(url));
            return img == null ? defaultImage : img;
        } catch (IOException e) {
            return defaultImage;
        }
    }
}
