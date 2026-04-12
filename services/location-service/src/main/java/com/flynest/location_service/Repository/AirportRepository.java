package com.flynest.location_service.Repository;

// ── AirportRepository ────────────────────────────────────────────────────────

import com.flynest.location_service.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    // Used for duplicate IATA validation
    boolean existsByIataIgnoreCase(String iata);

    // Used for duplicate check on update (skip self)
    boolean existsByIataIgnoreCaseAndIdNot(String iata, Long id);

    // Fetch all airports belonging to a city
    List<Airport> findByCityId(Long cityId);

    // Verify city has any airports at all
    boolean existsByCityId(Long cityId);


}
