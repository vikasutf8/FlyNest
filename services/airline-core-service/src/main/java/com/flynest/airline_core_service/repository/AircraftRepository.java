package com.flynest.airline_core_service.repository;

import com.flynest.airline_core_service.model.Aircraft;
import com.flynest.airline_core_service.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    // fetch all aircrafts owned by an airline owner
    List<Aircraft> findByAirline_Id(Long airlineId);

    // ownership check
    boolean existsByIdAndAirline_Id(Long id, Long airlineId);

    // duplicate code check
    boolean existsByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);

    Optional<Aircraft> findByCode(String code);

    List<Aircraft> findByAirline(Airline airline);

//    <T> ScopedValue<T> findByCode(@jakarta.validation.constraints.NotBlank(message = "Code is required") @jakarta.validation.constraints.Size(max = 20, message = "Code must be <= 20 characters") String code);
}
