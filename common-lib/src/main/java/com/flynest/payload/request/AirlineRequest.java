package com.flynest.payload.request;
// ── AirlineRequest ───────────────────────────────────────────────────────────

import com.flynest.enums.AirlineStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirlineRequest {

    @NotBlank(message = "IATA code is required")
    @Size(min = 2, max = 3, message = "IATA code must be 2 or 3 characters")
    @Pattern(regexp = "^[A-Z0-9]{2,3}$", message = "IATA code must be uppercase letters or digits")
    private String iataCode;

    @NotBlank(message = "ICAO code is required")
    @Size(min = 3, max = 4, message = "ICAO code must be 3 or 4 characters")
    @Pattern(regexp = "^[A-Z]{3,4}$", message = "ICAO code must be uppercase letters only")
    private String icaoCode;

    @NotBlank(message = "Airline name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    private String alias;                       // optional

    @Pattern(regexp = "^(https?://).+", message = "Logo URL must be a valid URL")
    private String logoUrl;                     // optional

    @Pattern(regexp = "^(https?://).+", message = "Website must be a valid URL")
    private String website;                     // optional

    @NotNull(message = "Status is required")
    private AirlineStatus status;

    private String alliances;                   // optional

    @NotNull(message = "Headquarters city ID is required")
    @Positive(message = "Headquarters city ID must be a positive number")
    private Long headquartersCityId;

    private String supportEmail;
    private String supportPhone;
    private String supportHours;

}
