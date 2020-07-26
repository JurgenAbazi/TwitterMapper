package app.twitter.test;

import java.util.Observable;
import java.util.Observer;

/**
 * A Dummy Observer Class used for testing purposes.
 * Deprecation warnings are suppressed since Observer and Observable are requirements of the project.
 */
@SuppressWarnings("deprecation")
public class FakeObserver implements Observer {
    /**
     * Counter that stores the number of tweets.
     */
    private int numberOfTweets;

    /**
     * Default Constructor.
     */
    public FakeObserver() {
        numberOfTweets = 0;
    }

    /**
     * Method used to increment the tweet counter each time a new status is handled.
     *
     * @param o   The observable object.
     * @param arg The incoming status.
     */
    @Override
    public void update(Observable o, Object arg) {
        numberOfTweets++;
    }

    /**
     * Getter method for the number of tweets counter.
     *
     * @return The number of tweets.
     */
    public int getNumberOfTweets() {
        return numberOfTweets;
    }
}
