package com.assignment.weather.persistence;

import com.assignment.weather.dto.WeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ReportsRepository extends JpaRepository<WeatherReport, Integer> {

    List<WeatherReport> findAllByLocationId(Integer locationId);
}
