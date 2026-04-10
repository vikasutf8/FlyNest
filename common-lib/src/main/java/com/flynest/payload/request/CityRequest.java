package com.flynest.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityRequest {

    @NotBlank(message = "City name is required")
    private String name;

    @NotBlank(message = "Country code is required")
    private String countryCode;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City code is required")
    private String cityCode;

    @NotBlank(message = "Region code is required")
    private String regionCode;

    @NotBlank(message = "Time zone offset is required")
    private String timeZoneOffset;


}
