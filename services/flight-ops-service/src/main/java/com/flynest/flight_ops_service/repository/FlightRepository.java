package com.flynest.flight_ops_service.repository;

import com.flynest.flight_ops_service.model.Flight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Page<Flight> findByAirlineId(Long airlineId, Pageable pageable);

    boolean existByFlightNumber(String flightNumber);
}
