package com.assignment.weather.service.openweather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Configuration
public class OpenWeatherConfiguration {

    @Value("${open-weather.uri-base}")
    private String uriBase;

    @Value("${open-weather.uri-string}")
    private String uriString;

    @Value("${open-weather.api-key}")
    private String apiKey;

    @Value("${open-weather.language}")
    private String language;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Bean
    public UriBuilder uriBuilder() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(uriBase);
        return factory.uriString(uriString)
                .queryParam("appid", apiKey)
                .queryParam("mode", "json")
                .queryParam("units", "metric")
                .queryParam("lang", language);
    }
}
