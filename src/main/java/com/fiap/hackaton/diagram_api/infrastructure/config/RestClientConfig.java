package com.fiap.hackaton.diagram_api.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class RestClientConfig {

    @Bean
    public RestClient restClient(@Value("${client.report-api.url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}
