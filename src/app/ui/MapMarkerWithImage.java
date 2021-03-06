package app.ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import app.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapMarkerWithImage extends MapMarkerCircle {
    public static final double DEFAULT_MARKER_SIZE = 15.0;
    private final String tweet;
    private final String profileImageURL;
    private final BufferedImage userProfileImage;

    public MapMarkerWithImage(Layer layer, Coordinate coordinate, Color color, String profileImageURL, String tweet) {
        super(layer, null, coordinate, DEFAULT_MARKER_SIZE, STYLE.FIXED, getDefaultStyle());
        setColor(Color.BLACK);
        setBackColor(color);

        this.tweet = tweet;
        this.profileImageURL = profileImageURL;
        userProfileImage = Util.getImageFromURL(profileImageURL);
    }

    public String getTweet() {
        return this.tweet;
    }

    public String getProfileImageURL() {
        return this.profileImageURL;
    }

    @Override
    public void paint(Graphics graphics, Point position, int radius) {
        if (!(graphics instanceof Graphics2D) || this.getBackColor() == null) {
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics;
        Composite temporaryComposite = graphics2D.getComposite();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        graphics2D.setPaint(this.getBackColor());

        int size = radius * 2;
        graphics.fillOval(position.x - radius, position.y - radius, size, size);
        graphics2D.setComposite(temporaryComposite);
        graphics.drawImage(userProfileImage, position.x - 10, position.y - 10, 20, 20, null);
    }

}