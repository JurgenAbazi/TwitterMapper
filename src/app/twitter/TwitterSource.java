package app.twitter;

import twitter4j.Status;
import app.util.ImageCache;

import java.util.*;
import java.util.Observable;

/**
 * Abstract class that encapsulates the connection to Twitter.
 * Implements Observable - each received tweet is signalled to all observers.
 * Deprecated methods and classes warnings are suppressed since Observer and Observable are requirements of the project.
 */
@SuppressWarnings("deprecation")
public abstract class TwitterSource extends Observable {
    /**
     * If set to true, some methods with log the statuses in the console.
     */
    protected boolean doLogging;

    /**
     * The set of terms to look for in the stream of tweets.
     */
    protected Set<String> terms;

    /**
     * Default constructor.
     */
    protected TwitterSource() {
        doLogging = true;
        terms = new HashSet<>();
    }

    /**
     * Saves the image of the status in the ImageCache.
     * If doLogging is set to true, it logs the status name and text in the console.
     *
     * @param status The twitter status.
     */
    protected void saveStatusImageToCache(Status status) {
        if (doLogging) {
            logStatusInfo(status);
        }
        ImageCache.getInstance().putImageInCache(status.getUser().getProfileImageURL());
    }

    /**
     * Print the name of the author of the status and its text.
     *
     * @param status The status which is being logged.
     */
    private void logStatusInfo(Status status) {
        System.out.println(status.getUser().getName() + ": " + status.getText());
    }

    /**
     * Getter methods for the terms of the filter.
     *
     * @return The terms as an ArrayList.
     */
    public List<String> getFilterTerms() {
        return new ArrayList<>(terms);
    }

    /**
     * Setter method for the set of terms.
     *
     * @param newTerms New collection of terms.
     */
    public void setFilterTerms(Collection<String> newTerms) {
        terms.clear();
        terms.addAll(newTerms);
        sync();
    }

    /**
     * Notifies observers (Queries) each time a new tweet comes so they can determine if it should be displayed or not.
     *
     * @param status The tweet that is delivered to the application.
     */
    protected void handleTweet(Status status) {
        setChanged();
        notifyObservers(status);
    }

    /**
     * Abstract method that is called when a new set of filter terms has been established.
     */
    protected abstract void sync();
}
