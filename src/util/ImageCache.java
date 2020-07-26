package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that caches images loaded from twitter urls.
 */
public class ImageCache {
    private BufferedImage defaultImage;
    private final Map<String, BufferedImage> cache;
    private final Map<String, String> pathCache;
    private final static char[] HEX_DIGITS_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Private Constructor.
     */
    private ImageCache() {
        cache = new HashMap<>();
        pathCache = new HashMap<>();
    }

    /**
     * Lazy holder idiom for lazy loading singleton.
     */
    private static class LazyHolder {
        static final ImageCache INSTANCE = new ImageCache();
    }

    /**
     * Getter method for the instance of the singleton.
     *
     * @return The singleton.
     */
    public static ImageCache getInstance() {
        return LazyHolder.INSTANCE;
    }

    public BufferedImage getImageFromCache(String url) {
        BufferedImage ans = cache.get(url);
        if (ans == null) {
            ans = Util.getImageFromURL(url);
            cache.put(url, ans);
        }
        return ans;
    }

    /**
     * Loads the image with the given url into the cache.
     *
     * @param url The url of the image.
     */
    public void putImageInCache(String url) {
        BufferedImage ans = cache.get(url);
        if (ans == null) {
            cache.put(url, defaultImage);
            Thread t = new Thread(() -> {
                BufferedImage ans1 = Util.getImageFromURL(url);
                SwingUtilities.invokeLater(() -> cache.put(url, ans1));
            });
            t.start();
        }
    }

    /**
     * Converts a byte array to Hex and returns them as String.
     *
     * @param bytes The byte array.
     * @return The hex string of the bytes.
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_DIGITS_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_DIGITS_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Hashes some data.
     *
     * @param data The data being hashed.
     * @return The hashed value.
     */
    private String sha256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = data.getBytes();
            md.update(bytes);
            byte[] hash = md.digest();
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't find SHA-256");
        }
    }

    /**
     * Hashes a given url using SHA256 algorithm.
     *
     * @param url The url that will be hashed.
     * @return The hash value.
     */
    private String hashURL(String url) {
        String hash = pathCache.get(url);
        if (hash == null) {
            hash = sha256(url);
        }
        return hash;
    }

    public String imagePath(String url) {
        String path = hashURL(url);
        path = saveImageToFile(getImageFromCache(url), path);
        return path;
    }

    // I'm going to assume that hashing is good enough and collisions are rare enough
    private String saveImageToFile(BufferedImage image, String path) {
        File dir = new File("data/imagecache");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        String pathString = "data/imagecache/" + path + ".png";
        File f = new File(pathString);
        pathString = f.getAbsolutePath();
        if (f.canRead()) {
            return pathString;
        }

        try {
            ImageIO.write(image, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathString;
    }

    /**
     * Getter method for the default image.
     *
     * @return The default image object.
     */
    public BufferedImage getDefaultImage() {
        return defaultImage;
    }

    /**
     * Setter method for the default image.
     *
     * @param defaultImage The new default image.
     */
    public void setDefaultImage(BufferedImage defaultImage) {
        this.defaultImage = defaultImage;
    }
}