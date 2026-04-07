package com.flynest.location_service.controller;

import com.flynest.location_service.service.CityService;
import com.flynest.payload.request.CityRequest;
import com.flynest.payload.response.ApiResponse;
import com.flynest.payload.response.CityResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CityController {

    private final CityService cityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CityResponse>> createCity(
            @Valid @RequestBody CityRequest cityRequest) {
        log.info("REST request to create city with code: {}", cityRequest.getCityCode());
        CityResponse response = cityService.createCity(cityRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("City created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CityResponse>> updateCity(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest cityRequest) {
        log.info("REST request to update city with id: {}", id);
        CityResponse response = cityService.updateCity(id, cityRequest);
        return ResponseEntity.ok(ApiResponse.success("City updated successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CityResponse>> getCityById(@PathVariable Long id) {
        log.info("REST request to get city by id: {}", id);
        CityResponse response = cityService.getCityById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        log.info("REST request to delete city with id: {}", id);
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CityResponse>>> getAllCities(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        log.info("REST request to get all cities, page: {}", pageable.getPageNumber());
        Page<CityResponse> cities = cityService.getAllCities(pageable);
        return ResponseEntity.ok(ApiResponse.success(cities));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CityResponse>>> searchCities(
            @RequestParam @NotBlank String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("REST request to search cities with keyword: {}", keyword);
        Page<CityResponse> cities = cityService.searchCities(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(cities));
    }

    @GetMapping("/country/{countryCode}")
    public ResponseEntity<ApiResponse<Page<CityResponse>>> getCitiesByCountryCode(
            @PathVariable @Size(min = 2, max = 3) String countryCode,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("REST request to get cities by country code: {}", countryCode);
        Page<CityResponse> cities = cityService.getCitiesByCountryCode(countryCode, pageable);
        return ResponseEntity.ok(ApiResponse.success(cities));
    }

    @GetMapping("/exists/{cityCode}")
    public ResponseEntity<ApiResponse<Boolean>> cityExists(@PathVariable String cityCode) {
        boolean exists = cityService.cityExists(cityCode);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @GetMapping("/validate/{cityCode}")
    public ResponseEntity<ApiResponse<Boolean>> validateCityCode(@PathVariable String cityCode) {
        boolean valid = cityService.validateCityCode(cityCode);
        return ResponseEntity.ok(ApiResponse.success(valid));
    }
}