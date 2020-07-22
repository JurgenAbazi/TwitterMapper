package twitter;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Provider class for the default Configuration object.
 * Abstract the building of the Configuration object from the rest of the application.
 */
public class ConfigurationProvider {
    /**
     * The consumer API key provided by Twitter for developers.
     */
    private static final String CONSUMER_KEY;

    /**
     * The consumer API key secret provided by Twitter for developers.
     */
    private static final String CONSUMER_SECRET;

    /**
     * The access token string provided by Twitter for developers.
     */
    private static final String ACCESS_TOKEN;

    /**
     * The access token secret string provided by Twitter for developers.
     */
    private static final String ACCESS_TOKEN_SECRET;

    static {
        CONSUMER_SECRET = "";
        ACCESS_TOKEN = "";
        CONSUMER_KEY = "";
        ACCESS_TOKEN_SECRET = "";
    }

    /**
     * Builds the Configuration object using the ConfigurationBuilder and returns it.
     *
     * @return The default twitter API configuration.
     */
    public static Configuration getDefaultConfiguration() {
        return new ConfigurationBuilder()
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();
    }
}
