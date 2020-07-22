package util;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

/**
 * Spherical Geometry Utilities
 */
public class SphericalGeometryUtils {
    /**
     * The radius of planet earth in meters.
     */
    private static final int EARTH_RADIUS_IN_METRES;

    static {
        EARTH_RADIUS_IN_METRES = 6371000;
    }

    /**
     * Find distance in metres between two lat/lon points.
     *
     * @param firstPoint  first point
     * @param secondPoint second point
     * @return distance between firstPoint and secondPoint in metres
     */
    public static double distanceBetween(ICoordinate firstPoint, ICoordinate secondPoint) {
        double lat1 = firstPoint.getLat() / 180.0 * Math.PI;
        double lat2 = secondPoint.getLat() / 180.0 * Math.PI;
        double deltaLon = (secondPoint.getLon() - firstPoint.getLon()) / 180.0 * Math.PI;
        double deltaLat = (secondPoint.getLat() - firstPoint.getLat()) / 180.0 * Math.PI;

        double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2.0) * Math.sin(deltaLon / 2.0);

        return  2.0 * EARTH_RADIUS_IN_METRES * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
