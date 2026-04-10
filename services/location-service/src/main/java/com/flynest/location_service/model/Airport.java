package com.flynest.location_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flynest.emabbedable.Address;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true,length = 3)
    private String iata;
    @Column(nullable = false)
    private String name;

    private String timeZoneId;

    @Embedded
    private Address address;

    @Embedded
    private com.flynest.emabbedable.GeoCode geoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}