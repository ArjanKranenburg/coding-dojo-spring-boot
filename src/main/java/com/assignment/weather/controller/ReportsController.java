package com.assignment.weather.controller;

import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.service.WeatherReportCachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final WeatherReportCachingService weatherReportCachingService;

    @Autowired
    public ReportsController(final WeatherReportCachingService weatherReportCachingService) {
        this.weatherReportCachingService = requireNonNull(weatherReportCachingService);
    }

    @GetMapping(params = { "location" })
    @ResponseStatus(HttpStatus.OK)
    public WeatherReport weather(@RequestParam final String location) {
        return weatherReportCachingService.getCurrentWeatherReportForLocation(location);
    }

}
