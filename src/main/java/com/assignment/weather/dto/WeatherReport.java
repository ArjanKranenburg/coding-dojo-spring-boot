package com.assignment.weather.dto;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "reports")
public class WeatherReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_LOCATION_REPORTS"))
    private Location location;

    private Double temperature;

    private Integer utcTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = requireNonNull(id);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = requireNonNull(location);
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = requireNonNull(temperature);
    }

    public Integer getUtcTimestamp() {
        return utcTimestamp;
    }

    public void setUtcTimestamp(Integer utcTimestamp) {
        this.utcTimestamp = utcTimestamp;
    }
}
