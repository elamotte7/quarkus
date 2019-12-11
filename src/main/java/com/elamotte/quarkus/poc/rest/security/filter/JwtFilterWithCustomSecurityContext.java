package com.elamotte.quarkus.poc.rest.security.filter;

import com.elamotte.quarkus.poc.rest.annotation.JWTTokenNeeded;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.http.HttpHeaders;

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
import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Provider
@ApplicationScoped
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JwtFilterWithCustomSecurityContext implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;

    JWTClaimsSet claimsSet;

    public static RSAKey rsaJWK = null;

    static {
        try {
            rsaJWK = new RSAKeyGenerator(2048)
                    .keyID("3db3ed6b9574ee3fcd9f149e59ff0eef4f932153")
                    .generate();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String jwtToken = "";

        try {
            jwtToken = requestContext
                    .getHeaderString(HttpHeaders.AUTHORIZATION)
                    .replaceFirst("Bearer ", "");
        } catch (NullPointerException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("#### NO TOKEN!").build());
            return;
        }
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(jwtToken);
        } catch (ParseException e) {
        }

        claimsSet = null;
        try {
            claimsSet = this.validateToken(signedJWT);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("#### invalid token!").build());
            return;
        }

        if (claimsSet != null && claimsSet.getSubject() != null) {
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> claimsSet.getSubject();
                }

                @Override
                public boolean isUserInRole(String role) {
                    Map<String, List<String>> mapUser = new HashMap<>();
                    mapUser.put("user1", List.of("role1", "role2"));
                    mapUser.put("user2", List.of("role3"));
                    // Actually roles are only needed for the back-office
                    // The frontend apis shoud remain in permitall otherwise this will return a 403 for all front end api calls
                    List<String> userRight = mapUser.get(claimsSet.getSubject());

                    return userRight != null && userRight.contains(role);
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

    private JWTClaimsSet validateToken(SignedJWT signedJWT) throws JOSEException, ParseException {
        RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();

        JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);

        signedJWT.verify(verifier);

        return signedJWT.getJWTClaimsSet();
    }
}
