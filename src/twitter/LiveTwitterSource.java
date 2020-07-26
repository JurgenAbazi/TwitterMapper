package twitter;

import twitter4j.*;
import twitter4j.conf.Configuration;

/**
 * Encapsulates the connection to Twitter.
 * Terms to include in the returned tweets can be set with setFilterTerms.
 * Implements Observable - each received tweet is signalled to all observers.
 */
public class LiveTwitterSource extends TwitterSource {
    /**
     * Streams tweets in realtime.
     */
    private TwitterStream twitterStream;

    /**
     * Listens for incoming tweets.
     */
    private StatusListener statusListener;

    /**
     * Default Constructor.
     *
     * Initializes the Live Twitter Stream.
     */
    public LiveTwitterSource() {
        initializeTwitterStream();
    }

    /**
     * Method which creates and initializes the TwitterStream.
     */
    private void initializeTwitterStream() {
        Configuration configuration = ConfigurationProvider.getDefaultConfiguration();
        twitterStream = new TwitterStreamFactory(configuration).getInstance();
        initializeListener();
    }

    /**
     * Initializes the Listener.
     * It listens for new statuses, and decides if it should handle them or not.
     */
    private void initializeListener() {
        statusListener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (status.getPlace() != null) {
                    handleTweet(status);
                }
            }
        };
        twitterStream.addListener(statusListener);
    }

    /**
     * Method that is called when a new set of filter terms has been established.
     */
    @Override
    protected void sync() {
        FilterQuery filter = new FilterQuery();
        String[] queriesArray = terms.toArray(new String[0]);
        filter.track(queriesArray);

        System.out.println("Syncing live Twitter stream with " + terms);
        twitterStream.filter(filter);
    }
}
