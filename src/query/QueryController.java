package query;

import twitter.TwitterSource;
import twitter.TwitterSourceFactory;
import twitter.TwitterSourceFactory.SourceType;
import ui.ContentPanel;

import java.util.*;

/**
 * Class which manages all the query display logic.
 * Abstracts them from the main application class.
 */
public class QueryController implements Iterable<Query> {
    /**
     * The list of all queries currently active.
     */
    private final List<Query> queries;

    /**
     * The source of tweets, a TwitterSource, either live or playback.
     */
    private final TwitterSource twitterSource;

    /**
     * Private Constructor.
     */
    private QueryController() {
        twitterSource = new TwitterSourceFactory().getTwitterSource(SourceType.LIVE);
        queries = new ArrayList<>();
    }

    /**
     * Lazy holder idiom for lazy loading singleton.
     */
    private static class LazyHolder {
        static final QueryController INSTANCE = new QueryController();
    }

    /**
     * Getter method for the instance of the singleton.
     *
     * @return The singleton.
     */
    public static QueryController getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * The iterator.
     * Iterates through an unmodifiable version of the original list.
     *
     * @return An iterator object that iterates the queries list.
     */
    @Override
    public Iterator<Query> iterator() {
        List<Query> queries = Collections.unmodifiableList(this.queries);
        return queries.iterator();
    }


    /**
     * A new query has been entered via the User Interface.
     *
     * @param query The new query object.
     * @param contentPanel The panel object that displays the added query.
     */
    public void addQuery(Query query, ContentPanel contentPanel) {
        queries.add(query);
        Set<String> queryTerms = getQueryTerms();
        twitterSource.setFilterTerms(queryTerms);
        contentPanel.addQueryToUI(query);
        twitterSource.addObserver(query);
    }

    /**
     * Return a list of all terms mentioned in all queries. The live twitter source uses this
     * to request matching tweets from the Twitter API.
     *
     * @return Set of query terms.
     */
    private Set<String> getQueryTerms() {
        Set<String> queryTermsSet = new HashSet<>();
        for (Query query : queries) {
            queryTermsSet.addAll(query.getFilter().getTerms());
        }
        return queryTermsSet;
    }

    /**
     * This query is no longer interesting, so terminate it and remove all traces of its existence.
     */
    public void terminateQuery(Query query) {
        query.terminate();
        queries.remove(query);
        twitterSource.setFilterTerms(getQueryTerms());
        twitterSource.deleteObserver(query);
    }
}
