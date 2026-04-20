package com.flynest.flight_ops_service.repository;

import com.flynest.flight_ops_service.model.FlightInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Long> {

    @Query("""
    SELECT fi FROM FlightInstance fi
    WHERE fi.airlineId = :airlineId
    AND (:departureAirportId IS NULL OR fi.departureAirportId = :departureAirportId)
    AND (:arrivalAirportId   IS NULL OR fi.arrivalAirportId   = :arrivalAirportId)
    AND (:flightId           IS NULL OR fi.flight.id          = :flightId)
    AND (:dayStart           IS NULL OR fi.departureTime      >= :dayStart)
    AND (:dayEnd             IS NULL OR fi.departureTime      <= :dayEnd)
    """)
    Page<FlightInstance> findByAirlineId(
            @Param("airlineId")          Long airlineId,
            @Param("departureAirportId") Long departureAirportId,
            @Param("arrivalAirportId")   Long arrivalAirportId,
            @Param("flightId")           Long flightId,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd")             LocalDateTime dayEnd,
            Pageable pageable
    );
}
