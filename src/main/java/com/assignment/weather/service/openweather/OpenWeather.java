package com.assignment.weather.service.openweather;

import com.assignment.weather.dto.Location;
import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.service.WeatherService;
import com.assignment.weather.service.openweather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static com.assignment.weather.dto.builders.LocationBuilder.locationBuilder;
import static com.assignment.weather.dto.builders.WeatherReportBuilder.weatherReportBuilder;
import static java.util.Objects.requireNonNull;

@Service
public class OpenWeather implements WeatherService {

    private final UrlBuilder urlBuilder;

    private final RestTemplate restTemplate;

    @Autowired
    public OpenWeather(UrlBuilder urlBuilder) {
        this.urlBuilder = requireNonNull(urlBuilder);
        this.restTemplate = new RestTemplate();
    }

    public WeatherReport getWeather(String location) {
        String url = urlBuilder.Build(requireNonNull(location));
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
        if (! response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error from OpenWeather: " + response.getStatusCode());
        }

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
