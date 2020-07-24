package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapMarkerWithImage extends MapMarkerCircle {
    public static final double DEFAULT_MARKER_SIZE = 15.0;
    public String tweet;
    public String profileImageURL;
    public BufferedImage userProfileImage;

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

    public BufferedImage getUserProfileImage() {
        return userProfileImage;
    }

    @Override
    public void paint(Graphics g, Point position, int radius) {
        int size = radius * 2;
        if (g instanceof Graphics2D && this.getBackColor() != null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(3));
            g2.setPaint(this.getBackColor());
            g.fillOval(position.x - radius, position.y - radius, size, size);
            g2.setComposite(oldComposite);
            g.drawImage(userProfileImage, position.x - 10, position.y - 10, 20, 20, null);
        }
    }

}