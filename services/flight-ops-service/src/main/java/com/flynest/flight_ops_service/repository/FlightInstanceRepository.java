package com.flynest.flight_ops_service.repository;

import com.flynest.flight_ops_service.model.FlightInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Long> {
}
