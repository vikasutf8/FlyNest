package com.flynest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {
    private String id;
    private String name;
    private String countryCode;
    private String country;
    private String cityCode;
    private String regionCode;
    private String timeZoneOffest;
}
