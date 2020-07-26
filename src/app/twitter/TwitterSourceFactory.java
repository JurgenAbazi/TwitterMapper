package app.twitter;

import java.security.InvalidParameterException;

/**
 * Factory Method design pattern implementation for the Twitter Source.
 * Abstracts the creation and implementation of the Twitter Sources from the rest of the application.
 */
public class TwitterSourceFactory {
    /**
     * The speedup rate of the playback twitter source.
     * Servers no purpose for the live twitter source.
     */
    private double speedup;

    /**
     * Default Constructor.
     */
    public TwitterSourceFactory() {
        speedup = 1.0;
    }

    /**
     * Setter method for the speedup attribute.
     * To be used when the rate of the playback source must be changed.
     *
     * @param speedup The new speedup value.
     */
    public void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

    /**
     * The factory method.
     *
     * @param type The type of twitter source that the method must return.
     * @return The specialized TwitterSource object.
     */
    public TwitterSource getTwitterSource(SourceType type) {
        if (type == SourceType.LIVE) {
            return new LiveTwitterSource();
        } else if (type == SourceType.PLAYBACK) {
            return new PlaybackTwitterSource(speedup);
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * Enumeration for the different source types.
     * Used as arguments for the Factory Method.
     */
    public enum SourceType {
        LIVE,
        PLAYBACK
    }
}
