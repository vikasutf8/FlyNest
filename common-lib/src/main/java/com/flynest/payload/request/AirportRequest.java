package com.flynest.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flynest.emabbedable.Address;
import com.flynest.emabbedable.GeoCode;
import com.flynest.payload.response.CityResponse;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportRequest {

    @NotBlank(message = "IATA code is required")
    @Size(min = 3, max = 3, message = "IATA code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be 3 uppercase letters")
    private String iata;

    @NotBlank(message = "Airport name is required")
    @Size(min = 2, max = 100, message = "Airport name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Timezone is required")
    private String timeZoneId;

    @NotNull(message = "Address is required")
    @Valid                              // cascade validation into Address fields
    private Address address;

    @NotNull(message = "GeoCode is required")
    @Valid                              // cascade validation into GeoCode fields
    private GeoCode geoCode;

    @NotNull(message = "City ID is required")
    @Positive(message = "City ID must be a positive number")
    private Long cityId;
}
