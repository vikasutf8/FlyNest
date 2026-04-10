package com.flynest.location_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "cities")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String countryCode;

    private String country;

    @Column(unique = true, nullable = false)
    private String cityCode;

    private String regionCode;

    @Column(name = "time_zone_id")
    private String timeZoneId;

    // Bidirectional
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<Airport> airports;
}