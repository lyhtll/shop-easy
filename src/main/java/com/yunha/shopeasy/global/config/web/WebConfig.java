package com.yunha.shopeasy.global.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebConfig {

    @Value("${toss.payments.secret-key}")
    private String tossSecretKey;

    @Bean
    public RestClient tossPaymentsRestClient() {
        String encodedKey = Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        return RestClient.builder()
                .baseUrl("https://api.tosspayments.com/v1")
                .defaultHeader("Authorization", "Basic " + encodedKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
