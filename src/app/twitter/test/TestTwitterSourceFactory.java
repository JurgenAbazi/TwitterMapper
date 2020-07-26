package app.twitter.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import app.twitter.LiveTwitterSource;
import app.twitter.PlaybackTwitterSource;
import app.twitter.TwitterSource;
import app.twitter.TwitterSourceFactory;
import app.twitter.TwitterSourceFactory.SourceType;

/**
 * Tests for the TwitterSourceFactory class.
 */
public class TestTwitterSourceFactory {
    @Test
    public void testInvalidParameterToTwitterFactory() {
        try {
            new TwitterSourceFactory().getTwitterSource(null);
            Assertions.fail("Exception was not thrown");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testLiveSourceTwitterRequestFromFactory() {
        TwitterSource twitterSource = new TwitterSourceFactory().getTwitterSource(SourceType.LIVE);
        if (twitterSource instanceof LiveTwitterSource) {
            return;
        }
        Assertions.fail();
    }

    @Test
    public void testPlaybackSourceTwitterRequestFromFactory() {
        TwitterSource twitterSource = new TwitterSourceFactory().getTwitterSource(SourceType.PLAYBACK);
        if (twitterSource instanceof PlaybackTwitterSource) {
            return;
        }
        Assertions.fail();
    }
}
