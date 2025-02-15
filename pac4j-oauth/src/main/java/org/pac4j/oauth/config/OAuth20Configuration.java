package org.pac4j.oauth.config;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth.OAuthService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.generator.RandomValueGenerator;
import org.pac4j.core.util.generator.ValueGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * The OAuh 2.0 configuration.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
@ToString(callSuper = true)
@Getter
@Setter
public class OAuth20Configuration extends OAuthConfiguration {

    public static final String OAUTH_CODE = "code";

    public static final String STATE_REQUEST_PARAMETER = "state";

    /* Map containing user defined parameters */
    private Map<String, String> customParams = new HashMap<>();

    private boolean withState;

    private ValueGenerator stateGenerator = new RandomValueGenerator();

    /**
     * Build an OAuth service from the web context.
     *
     * @param context the web context
     * @param client the client
     * @return the OAuth service
     */
    @Override
    public OAuthService buildService(final WebContext context, final IndirectClient client) {
        init();

        val finalCallbackUrl = client.computeFinalCallbackUrl(context);

        return ((DefaultApi20) api).createService(this.key, this.secret, finalCallbackUrl, this.scope,
            this.responseType, null, null, this.httpClientConfig, null);
    }

    public void setStateGenerator(final ValueGenerator stateGenerator) {
        CommonHelper.assertNotNull("stateGenerator", stateGenerator);
        this.stateGenerator = stateGenerator;
    }
}
