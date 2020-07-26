package util.test;

import org.junit.jupiter.api.Test;
import util.ImageCache;
import util.Util;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for the ImageCache class from the util package.
 */
public class TestImageCache {
    @Test
    public void testGetImageFromCacheWithIncorrectImageLink() {
        BufferedImage ans = ImageCache.getInstance().getImageFromCache("https://www.google.com");
        if (ans.equals(Util.DEFAULT_IMAGE)) {
            return;
        }
        fail("The test should have returned the default image!");
    }

    @Test
    public void testIfGetImagePathSavesImage() {
        String path = ImageCache.getInstance().imagePath("https://upload.wikimedia.org/wikipedia/en/2/26/Led_Zeppelin_-_Led_Zeppelin_IV.jpg");
        assertNotNull(path);
    }
}
