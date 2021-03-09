package com.assignment.weather.dto;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    private Integer id;

    private String name;

    private String country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = requireNonNull(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireNonNull(name);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = requireNonNull(country);
    }

    public String toString() {
        return String.format("%s, %s", name, country);
    }
}
