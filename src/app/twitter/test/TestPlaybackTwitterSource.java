package app.twitter.test;

import org.junit.jupiter.api.Test;
import app.twitter.PlaybackTwitterSource;
import app.twitter.TwitterSource;
import app.twitter.TwitterSourceFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static app.twitter.TwitterSourceFactory.SourceType.*;

/**
 * Test the basic functionality of the PlaybackTwitterSource.
 */
public class TestPlaybackTwitterSource {
    /**
     * Method that testes the PlaybackTwitterSource object.
     */
    @Test
    public void testSetup() {
        TwitterSourceFactory factory = new TwitterSourceFactory();
        factory.setSpeedup(1.0);
        PlaybackTwitterSource source = (PlaybackTwitterSource) factory.getTwitterSource(PLAYBACK);

        FakeObserver fakeObserver = new FakeObserver();
        source.addObserver(fakeObserver);

        testTwitterSourceWithOneTermFilter(source, fakeObserver);
        testTwitterSourceWithTwoTermsFilter(source, fakeObserver);
    }

    /**
     * Tests tne source with a filter composed of 1 term.
     *
     * @param source The twitter source where terms are set.
     * @param observer The DummyObserver object that will observe the source.
     */
    private void testTwitterSourceWithOneTermFilter(TwitterSource source, FakeObserver observer) {
        source.setFilterTerms(createSet("food"));
        pause();

        int numberOfTweets = observer.getNumberOfTweets();
        assertTrue(numberOfTweets > 0, "Expected getNTweets() to be > 0, was " + numberOfTweets);
        assertTrue(numberOfTweets <= 10, "Expected getNTweets() to be <= 10, was " + numberOfTweets);
    }

    /**
     * Tests tne source with a filter composed of 2 terms.
     *
     * @param source The twitter source where terms are set.
     * @param observer The DummyObserver object that will observe the source.
     */
    private void testTwitterSourceWithTwoTermsFilter(TwitterSource source, FakeObserver observer) {
        int firstBunch = observer.getNumberOfTweets();
        System.out.println("Now adding 'the'");
        source.setFilterTerms(createSet("food", "the"));
        pause();

        int numberOfTweets = observer.getNumberOfTweets();
        assertTrue(numberOfTweets > 0, "Expected getNTweets() to be > 0, was " + numberOfTweets);
        assertTrue(numberOfTweets > firstBunch, "Expected getNTweets() to be < firstBunch (" + firstBunch + "), was " + numberOfTweets);
        assertTrue(numberOfTweets <= 10, "Expected getNTweets() to be <= 10, was " + numberOfTweets);
    }

    /**
     * Puts the current thread to sleep for 3000 milliseconds.
     */
    private void pause() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a set from elements of same type, that are passed as var-args.
     *
     * @param p   Elements.
     * @param <E> Generic type of the set elements.
     * @return The newly created set.
     */
    @SafeVarargs
    private final <E> Set<E> createSet(E... p) {
        Set<E> ans = new HashSet<>();
        Collections.addAll(ans, p);
        return ans;
    }
}
