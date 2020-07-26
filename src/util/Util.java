package util;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import twitter4j.GeoLocation;
import twitter4j.Status;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * Helpful methods that don't clearly fit anywhere else.
 */
public class Util {
    /**
     * The default image.
     */
    public static final BufferedImage DEFAULT_IMAGE;

    /**
     * The radius of planet earth in meters.
     */
    private static final int EARTH_RADIUS_IN_METRES;


    static {
        DEFAULT_IMAGE = getImageFromURL("https://upload.wikimedia.org/wikipedia/en/2/26/Led_Zeppelin_-_Led_Zeppelin_IV.jpg");
        EARTH_RADIUS_IN_METRES = 6371000;
    }

    /**
     * Calculate the coordinates of the given geo-location and returns them as a Coordinate object.
     *
     * @param geoLocation The GeoLocation object being converted..
     * @return The Coordinate object of the GeoLocation.
     */
    public static Coordinate getCoordinateFromGeoLocation(GeoLocation geoLocation) {
        return new Coordinate(geoLocation.getLatitude(), geoLocation.getLongitude());
    }

    /**
     * Calculate the geo-location of the given status and returns it as a GeoLocation object.
     *
     * @param status The twitter status.
     * @return The GeoLocation object of the status.
     */
    public static GeoLocation getStatusGeoLocation(Status status) {
        return new GeoLocation(getStatusLatitude(status), getStatusLongitude(status));
    }

    /**
     * Calculate the coordinates of the given status and returns them as a Coordinate object.
     *
     * @param status The twitter status.
     * @return The Coordinate object of the status.
     */
    public static Coordinate getStatusCoordinates(Status status) {
        return new Coordinate(getStatusLatitude(status), getStatusLongitude(status));
    }

    /**
     * Inner method that calculates the latitude of the given twitter status.
     *
     * @param status The twitter status.
     * @return The latitude.
     */
    private static double getStatusLatitude(Status status) {
        GeoLocation bottomRight = status.getPlace().getBoundingBoxCoordinates()[0][0];
        GeoLocation topLeft = status.getPlace().getBoundingBoxCoordinates()[0][2];
        return (bottomRight.getLatitude() + topLeft.getLatitude()) / 2;
    }

    /**
     * Inner method that calculates the longitude of the given twitter status.
     *
     * @param status The twitter status.
     * @return The longitude.
     */
    private static double getStatusLongitude(Status status) {
        GeoLocation bottomRight = status.getPlace().getBoundingBoxCoordinates()[0][0];
        GeoLocation topLeft = status.getPlace().getBoundingBoxCoordinates()[0][2];
        return (bottomRight.getLongitude() + topLeft.getLongitude()) / 2;
    }

    /**
     * Creates a BufferedImage from a given URL.
     *
     * @param url The url of the image.
     * @return The BufferedImage object.
     */
    public static BufferedImage getImageFromURL(String url) {
        try {
            BufferedImage img = ImageIO.read(new URL(url));
            return img == null ? DEFAULT_IMAGE : img;
        } catch (IOException e) {
            return DEFAULT_IMAGE;
        }
    }

    /**
     * Finds the distance in metres between two coordinate points.
     *
     * @param firstPoint  first point
     * @param secondPoint second point
     * @return distance between the points in metres
     */
    public static double getDistanceBetweenPoints(ICoordinate firstPoint, ICoordinate secondPoint) {
        double lat1 = firstPoint.getLat() / 180.0 * Math.PI;
        double lat2 = secondPoint.getLat() / 180.0 * Math.PI;
        double deltaLon = (secondPoint.getLon() - firstPoint.getLon()) / 180.0 * Math.PI;
        double deltaLat = (secondPoint.getLat() - firstPoint.getLat()) / 180.0 * Math.PI;

        double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2.0) * Math.sin(deltaLon / 2.0);

        return  2.0 * EARTH_RADIUS_IN_METRES * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public static void addTittledBorderToPanel(JPanel panel, String title) {
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(title),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
}
