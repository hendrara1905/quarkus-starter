package com.example.modularapp.config;

import com.example.modularapp.auth.LogoutConfig;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Startup
@Singleton
public class ConfigLogger {

    @Inject
    LogoutConfig config;

    @PostConstruct
    void logConfig() {
        Log.infof("Logout session: %s, cache max size: %d",
                config.durationSession(), config.maximumSize());
    }
}
