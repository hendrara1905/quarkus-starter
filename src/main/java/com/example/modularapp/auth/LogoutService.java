package com.example.modularapp.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LogoutService {
    private final Cache<String, Boolean> blacklist;

    @Inject
    public LogoutService(LogoutConfig config) {
        this.blacklist = Caffeine.newBuilder()
                .maximumSize(config.maximumSize())
                .expireAfterWrite(config.durationSession())
                .build();
    }

    public void blacklistToken(String token) {
        blacklist.put(token, true);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.getIfPresent(token) != null;
    }
}
