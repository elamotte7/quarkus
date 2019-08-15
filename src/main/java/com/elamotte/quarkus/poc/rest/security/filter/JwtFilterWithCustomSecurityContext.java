package com.elamotte.quarkus.poc.rest.security.filter;

import com.elamotte.quarkus.poc.rest.annotation.JWTTokenNeeded;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.apache.http.HttpHeaders;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;

@Provider
@ApplicationScoped
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JwtFilterWithCustomSecurityContext implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtFilterWithCustomSecurityContext.class);

    @ConfigProperty(name = "oidc.verify.jwk.issuer")
    String issuer;

    @ConfigProperty(name = "oidc.verify.jwk.set")
    String jwkSet;

    @Context
    UriInfo uriInfo;

    JWTClaimsSet claimsSet;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        try {
            checkEnvVar();
        } catch (Exception e) {
            LOG.error("An OIDC configuration exception occured:", e);
        }

        String jwtToken = "";
        try {
            jwtToken = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION).replaceFirst("Bearer ", "");
        } catch (NullPointerException e) {
            LOG.error("#### invalid token : NO TOKEN!");
            requestContext.abortWith(Response.ok("#### invalid token : NO TOKEN!").status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        claimsSet = null;
        try {
            claimsSet = this.validateToken(jwtToken, this.jwkSet);
        } catch (Exception e) {
            LOG.error("#### invalid token : " + jwtToken, e);
            requestContext.abortWith(Response.ok("#### invalid token : " + jwtToken).status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        // Print out the token claims set
        LOG.debug(claimsSet.toJSONObject().toJSONString());

        // TODO : check the required claims are present

        if (claimsSet != null && claimsSet.getSubject() != null) {
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> claimsSet.getSubject();
                }

                @Override
                public boolean isUserInRole(String role) {
                    // TODO : récupérer le bon claim des groupes/roles
//                    List<Role> roles = claimsSet.getClaim("groups").;
//                    List<Role> roles = claimsSet.getClaim("roles").;
//                    return roles.contains(role);
                    // TODO : récupération des rôles s'ils existent, sinon positionner par défaut en mode consultation
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return uriInfo.getAbsolutePath().toString().startsWith("https");
                }

                @Override
                public String getAuthenticationScheme() {
                    return "OIDC-JWT";
                }
            });
        }

    }

    private void checkEnvVar() throws Exception {
        if (issuer == null || issuer.isEmpty() || issuer.contains("{")) {
            issuer = System.getenv("OIDC_ISSUER");
        }
        if (jwkSet == null || issuer.isEmpty() || jwkSet.contains("{")) {
            jwkSet = System.getenv("OIDC_PROVIDER_METADATA_URL");
        }
        if (jwkSet == null || jwkSet.isEmpty()) {
            throw new Exception("No env var for OIDC!");
        }
    }

    private JWTClaimsSet validateToken(String token, String jwkUri) throws Exception {
        // Set up a JWT processor to parse the tokens and then check their signature
        // and validity time window (bounded by the "iat", "nbf" and "exp" claims)
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();

        // The public RSA keys to validate the signatures will be sourced from the
        // OAuth 2.0 server's JWK set, published at a well-known URL. The RemoteJWKSet
        // object caches the retrieved keys to speed up subsequent look-ups and can
        // also gracefully handle key-rollover
        JWKSource keySource = new RemoteJWKSet(new URL(jwkUri));

        // The expected JWS algorithm of the access tokens (agreed out-of-band)
        JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;

        // Configure the JWT processor with a key selector to feed matching public
        // RSA keys sourced from the JWK set URL
        JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        // Process the token
        com.nimbusds.jose.proc.SecurityContext ctx = null; // optional context parameter, not required here

        // validate claims
        jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<com.nimbusds.jose.proc.SecurityContext>() {
            @Override
            public void verify(JWTClaimsSet claimsSet, com.nimbusds.jose.proc.SecurityContext ctx)
                    throws BadJWTException {

                super.verify(claimsSet, ctx);

                // If present the actual expiration timestamp is checked by the
                // overridden method
                if (claimsSet.getExpirationTime() == null) {
                    throw new BadJWTException("Missing token expiration claim");
                }

                if (!issuer.equals(claimsSet.getIssuer())) {
                    throw new BadJWTException("Token issuer not accepted");
                }
            }
        });

        return jwtProcessor.process(token, ctx);
    }
}
