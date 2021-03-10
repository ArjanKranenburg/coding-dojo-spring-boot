package com.assignment.weather.service;

import com.assignment.weather.controller.ReportsController;
import com.assignment.weather.dto.Location;
import com.assignment.weather.dto.LocationAlias;
import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.persistence.LocationsRepository;
import com.assignment.weather.persistence.AliasesRepository;
import com.assignment.weather.persistence.ReportsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.assignment.weather.dto.builders.LocationAliasBuilder.locationAliasBuilder;
import static java.util.Objects.requireNonNull;

@Service
public class WeatherReportCachingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherReportCachingService.class);
    private static final int TEN_MINUTES_IN_SECONDS = 10*60;

    private final LocationsRepository locationsRepository;
    private final AliasesRepository aliasesRepository;
    private final ReportsRepository reportsRepository;

    private final WeatherService weatherService;

    @Autowired
    public WeatherReportCachingService(final LocationsRepository cityReposity, final AliasesRepository aliasesRepository,
                                       final ReportsRepository reportsRepository, final WeatherService weatherService) {
        this.locationsRepository = requireNonNull(cityReposity);
        this.aliasesRepository = requireNonNull(aliasesRepository);
        this.reportsRepository = requireNonNull(reportsRepository);
        this.weatherService = requireNonNull(weatherService);
    }

    public WeatherReport getCurrentWeatherReportForLocation(String alias) {
        LOGGER.info("getCurrentWeatherReportForCity( {} )", alias);
        Optional<LocationAlias> locationAlias = aliasesRepository.findByAlias(alias);
        if (locationAlias.isPresent()) {
            List<WeatherReport> reports = reportsRepository.findAllByLocationId(locationAlias.get().getLocation().getId())
                    .stream()
                    .filter(report -> report.getUtcTimestamp()>Instant.now().getEpochSecond()-TEN_MINUTES_IN_SECONDS)
                    .collect(Collectors.toList());
            if (!reports.isEmpty()) {
                if ( reports.size() > 1 ) LOGGER.warn("{} reports stored for the last 10 minutes. Taking the first...", reports.size());
                return reports.get(0);
            }
        }

        WeatherReport report = weatherService.getWeather(alias);

        if ( locationAlias.isEmpty() ) storeNewAlias(alias, report.getLocation());
        saveReport(report);
        return report;
    }

    private void storeNewAlias(String alias, Location location) {
        LOGGER.info("storeNewAlias( {}, {} )", alias, location);
        if ( !locationsRepository.exists(Example.of(location)) ) locationsRepository.save(location);
        LocationAlias newLocationAlias = locationAliasBuilder()
                .withAlias(alias)
                .withLocation(location)
                .build();
        aliasesRepository.save(newLocationAlias);
    }

    private void saveReport(WeatherReport report) {
        LOGGER.info("saveReport( {} )", report);
        if ( !reportsRepository.exists(Example.of(report)) ) reportsRepository.save(report);
    }
}
