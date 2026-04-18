package com.flynest.payload.response;

// ── AirlineResponse ──────────────────────────────────────────────────────────

import com.flynest.emabbedable.Support;
import com.flynest.enums.AirlineStatus;
import com.flynest.payload.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirlineResponse {

    private Long            id;
    private String          iataCode;
    private String          icaoCode;
    private String          name;
    private String          alias;
    private String          logoUrl;
    private String          website;
    private AirlineStatus status;
    private String          alliances;

    private CityResponse           headquartersCity;
    private Long            updatedById;
    private Long ownerId;
    private UserDto owner;

    private LocalDateTime createdAt;
    private LocalDateTime   updatedAt;

    private Support support;
}
