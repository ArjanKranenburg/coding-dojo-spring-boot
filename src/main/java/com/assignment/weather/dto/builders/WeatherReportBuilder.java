package com.assignment.weather.dto.builders;

import com.assignment.weather.dto.Location;
import com.assignment.weather.dto.WeatherReport;

import static java.util.Objects.requireNonNull;

public class WeatherReportBuilder {
    private Location location;

    private Double temperature;

    private Integer utcTimestamp;

    static public WeatherReportBuilder weatherReportBuilder() {
        return new WeatherReportBuilder();
    }

    public WeatherReportBuilder withLocation(Location location) {
        this.location = requireNonNull(location);
        return this;
    }

    public WeatherReportBuilder withTemperature(Double temperature) {
        this.temperature = requireNonNull(temperature);
        return this;
    }

    public WeatherReportBuilder withUtcTimestamp(Integer utcTimestamp) {
        this.utcTimestamp = requireNonNull(utcTimestamp);
        return this;
    }

    public WeatherReport build() {
        WeatherReport report = new WeatherReport();
        report.setLocation(location);
        report.setTemperature(temperature);
        report.setUtcTimestamp(utcTimestamp);
        return report;
    }

}
