package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;

import java.awt.*;

/**
 * Simple red colored map marker.
 */
public class MapMarkerSimple extends MapMarkerCircle {
    /**
     * The default size of the marker.
     */
    public static final double DEFAULT_MARKER_SIZE = 5.0;

    /**
     * The default color of the marker.
     */
    public static final Color DEFAULT_COLOR = Color.RED;

    /**
     * Constructor.
     *
     * @param layer      The layer where the marker will be added.
     * @param coordinate The coordinates of the marker.
     */
    public MapMarkerSimple(Layer layer, Coordinate coordinate) {
        super(layer, null, coordinate, DEFAULT_MARKER_SIZE, STYLE.FIXED, getDefaultStyle());
        setColor(Color.BLACK);
        setBackColor(DEFAULT_COLOR);
    }
}
