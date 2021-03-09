package com.assignment.weather.service.openweather;

import com.assignment.weather.dto.WeatherReport;
import com.assignment.weather.service.openweather.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

import static java.lang.Long.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OpenWeatherTest {
    final static String SEARCH_LOCATION = "Lutjewinkel";

    @Mock
    private UriBuilder uriBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<WeatherResponse> responseEntityMock;

    private OpenWeather service;

    @BeforeEach
    public void initialize() {
        service = new OpenWeather(uriBuilder, restTemplate);

        when(uriBuilder.queryParam(anyString(), ArgumentMatchers.<Class<?>>any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("testUri"));
    }

    @Test
    public void when_called_with_location_will_call_rest_service() {

        WeatherResponse weatherResponse = createTestWeatherResponse();

        // prepare
        when(restTemplate.getForEntity(any(), ArgumentMatchers.<Class<WeatherResponse>>any())).thenReturn(responseEntityMock);
        when(responseEntityMock.getBody()).thenReturn(weatherResponse);

        // act
        WeatherReport actualWeather = service.getWeather(SEARCH_LOCATION);

        // check
        assertEquals(actualWeather.getTemperature(), weatherResponse.getMain().getTemp());
    }

    private WeatherResponse createTestWeatherResponse() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setId(1);
        weatherResponse.setDt(valueOf(235689));
        weatherResponse.setName("Lutjewinkel City");

        Main main = new Main();
        main.setTemp(25.2);
        weatherResponse.setMain(main);

        Sys sys = new Sys();
        sys.setCountry("FRL");
        weatherResponse.setSys(sys);

        return weatherResponse;
    }
}
