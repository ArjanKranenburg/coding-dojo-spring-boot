package com.assignment.weather.dto.builders;

import com.assignment.weather.dto.Location;

import static java.util.Objects.requireNonNull;

public class LocationBuilder {
    private Integer id;

    private String name;

    private String country;

    static public LocationBuilder locationBuilder() {
        return new LocationBuilder();
    }

    public LocationBuilder withId(Integer id) {
        this.id = requireNonNull(id);
        return this;
    }

    public LocationBuilder withName(String name) {
        this.name = requireNonNull(name);
        return this;
    }

    public LocationBuilder withCountry(String country) {
        this.country = requireNonNull(country);
        return this;
    }

    public Location build() {
        Location location = new Location();
        location.setId(id);
        location.setName(name);
        location.setCountry(country);
        return location;
    }

}
