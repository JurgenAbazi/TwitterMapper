package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import query.Query;
import twitter.TwitterSource;
import twitter.TwitterSourceFactory;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

import static twitter.TwitterSourceFactory.SourceType.*;

/**
 * The Twitter viewer application.
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 */
public class Application extends JFrame {
    /**
     * The content panel, which contains the entire UI.
     */
    private ContentPanel contentPanel;

    /**
     * The provider of the tiles for the map, we use the Bing source.
     */
    private BingAerialTileSource bing;

    /**
     * A list of all of the active queries.
     */
    private List<Query> queries;

    /**
     * The source of tweets, a TwitterSource, either live or playback.
     */
    private TwitterSource twitterSource;

    /**
     * Default Constructor.
     */
    public Application() {
        super("Twitter content viewer");
        initialize();
        configureMap();
        scheduleBingTimer();
    }

    /**
     * Initializes the attributes and builds GUI.
     */
    private void initialize() {
        twitterSource = new TwitterSourceFactory().getTwitterSource(LIVE);
        queries = new ArrayList<>();
        bing = new BingAerialTileSource();
        contentPanel = new ContentPanel(this);

        setSize(300, 300);
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
        addMapMouseListeners();
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
                    String profilePictureURL = mapMarkerWithImage.getProfileImageUrl();
                    getMap().setToolTipText("<html><img src=" + profilePictureURL + " height=\"42\" width=\"42\">" + tweet + "</html>");
                }
            }
        });
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
                if (!bing.getAttributionText(0, coordinate, coordinate).equals("Error loading Bing attribution data")) {
                    getMap().setZoom(2);
                    bingTimer.cancel();
                }
            }
        };
        bingTimer.schedule(bingAttributionCheck, 100, 200);
    }

    /**
     * A new query has been entered via the User Interface
     *
     * @param query The new query object
     */
    public void addQuery(Query query) {
        Set<String> allTerms = getQueryTerms();
        queries.add(query);
        twitterSource.setFilterTerms(allTerms);
        contentPanel.addQueryToUI(query);
        twitterSource.addObserver(query);
    }

    /**
     * Returns a list of all terms mentioned in all queries. The live twitter source uses this
     * to request matching tweets from the Twitter API.
     *
     * @return Set containing all query terms.
     */
    private Set<String> getQueryTerms() {
        Set<String> ans = new HashSet<>();
        queries.stream().map(q -> q.getFilter().terms()).forEach(ans::addAll);
        return ans;
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
        return Util.distanceBetween(center, edge);
    }

    /**
     * Get those layers (of tweet markers) that are visible because their corresponding query is enabled
     *
     * @return A set of layers.
     */
    private Set<Layer> getVisibleLayers() {
        return queries.stream()
                .filter(Query::getVisible)
                .map(Query::getLayer)
                .collect(Collectors.toSet());
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
                double distance = Util.distanceBetween(m.getCoordinate(), pos);
                if (distance < m.getRadius() * pixelWidth) {
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
     * @param args Application program arguments (which are ignored).
     */
    public static void main(String[] args) {
        new Application().setVisible(true);
    }

    /**
     * Update which queries are visible after any checkBox has been changed.
     */
    public void updateVisibility() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Recomputing visible queries");
            queries.forEach(query -> query.setVisible(query.getCheckBox().isSelected()));
            getMap().repaint();
        });
    }

    /**
     * Disconnects the expiring query from the twitter source. Removes all the traces of the query.
     *
     * @param query The query being terminated.
     */
    public void terminateQuery(Query query) {
        query.terminate();
        twitterSource.deleteObserver(query);
        queries.remove(query);

        Set<String> allTerms = getQueryTerms();
        twitterSource.setFilterTerms(allTerms);
    }
}
