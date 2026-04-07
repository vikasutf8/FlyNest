package com.flynest.location_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airports", indexes = {
        @Index(name = "idx_iata", columnList = "iata", unique = true)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String iata;

    private String name;

    private String timezoneId;

    private String address;

    @Embedded
    private GeoCode geoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}