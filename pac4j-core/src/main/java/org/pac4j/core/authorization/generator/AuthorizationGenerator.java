package org.pac4j.core.authorization.generator;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.Optional;

/**
 * Generate the authorization roles for this user profile.
 *
 * @author Jerome Leleu
 * @since 1.5.0
 */
@FunctionalInterface
public interface AuthorizationGenerator {

    /**
     * Generate the authorization information from and for the user profile.
     *
     * @param context the web context
     * @param sessionStore the session store
     * @param profile the user profile for which to generate the authorization information.
     * @return the updated profile or a new one (optional)
     */
    Optional<UserProfile> generate(WebContext context, SessionStore sessionStore, UserProfile profile);
}
