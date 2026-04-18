package com.flynest.airline_core_service.model;

import com.flynest.emabbedable.Support;
import com.flynest.enums.AirlineStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.resilience.annotation.EnableResilientMethods;

import java.time.LocalDateTime;

// ── Airline Entity ───────────────────────────────────────────────────────────

@Entity
@Table(name = "airlines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    private String iataCode;                    // e.g. "AI", "6E"

    @Column(nullable = false, unique = true, length = 4)
    private String icaoCode;                    // e.g. "AIC", "IGO"

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;

    private String alias;                       // e.g. "Air India", "IndiGo"

    private String logoUrl;

    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AirlineStatus status;

    @Embedded
    private Support support;                   // embedded support contact info

    private String alliances;                   // e.g. "Star Alliance", "OneWorld"

    @Column(name = "headquarters_city_id")
    private Long headquartersCityId;            // FK reference to city — no hard join

    @Column(name = "updated_by_id")
    private Long updatedById;                   // tracks which admin last updated

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}