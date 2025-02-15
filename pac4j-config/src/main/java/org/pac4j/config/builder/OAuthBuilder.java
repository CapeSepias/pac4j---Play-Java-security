package org.pac4j.config.builder;

import lombok.val;
import org.pac4j.core.client.Client;
import org.pac4j.oauth.client.*;

import java.util.List;
import java.util.Map;

import static org.pac4j.core.util.CommonHelper.isNotBlank;

/**
 * Builder for OAuth clients.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public class OAuthBuilder extends AbstractBuilder {

    public OAuthBuilder(final Map<String, String> properties) {
        super(properties);
    }

    public void tryCreateLinkedInClient(final List<Client> clients) {
        val id = getProperty(LINKEDIN_ID);
        val secret = getProperty(LINKEDIN_SECRET);
        val scope = getProperty(LINKEDIN_SCOPE);

        if (isNotBlank(id) && isNotBlank(secret)) {
            val linkedInClient = new LinkedIn2Client(id, secret);
            if (isNotBlank(scope)) {
                linkedInClient.setScope(scope);
            }
            clients.add(linkedInClient);
        }
    }

    public void tryCreateFacebookClient(final List<Client> clients) {
        val id = getProperty(FACEBOOK_ID);
        val secret = getProperty(FACEBOOK_SECRET);
        val scope = getProperty(FACEBOOK_SCOPE);
        val fields = getProperty(FACEBOOK_FIELDS);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val facebookClient = new FacebookClient(id, secret);
            if (isNotBlank(scope)) {
                facebookClient.setScope(scope);
            }
            if (isNotBlank(fields)) {
                facebookClient.setFields(fields);
            }
            clients.add(facebookClient);
        }
    }

    public void tryCreateWindowsLiveClient(final List<Client> clients) {
        val id = getProperty(WINDOWSLIVE_ID);
        val secret = getProperty(WINDOWSLIVE_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val client = new WindowsLiveClient(id, secret);
            clients.add(client);
        }
    }

    public void tryCreateFoursquareClient(final List<Client> clients) {
        val id = getProperty(FOURSQUARE_ID);
        val secret = getProperty(FOURSQUARE_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val client = new FoursquareClient(id, secret);
            clients.add(client);
        }
    }

    public void tryCreateGoogleClient(final List<Client> clients) {
        val id = getProperty(GOOGLE_ID);
        val secret = getProperty(GOOGLE_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val client = new Google2Client(id, secret);
            val scope = getProperty(GOOGLE_SCOPE);
            if (isNotBlank(scope)) {
                client.setScope(Google2Client.Google2Scope.valueOf(scope.toUpperCase()));
            }
            clients.add(client);
        }
    }

    public void tryCreateYahooClient(final List<Client> clients) {
        val id = getProperty(YAHOO_ID);
        val secret = getProperty(YAHOO_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val client = new YahooClient(id, secret);
            clients.add(client);
        }
    }

    public void tryCreateDropboxClient(final List<Client> clients) {
        val id = getProperty(DROPBOX_ID);
        val secret = getProperty(DROPBOX_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val client = new DropBoxClient(id, secret);
            clients.add(client);
        }
    }

    public void tryCreateGithubClient(final List<Client> clients) {
        val id = getProperty(GITHUB_ID);
        val secret = getProperty(GITHUB_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val client = new GitHubClient(id, secret);
            clients.add(client);
        }
    }

    public void tryCreateTwitterClient(final List<Client> clients) {
        val id = getProperty(TWITTER_ID);
        val secret = getProperty(TWITTER_SECRET);
        if (isNotBlank(id) && isNotBlank(secret)) {
            val twitterClient = new TwitterClient(id, secret);
            clients.add(twitterClient);
        }
    }

    public void tryCreateGenericOAuth2Clients(final List<Client> clients) {
        for (var i = 0; i <= MAX_NUM_CLIENTS; i++) {
            val id = getProperty(OAUTH2_ID, i);
            val secret = getProperty(OAUTH2_SECRET, i);

            if (isNotBlank(id) && isNotBlank(secret)) {
                val client = new GenericOAuth20Client();
                client.setName(concat(client.getName(), i));

                client.setKey(id);
                client.setSecret(secret);

                client.setAuthUrl(getProperty(OAUTH2_AUTH_URL, i));
                client.setTokenUrl(getProperty(OAUTH2_TOKEN_URL, i));
                client.setProfileUrl(getProperty(OAUTH2_PROFILE_URL, i));
                client.setProfilePath(getProperty(OAUTH2_PROFILE_PATH, i));
                client.setProfileId(getProperty(OAUTH2_PROFILE_ID, i));
                client.setScope(getProperty(OAUTH2_SCOPE, i));

                if (containsProperty(OAUTH2_WITH_STATE, i)) {
                    client.setWithState(getPropertyAsBoolean(OAUTH2_WITH_STATE, i));
                }
                if (containsProperty(OAUTH2_CLIENT_AUTHENTICATION_METHOD, i)) {
                    client.setClientAuthenticationMethod(getProperty(OAUTH2_CLIENT_AUTHENTICATION_METHOD, i));
                }

                clients.add(client);
            }
        }
    }
}
