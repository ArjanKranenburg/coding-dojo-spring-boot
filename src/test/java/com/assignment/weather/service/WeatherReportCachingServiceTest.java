package com.assignment.weather.service;

import com.assignment.weather.dto.Location;
import com.assignment.weather.dto.LocationAlias;
import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.persistence.AliasesRepository;
import com.assignment.weather.persistence.LocationsRepository;
import com.assignment.weather.persistence.ReportsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.assignment.weather.dto.builders.LocationAliasBuilder.locationAliasBuilder;
import static com.assignment.weather.dto.builders.LocationBuilder.locationBuilder;
import static com.assignment.weather.dto.builders.WeatherReportBuilder.weatherReportBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class WeatherReportCachingServiceTest {
    final static Location TEST_LOCATION = locationBuilder().withId(1).withName("Gotham City").withCountry("US").build();
    final static LocationAlias TEST_ALIAS = locationAliasBuilder().withAlias("Gotham").withLocation(TEST_LOCATION).build();
    final static WeatherReport TEST_REPORT = weatherReportBuilder().withLocation(TEST_LOCATION)
            .withUtcTimestamp(Instant.now().getEpochSecond()).withTemperature(23.4).build();
    final static WeatherReport TEST_OUTDATED_REPORT = weatherReportBuilder().withLocation(TEST_LOCATION)
            .withUtcTimestamp(Instant.now().getEpochSecond()-1000).withTemperature(21.8).build();

    final static String SEARCH_LOCATION = TEST_ALIAS.getAlias();

    @Mock
    private LocationsRepository locationsRepository;
    @Mock
    private AliasesRepository aliasesRepository;
    @Mock
    private ReportsRepository reportsRepository;
    @Mock
    private WeatherService weatherService;

    private WeatherReportCachingService service;

    @BeforeEach
    public void initialize() {
        service = new WeatherReportCachingService(locationsRepository, aliasesRepository, reportsRepository, weatherService);
    }

    @Test
    public void when_called_with_location_for_first_time_will_return_new_report() {

        // prepare
        when(aliasesRepository.findByAlias(SEARCH_LOCATION)).thenReturn(Optional.empty());
        when(weatherService.getWeather(SEARCH_LOCATION)).thenReturn(TEST_REPORT);

        // act
        WeatherReport actualReport = service.getCurrentWeatherReportForLocation(SEARCH_LOCATION);

        // check
        assertEquals(actualReport, TEST_REPORT);

        verify(locationsRepository).exists(Example.of(TEST_LOCATION));
        verify(locationsRepository).save(TEST_LOCATION);

        AssertThatSavedLocationAliasEquals(TEST_ALIAS);

        verify(reportsRepository).exists(Example.of(TEST_REPORT));
        verify(reportsRepository).save(TEST_REPORT);
    }

    @Test
    public void when_outdated_report_is_stored_and_called_with_known_location_will_return_new_report() {

        // prepare
        when(aliasesRepository.findByAlias(SEARCH_LOCATION)).thenReturn(Optional.of(TEST_ALIAS));
        when(reportsRepository.findAllByLocationId(TEST_LOCATION.getId())).thenReturn(List.of(TEST_OUTDATED_REPORT));
        when(weatherService.getWeather(SEARCH_LOCATION)).thenReturn(TEST_REPORT);

        // act
        WeatherReport actualReport = service.getCurrentWeatherReportForLocation(SEARCH_LOCATION);

        // check
        assertEquals(actualReport, TEST_REPORT);

        verify(reportsRepository).exists(Example.of(TEST_REPORT));
        verify(aliasesRepository, never()).save(any());
        verify(locationsRepository, never()).save(any());

        verify(reportsRepository).save(TEST_REPORT);
    }

    @Test
    public void when_recent_report_is_stored_and_called_with_known_location_will_return_known_report() {

        // prepare
        when(aliasesRepository.findByAlias(SEARCH_LOCATION)).thenReturn(Optional.of(TEST_ALIAS));
        when(reportsRepository.findAllByLocationId(TEST_LOCATION.getId())).thenReturn(List.of(TEST_REPORT));

        // act
        WeatherReport actualReport = service.getCurrentWeatherReportForLocation(SEARCH_LOCATION);

        // check
        assertEquals(actualReport, TEST_REPORT);

        verify(reportsRepository).findAllByLocationId(TEST_LOCATION.getId());

        verify(reportsRepository, never()).save(any());
        verify(weatherService, never()).getWeather(any());
    }

    @Test
    public void when_recent_report_is_stored_and_called_with_unknown_alias_for_known_location_will_return_known_report() {

        // prepare
        when(aliasesRepository.findByAlias(SEARCH_LOCATION)).thenReturn(Optional.empty());
        when(weatherService.getWeather(SEARCH_LOCATION)).thenReturn(TEST_REPORT);
        when(locationsRepository.exists(Example.of(TEST_LOCATION))).thenReturn(true);
        when(reportsRepository.exists(Example.of(TEST_REPORT))).thenReturn(true);

        // act
        WeatherReport actualReport = service.getCurrentWeatherReportForLocation(SEARCH_LOCATION);

        // check
        assertEquals(actualReport, TEST_REPORT);

        AssertThatSavedLocationAliasEquals(TEST_ALIAS);

        verify(reportsRepository, never()).save(any());
        verify(locationsRepository, never()).save(any());
    }

    private void AssertThatSavedLocationAliasEquals(LocationAlias expectedLocationAlias) {
        ArgumentCaptor<LocationAlias> aliasArgumentCaptor = ArgumentCaptor.forClass(LocationAlias.class);
        verify(aliasesRepository).save(aliasArgumentCaptor.capture());
        LocationAlias capturedAliasArgument = aliasArgumentCaptor.getValue();

        assertEquals(capturedAliasArgument.getAlias(), expectedLocationAlias.getAlias());
        assertEquals(capturedAliasArgument.getLocation(), expectedLocationAlias.getLocation());
    }
}
