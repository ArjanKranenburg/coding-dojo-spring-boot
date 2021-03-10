package com.assignment.weather.controller;

import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.exception.BadRequestException;
import com.assignment.weather.service.WeatherReportCachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/reports")
public class ReportsController {
    private static final Pattern pattern = Pattern.compile("^[A-Za-z0-9, ]++$");

    private final WeatherReportCachingService weatherReportCachingService;

    @Autowired
    public ReportsController(final WeatherReportCachingService weatherReportCachingService) {
        this.weatherReportCachingService = requireNonNull(weatherReportCachingService);
    }

    @GetMapping(params = { "location" })
    @ResponseStatus(HttpStatus.OK)
    public WeatherReport weather(@RequestParam final String location) {
        ValidateInput(location);
        return weatherReportCachingService.getCurrentWeatherReportForLocation(location);
    }

    private void ValidateInput(@Nullable String location) {
        if (location.isEmpty()) throw new BadRequestException("Location must be set");
        if (!pattern.matcher(location).matches()) {
            throw new BadRequestException("Only letters, numbers, commas, and spaces are allowed");
        }

    }
}
