package org.pac4j.core.authorization.authorizer;

import lombok.val;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.List;
import java.util.Set;

/**
 * Checks an access if the user profile has any of the roles.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
public class RequireAnyRoleAuthorizer extends AbstractRequireAnyAuthorizer<String> {

    public RequireAnyRoleAuthorizer() { }

    public RequireAnyRoleAuthorizer(final String... roles) {
        setElements(roles);
    }

    public RequireAnyRoleAuthorizer(final List<String> roles) {
        setElements(roles);
    }

    public RequireAnyRoleAuthorizer(final Set<String> roles) { setElements(roles); }

    @Override
    protected boolean check(final WebContext context, final SessionStore sessionStore, final UserProfile profile, final String element) {
        val profileRoles = profile.getRoles();
        if( profileRoles.isEmpty() ) {
            return false;
        }
        if( element == null ) {
            return true;
        }
        return profileRoles.contains(element);
    }

    public static RequireAnyRoleAuthorizer requireAnyRole(String ... roles) {
        return new RequireAnyRoleAuthorizer(roles);
    }

    public static RequireAnyRoleAuthorizer requireAnyRole(List<String> roles) {
        return new RequireAnyRoleAuthorizer(roles);
    }

    public static RequireAnyRoleAuthorizer requireAnyRole(Set<String> roles) {
        return new RequireAnyRoleAuthorizer(roles);
    }
}
