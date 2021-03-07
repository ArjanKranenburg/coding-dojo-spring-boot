package com.assignment.weather.service.openweather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlBuilder {
    @Value("${open-weather.url-base}")
    private String urlBase;

    private String mode = "json";

    private String units = "metric";

    @Value("${open-weather.api-key}")
    private String apiKey;

    @Value("${open-weather.language}")
    private String language;


    public String Build(String location)
    {
        return String.format("%s?appid=%s&q=%s&mode=%s&units=%s&lang=%s", urlBase, apiKey, location, mode, units, language);
    }
}
