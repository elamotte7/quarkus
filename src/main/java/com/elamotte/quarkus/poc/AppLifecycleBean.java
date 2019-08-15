package com.elamotte.quarkus.poc;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AppLifecycleBean {

    @ConfigProperty(name = "oidc.verify.jwk.issuer")
    String issuer;

    @ConfigProperty(name = "oidc.verify.jwk.set")
    String jwkSet;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

    void onStart(@Observes StartupEvent ev) {

        LOGGER.info("The application is starting...");
        LOGGER.info("issuer : " + this.issuer);
        LOGGER.info("jwkSet : " + this.jwkSet);
        LOGGER.info("OIDC_PROVIDER_METADATA_URL : " + System.getenv("OIDC_PROVIDER_METADATA_URL"));
        LOGGER.info("OIDC_ISSUER : " + System.getenv("OIDC_ISSUER"));

    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}