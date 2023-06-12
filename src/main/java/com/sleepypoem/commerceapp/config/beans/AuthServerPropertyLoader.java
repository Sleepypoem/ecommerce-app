package com.sleepypoem.commerceapp.config.beans;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-server")
@Data
@ToString
public class AuthServerPropertyLoader {

    private String realmName;

    private String clientId;

    private String clientSecret;

    private String prefix;

    private String adminRestPrefix;

    private String tokenEndpoint;

    private String adminUsername;

    private String adminPassword;
}
