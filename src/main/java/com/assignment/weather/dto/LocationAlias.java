package com.assignment.weather.dto;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "aliases")
public class LocationAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String alias;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_LOCATION_ALIASES"))
    private Location location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = requireNonNull(alias);
    }

    public Location getCity() {
        return location;
    }

    public void setCity(Location location) {
        this.location = requireNonNull(location);
    }
}
