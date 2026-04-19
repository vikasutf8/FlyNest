package com.flynest.flight_ops_service.repository;

import com.flynest.flight_ops_service.model.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {


    /**
     * Find all active schedules for an airline
     */
    List<FlightSchedule> findByAirlineIdAndIsActiveTrue(Long airlineId);

    /**
     * Find schedules for a specific flight
     */
    List<FlightSchedule> findByFlightIdOrderByStartDate(Long flightId);

    /**
     * Find overlapping schedules (same flight, overlapping date ranges)
     */
    @Query("SELECT fs FROM FlightSchedule fs " +
            "WHERE fs.flightId = :flightId " +
            "AND fs.startDate <= :endDate " +
            "AND fs.endDate >= :startDate")
    List<FlightSchedule> findOverlappingSchedules(
            @Param("flightId") Long flightId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Check if schedule exists for date range
     */
    boolean existsByFlightIdAndStartDateAndEndDate(Long flightId, LocalDate startDate, LocalDate endDate);
}
