package com.flynest.payload.response;

import com.flynest.emabbedable.Address;
import com.flynest.emabbedable.GeoCode;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportResponse {
    private Long id;
    private String iata;
    private String name;
    private String timeZoneId;
    private Address address;
    private GeoCode geoCode;
    private CityResponse city;
}
