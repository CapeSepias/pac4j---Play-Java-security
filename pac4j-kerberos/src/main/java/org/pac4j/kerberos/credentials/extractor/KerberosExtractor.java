package org.pac4j.kerberos.credentials.extractor;

import lombok.val;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.profile.factory.ProfileManagerFactory;
import org.pac4j.kerberos.credentials.KerberosCredentials;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * To extract Kerberos headers.
 *
 * @author Garry Boyce
 * @since 2.1.0
 */
public class KerberosExtractor implements CredentialsExtractor {

    @Override
    public Optional<Credentials> extract(final WebContext context, final SessionStore sessionStore,
                                         final ProfileManagerFactory profileManagerFactory) {
        val optHeader = context.getRequestHeader(HttpConstants.AUTHORIZATION_HEADER);
        if (!optHeader.isPresent()) {
            return Optional.empty();
        }

        val header = optHeader.get();
        if (!(header.startsWith("Negotiate ") || header.startsWith("Kerberos "))) {
            // "Authorization" header do not indicate Kerberos mechanism yet,
            // so the extractor shouldn't throw an exception
            return Optional.empty();
        }

        var base64Token = header.substring(header.indexOf(" ") + 1).getBytes(StandardCharsets.UTF_8);
        var kerberosTicket = Base64.getDecoder().decode(base64Token);

        return Optional.of(new KerberosCredentials(kerberosTicket));
    }
}
