package com.sleepypoem.commerceapp.config.payment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "stripe")
@Data
public class StripePropertyLoader {

    private String secretKey;

    private String publicKey;

}
