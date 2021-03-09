package com.assignment.weather.dto.builders;

import com.assignment.weather.dto.Location;
import com.assignment.weather.dto.LocationAlias;

import static java.util.Objects.requireNonNull;

public class LocationAliasBuilder {
    private String alias;

    private Location location;

    static public LocationAliasBuilder locationAliasBuilder() {
        return new LocationAliasBuilder();
    }

    public LocationAliasBuilder withAlias(String alias) {
        this.alias = requireNonNull(alias);
        return this;
    }

    public LocationAliasBuilder withLocation(Location location) {
        this.location = requireNonNull(location);
        return this;
    }

    public LocationAlias build() {
        LocationAlias locationAlias = new LocationAlias();
        locationAlias.setAlias(alias);
        locationAlias.setLocation(location);
        return locationAlias;
    }

}
