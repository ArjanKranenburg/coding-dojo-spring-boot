package com.assignment.weather.service;

import com.assignment.weather.dto.WeatherReport;

public interface WeatherService {
    WeatherReport getWeather(String city);
}
