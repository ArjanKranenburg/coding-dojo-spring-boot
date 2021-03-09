package com.assignment.weather.service.openweather;

import com.assignment.weather.dto.Location;
import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.service.WeatherService;
import com.assignment.weather.service.openweather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

import static com.assignment.weather.dto.builders.LocationBuilder.locationBuilder;
import static com.assignment.weather.dto.builders.WeatherReportBuilder.weatherReportBuilder;
import static java.util.Objects.requireNonNull;

@Service
public class OpenWeather implements WeatherService {

    private final UriBuilder uriBuilder;

    private final RestTemplate restTemplate;

    @Autowired
    public OpenWeather(UriBuilder uriBuilder, RestTemplate restTemplate) {
        this.uriBuilder = requireNonNull(uriBuilder);
        this.restTemplate = requireNonNull(restTemplate);
    }

    public WeatherReport getWeather(String location) {
        URI uri = uriBuilder
                .queryParam("q", requireNonNull(location))
                .build();
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(uri, WeatherResponse.class);
        return mapper(requireNonNull(response.getBody()));
    }

    private WeatherReport mapper(WeatherResponse response) {
        Location location = locationBuilder()
                .withId(response.getId())
                .withName(response.getName())
                .withCountry(response.getSys().getCountry())
                .build();
        return weatherReportBuilder()
                .withLocation(location)
                .withTemperature(response.getMain().getTemp())
                .withUtcTimestamp(response.getDt())
                .build();
    }
}
