package org.pac4j.core.authorization.authorizer;

import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link CheckHttpMethodAuthorizer}.
 *
 * @author Jerome Leleu
 * @since 1.8.1
 */
public final class CheckHttpMethodAuthorizerTests {

    private List<UserProfile> profiles;

    @Before
    public void setUp() {
        profiles = new ArrayList<>();
        profiles.add(new CommonProfile());
    }

    @Test
    public void testGoodHttpMethod() {
        val authorizer = new CheckHttpMethodAuthorizer(HttpConstants.HTTP_METHOD.GET,
            HttpConstants.HTTP_METHOD.POST);
        assertTrue(authorizer.isAuthorized(MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.GET.name()),
            new MockSessionStore(), profiles));
    }

    @Test
    public void testBadHttpMethod() {
        val authorizer = new CheckHttpMethodAuthorizer(HttpConstants.HTTP_METHOD.PUT);
        assertFalse(authorizer.isAuthorized(MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.DELETE.name()),
            new MockSessionStore(), profiles));
    }
}
