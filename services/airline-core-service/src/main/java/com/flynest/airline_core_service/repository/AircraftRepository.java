package com.flynest.airline_core_service.repository;

import com.flynest.airline_core_service.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
