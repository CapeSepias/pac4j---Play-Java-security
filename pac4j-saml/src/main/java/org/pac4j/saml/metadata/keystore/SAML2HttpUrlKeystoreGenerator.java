package org.pac4j.saml.metadata.keystore;

import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.saml.config.SAML2Configuration;
import org.pac4j.saml.exceptions.SAMLException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * This is {@link SAML2HttpUrlKeystoreGenerator}.
 *
 * @author Misagh Moayyed
 * @since 4.0.1
 */
public class SAML2HttpUrlKeystoreGenerator extends BaseSAML2KeystoreGenerator {

    public SAML2HttpUrlKeystoreGenerator(final SAML2Configuration configuration) {
        super(configuration);
    }

    @Override
    public InputStream retrieve() throws Exception {
        validate();

        val url = saml2Configuration.getKeystoreResource().getURL().toExternalForm();
        logger.debug("Loading keystore from {}", url);
        val httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", ContentType.TEXT_PLAIN.getMimeType());
        httpGet.addHeader("Content-Type", ContentType.TEXT_PLAIN.getMimeType());
        HttpResponse response = null;
        try {
            response = saml2Configuration.getHttpClient().execute(httpGet);
            if (response != null) {
                val code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_OK) {
                    logger.info("Successfully submitted/created keystore to {}", url);
                    val results = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
                    return new ByteArrayInputStream(Base64.getDecoder().decode(results));
                }
            }
            throw new SAMLException("Unable to retrieve keystore from " + url);
        } finally {
            if (response != null && response instanceof CloseableHttpResponse) {
                ((CloseableHttpResponse) response).close();
            }
        }
    }

    @Override
    protected void store(final KeyStore ks, final X509Certificate certificate,
                         final PrivateKey privateKey) throws Exception {
        validate();

        HttpResponse response = null;
        try (var out = new ByteArrayOutputStream()) {
            val password = saml2Configuration.getKeystorePassword().toCharArray();
            ks.store(out, password);
            out.flush();
            val content = Base64.getEncoder().encodeToString(out.toByteArray());

            if (logger.isTraceEnabled()) {
                logger.trace("Encoded keystore as base-64: {}", content);
            }

            val url = saml2Configuration.getKeystoreResource().getURL().toExternalForm();
            val httpPost = new HttpPost(url);
            httpPost.addHeader("Accept", ContentType.TEXT_PLAIN.getMimeType());
            httpPost.addHeader("Content-Type", ContentType.TEXT_PLAIN.getMimeType());
            httpPost.setEntity(new StringEntity(content, ContentType.TEXT_PLAIN));
            logger.debug("Submitting keystore to {}", url);

            response = saml2Configuration.getHttpClient().execute(httpPost);
            if (response != null) {
                val code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_NOT_IMPLEMENTED) {
                    logger.info("Storing keystore is not supported/implemented by {}", url);
                } else if (code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                    logger.info("Successfully submitted/created keystore to {}", url);
                } else if (code == HttpStatus.SC_NOT_MODIFIED) {
                    logger.info("Keystore was not modified/updated: {}", url);
                } else {
                    logger.error("Unable to store keystore successfully via {}", url);
                }
            }
        } finally {
            if (response != null && response instanceof CloseableHttpResponse) {
                ((CloseableHttpResponse) response).close();
            }
        }
    }

    private void validate() {
        CommonHelper.assertNotNull("keystoreResource", saml2Configuration.getKeystoreResource());
        CommonHelper.assertNotBlank("keystorePassword", saml2Configuration.getPrivateKeyPassword());
        CommonHelper.assertNotBlank("privateKeyPassword", saml2Configuration.getPrivateKeyPassword());
    }
}
