package twitter;

import twitter4j.Status;
import util.ObjectSourceStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A Twitter source that plays back a recorded stream of tweets.
 * Uses the terms set only on first call to setFilterTerms as signal to begin playback of the recorded stream of tweets.
 */
public class PlaybackTwitterSource extends TwitterSource {
    /**
     * The speedup rate to apply to the recorded stream of tweets.
     */
    private final double speedup;

    /**
     * Flags if a thread has started working or not
     */
    private boolean threadStarted;

    /**
     * The source of the recorded stream of tweets.
     */
    private final ObjectSourceStream source;

    /**
     * 1-arg Constructor.
     *
     * @param speedup The speedup rate for the playback.
     */
    public PlaybackTwitterSource(double speedup) {
        this.source = new ObjectSourceStream("data/TwitterCapture.jobj");
        this.threadStarted = false;
        this.speedup = speedup;
    }

    /**
     * The playback source merely starts the playback thread, it it hasn't been started already.
     */
    protected void sync() {
        System.out.println("Starting playback thread with " + terms);
        executeThread();
    }

    /**
     * Executes the thread that reads the recorded stream of tweets.
     */
    private void executeThread() {
        if (!threadStarted) {
            threadStarted = true;
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new PlaybackTwitterRunnable(source, speedup) {
                @Override
                public void operation(Status status) {
                    handleTweet(status);
                }
            });
            executorService.shutdown();
        }
    }
}
