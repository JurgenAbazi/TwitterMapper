package app.twitter;

import twitter4j.Status;
import app.util.ObjectSourceStream;

/**
 * Runnable abstract class that reads a playback source object.
 */
public abstract class PlaybackTwitterRunnable implements Runnable {
    /**
     * Object encapsulating the source of the playback file.
     */
    private final ObjectSourceStream source;

    /**
     * The speedup rate with which the files will be read.
     */
    private final double speedup;

    /**
     * The start time of the playback. Set to the current time plus 1000 ms.
     */
    private final long playbackStartTime;

    /**
     * The time in milliseconds of when the recording started.
     */
    private long recordStartTime;

    /**
     * Constructor.
     *
     * @param source The source of the playback file.
     * @param speedup The speedup rate of the playback twitter source.
     */
    public PlaybackTwitterRunnable(ObjectSourceStream source, double speedup) {
        playbackStartTime = System.currentTimeMillis() + 1000;
        recordStartTime = 0;
        this.source = source;
        this.speedup = speedup;
    }

    /**
     * The run method.
     * Constantly reads the objects from the source stream until there are no more left.
     * Performs an operation on each status,
     */
    @Override
    public void run() {
        long now;
        while (true) {
            Object timeObject = source.readObject();
            if (timeObject == null) {
                break;
            }

            Object statusObject = source.readObject();
            if (statusObject == null) {
                break;
            }

            long statusTime = (Long) timeObject;
            if (recordStartTime == 0) {
                recordStartTime = statusTime;
            }

            Status status = (Status) statusObject;
            long playbackTime = computePlaybackTime(statusTime);
            while ((now = System.currentTimeMillis()) < playbackTime) {
                pause(playbackTime - now);
            }

            if (status.getPlace() != null) {
                operation(status);
            }
        }
    }

    /**
     * Method which computes the playback time.
     *
     * @param statusTime The time when the status arrived.
     * @return The playback time.
     */
    private long computePlaybackTime(long statusTime) {
        long statusDelta = statusTime - recordStartTime;
        long targetDelta = Math.round(statusDelta / speedup);
        return playbackStartTime + targetDelta;
    }

    /**
     * Puts the current thread to sleep for a certain amount of time.
     *
     * @param millis The amount of milliseconds that the thread will be put to sleep.
     */
    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abstract method for performing an operation on a status.
     * Used to handle the tweet.
     *
     * @param status The status on which an operation will be performed.
     */
    public abstract void operation(Status status);
}
