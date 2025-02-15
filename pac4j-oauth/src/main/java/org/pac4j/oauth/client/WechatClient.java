package org.pac4j.oauth.client;

import org.pac4j.oauth.profile.wechat.WechatProfile;
import org.pac4j.oauth.profile.wechat.WechatProfileCreator;
import org.pac4j.oauth.profile.wechat.WechatProfileDefinition;
import org.pac4j.scribe.builder.api.WechatApi20;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is the OAuth client to authenticate users in Tencent Wechat.</p>
 * <p>It returns a {@link WechatProfile}.</p>
 *
 * @author zhangzhenli
 * @since 3.1.0
 */
public class WechatClient extends OAuth20Client {

    public enum WechatScope {
        /**
         * Only for WeChat QRCode login. Get the nickname, avatar, and gender of the logged in user.
         */
        SNSAPI_LOGIN,
        /**
         * Exchange code for access_token, refresh_token, and authorized scope
         */
        SNSAPI_BASE,
        /**
         * Get user personal information
         */
        SNSAPI_USERINFO
    }

    protected List<WechatScope> scopes;


    public WechatClient() {
    }

    public WechatClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected void internalInit(final boolean forceReinit) {
        configuration.setApi(new WechatApi20());
        configuration.setScope(getOAuthScope());
        configuration.setProfileDefinition(new WechatProfileDefinition());
        configuration.setWithState(true);
        defaultProfileCreator(new WechatProfileCreator(configuration, this));

        super.internalInit(forceReinit);
    }

    protected String getOAuthScope() {
        StringBuilder builder = null;
        if (scopes == null || scopes.isEmpty()) {
            scopes = new ArrayList<>();
            scopes.add(WechatScope.SNSAPI_BASE);
        }
        if (scopes != null) {
            for (var value : scopes) {
                if (builder == null) {
                    builder = new StringBuilder();
                } else {
                    builder.append(",");
                }
                builder.append(value.toString().toLowerCase());
            }
        }
        return builder == null ? null : builder.toString();
    }

    public List<WechatScope> getScopes() {
        return scopes;
    }

    public void setScopes(List<WechatScope> scopes) {
        this.scopes = scopes;
    }

    public void addScope(WechatScope scopes) {
        if (this.scopes == null)
            this.scopes = new ArrayList<>();
        this.scopes.add(scopes);
    }
}
