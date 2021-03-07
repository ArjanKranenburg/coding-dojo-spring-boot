package com.assignment.weather.persistence;

import com.assignment.weather.dto.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface LocationsRepository extends JpaRepository<Location, Integer> {
}
