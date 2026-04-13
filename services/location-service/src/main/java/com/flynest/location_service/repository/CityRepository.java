package com.flynest.location_service.repository;

import com.flynest.location_service.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findByCountryCodeIgnoreCase(String countryCode, Pageable pageable);
    boolean existsByCityCodeIgnoreCase(String cityCode);

    @Query("SELECT c FROM City c WHERE " +
            "LOWER(c.name) LIKE %:keyword% OR " +
            "LOWER(c.cityCode) LIKE %:keyword% OR " +
            "LOWER(c.country) LIKE %:keyword%")
    Page<City> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
