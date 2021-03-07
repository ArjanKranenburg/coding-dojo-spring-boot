package com.assignment.weather.persistence;

import com.assignment.weather.dto.LocationAlias;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface AliasesRepository extends JpaRepository<LocationAlias, Integer> {
    Optional<LocationAlias> findByAlias(@NotNull final String alias);
}
