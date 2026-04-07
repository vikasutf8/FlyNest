package com.flynest.location_service.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoCode {

    private Double latitude;
    private Double longitude;
}
