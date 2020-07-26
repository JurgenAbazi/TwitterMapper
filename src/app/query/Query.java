package app.query;

import app.filters.Filter;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import twitter4j.Status;
import twitter4j.User;
import app.ui.MapMarkerWithImage;
import app.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A query over the twitter stream.
 */
@SuppressWarnings("deprecation")
public class Query implements Observer {
    /**
     * The map on which to display markers when the query matches.
     */
    private final JMapViewer map;

    /**
     * Each query has its own "layer" so they can be turned on and off all at once.
     */
    private final Layer layer;

    /**
     * The color of the outside area of the marker.
     */
    private final Color color;

    /**
     * The string representing the filter for this query.
     */
    private final String queryString;

    /**
     * The filter parsed from the queryString.
     */
    private final Filter filter;

    /**
     * The checkBox in the UI corresponding to this query (so we can turn it on and off and delete it)
     */
    private JCheckBox checkBox;

    /**
     * List of all the query markers that correspond to this query.
     */
    private final List<MapMarkerCircle> markersList;

    public Query(String queryString, Color color, JMapViewer map) {
        this.queryString = queryString;
        this.filter = Filter.parse(queryString);
        this.color = color;
        this.layer = new Layer(queryString);
        this.map = map;
        this.markersList = new ArrayList<>();
    }

    public JMapViewer getMap() {
        return map;
    }

    public Color getColor() {
        return color;
    }

    public String getQueryString() {
        return queryString;
    }

    public Filter getFilter() {
        return filter;
    }

    public Layer getLayer() {
        return layer;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setVisible(boolean visible) {
        layer.setVisible(visible);
    }

    public boolean getVisible() {
        return layer.isVisible();
    }

    public List<MapMarkerCircle> getMarkersList() {
        return markersList;
    }

    /**
     * This query is no longer interesting, so terminate it and remove all traces of its existence.
     */
    public void terminate() {
        layer.setVisible(false);
        for (MapMarkerCircle mapMarkerCircle : markersList) {
            map.removeMapMarker(mapMarkerCircle);
        }
    }

    @Override
    public String toString() {
        return "Query: " + queryString;
    }

    @Override
    public void update(Observable observable, Object o) {
        Status status = (Status) o;
        if (filter.matches(status)) {
            MapMarkerCircle mapMarker = getStatusMapMarker(status);
            map.addMapMarker(mapMarker);
            markersList.add(mapMarker);
        }
    }

    private MapMarkerCircle getStatusMapMarker(Status status) {
        Coordinate coordinate = Util.getStatusCoordinates(status);
        User user = status.getUser();
        String profileImageURL = user.getProfileImageURL();
        String tweet = status.getText();

        return new MapMarkerWithImage(getLayer(), coordinate, getColor(), profileImageURL, tweet);
    }

}

