package app.ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import app.query.Query;
import app.query.QueryController;
import app.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * The Twitter viewer application.
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 */
public class ApplicationMainPanel extends JFrame {
    /**
     * The content panel, which contains the entire UI.
     */
    private ContentPanel contentPanel;

    /**
     * The provider of the tiles for the map, we use the Bing source.
     */
    private BingAerialTileSource bing;

    /**
     * Query Manager object.
     */
    private QueryController queryController;

    /**
     * Default Constructor.
     */
    public ApplicationMainPanel() {
        super("Twitter content viewer");
        initialize();
        configureMap();
        scheduleBingTimer();
        addMapMouseListeners();
    }

    /**
     * Initializes the attributes and builds GUI.
     */
    private void initialize() {
        setSize(300, 300);

        bing = new BingAerialTileSource();

        contentPanel = new ContentPanel(this);
        queryController = QueryController.getInstance();
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Configures the map. Displays markers, zoom controls and enables wrap.
     */
    private void configureMap() {
        getMap().setMapMarkerVisible(true);
        getMap().setZoomContolsVisible(true);
        getMap().setScrollWrapEnabled(true);
        getMap().setTileSource(bing);
    }

    /**
     * Method is used to load the tiles once the Bing attribution is ready.
     */
    private void scheduleBingTimer() {
        Coordinate coordinate = new Coordinate(0, 0);
        Timer bingTimer = new Timer();
        TimerTask bingAttributionCheck = new TimerTask() {
            @Override
            public void run() {
                String attributionText = bing.getAttributionText(0, coordinate, coordinate);
                if (!attributionText.equals("Error loading Bing attribution data")) {
                    getMap().setZoom(2);
                    bingTimer.cancel();
                }
            }
        };
        bingTimer.schedule(bingAttributionCheck, 100, 200);
    }

    /**
     * Set up a motion listener to create a tooltip showing the tweets at the pointer position
     */
    private void addMapMouseListeners() {
        getMap().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                ICoordinate pos = getMap().getPosition(p);

                List<MapMarker> markerList = getMarkersCovering(pos, pixelWidth(p));
                if (!markerList.isEmpty()) {
                    MapMarker marker = markerList.get(markerList.size() - 1);
                    MapMarkerWithImage mapMarkerWithImage = (MapMarkerWithImage) marker;
                    String tweet = mapMarkerWithImage.getTweet();
                    String profilePictureURL = mapMarkerWithImage.getProfileImageURL();
                    getMap().setToolTipText("<html><img src=" + profilePictureURL + " height=\"40\" width=\"40\">" + tweet + "</html>");
                }
            }
        });
    }

    /**
     * A new query has been entered via the User Interface.
     *
     * @param query The new query object.
     */
    public void addQuery(Query query) {
        queryController.addQuery(query, contentPanel);
    }

    /**
     * How big is a single pixel on the map? Used to compute which tweet markers are at the current most position.
     *
     * @param p Point in the map.
     * @return The width of a pixel.
     */
    private double pixelWidth(Point p) {
        ICoordinate center = getMap().getPosition(p);
        ICoordinate edge = getMap().getPosition(new Point(p.x + 1, p.y));
        return Util.getDistanceBetweenPoints(center, edge);
    }

    /**
     * Get those layers (of tweet markers) that are visible because their corresponding query is enabled
     *
     * @return A set of layers.
     */
    private Set<Layer> getVisibleLayers() {
        Set<Layer> set = new HashSet<>();
        for (Query query : queryController) {
            if (query.getVisible()) {
                Layer layer = query.getLayer();
                set.add(layer);
            }
        }
        return set;
    }

    /**
     * Get all the markers at the given map position, at the current map zoom setting.
     *
     * @param pos        Current map position.
     * @param pixelWidth The width of the pixel.
     * @return The map markers in that position.
     */
    private List<MapMarker> getMarkersCovering(ICoordinate pos, double pixelWidth) {
        List<MapMarker> ans = new ArrayList<>();
        Set<Layer> visibleLayers = getVisibleLayers();
        for (MapMarker m : getMap().getMapMarkerList()) {
            if (visibleLayers.contains(m.getLayer())) {
                double distance = Util.getDistanceBetweenPoints(m.getCoordinate(), pos);
                double radiusInPixels = m.getRadius() * pixelWidth;
                if (distance < radiusInPixels) {
                    ans.add(m);
                }
            }
        }
        return ans;
    }

    /**
     * Returns the Map object.
     *
     * @return The map.
     */
    public JMapViewer getMap() {
        return contentPanel.getMapViewer();
    }

    /**
     * Update which queries are visible after any checkBox has been changed.
     */
    public void updateVisibility() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Recomputing visible queries");
            for (Query query : queryController) {
                query.setVisible(query.getCheckBox().isSelected());
            }
            getMap().repaint();
        });
    }

    /**
     * Disconnects the expiring query from the twitter source. Removes all the traces of the query.
     *
     * @param query The query being terminated.
     */
    public void terminateQuery(Query query) {
        queryController.terminateQuery(query);
    }
}
