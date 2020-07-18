package twitter.test;

import org.junit.jupiter.api.Test;
import twitter.PlaybackTwitterSource;
import twitter4j.Status;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the basic functionality of the TwitterSource
 */
public class TestPlaybackTwitterSource {

    @Test
    public void testSetup() {
        PlaybackTwitterSource source = new PlaybackTwitterSource(1.0);
        TestObserver to = new TestObserver();
        source.addObserver(to);
        source.setFilterTerms(set("food"));

        pause(3000);
        assertTrue(to.getNTweets() > 0, "Expected getNTweets() to be > 0, was " + to.getNTweets());
        assertTrue(to.getNTweets() <= 10, "Expected getNTweets() to be <= 10, was " + to.getNTweets());

        int firstBunch = to.getNTweets();
        System.out.println("Now adding 'the'");
        source.setFilterTerms(set("food", "the"));

        pause(3000);
        assertTrue(to.getNTweets() > 0, "Expected getNTweets() to be > 0, was " + to.getNTweets());
        assertTrue(to.getNTweets() > firstBunch, "Expected getNTweets() to be < firstBunch (" + firstBunch + "), was " + to.getNTweets());
        assertTrue(to.getNTweets() <= 10, "Expected getNTweets() to be <= 10, was " + to.getNTweets());
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SafeVarargs
    private final <E> Set<E> set(E... p) {
        Set<E> ans = new HashSet<>();
        Collections.addAll(ans, p);
        return ans;
    }

    private static class TestObserver implements Observer {
        private int nTweets = 0;

        @Override
        public void update(Observable o, Object arg) {
            Status s = (Status) arg;
            nTweets++;
        }

        public int getNTweets() {
            return nTweets;
        }
    }
}
