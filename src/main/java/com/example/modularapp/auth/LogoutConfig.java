package com.example.modularapp.auth;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.time.Duration;

@ConfigMapping(prefix = "app.logout")
public interface LogoutConfig {
    @WithDefault("PT2H")
    Duration durationSession();

    @WithDefault("10000")
    int maximumSize();
}
