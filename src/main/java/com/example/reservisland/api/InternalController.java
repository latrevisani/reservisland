package com.example.reservisland.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Tag(name = "Reservation Island Configuration System", description = "Provides an API to configure the booking system.")
public interface InternalController {

    ResponseEntity<Void> makePeriodAvailable(
            @Parameter(name = "from-date", required = true, in = ParameterIn.PATH, description = "Start date from the period.") LocalDate fromDate,
            @Parameter(name = "to-date", required = true, in = ParameterIn.PATH, description = "End date from the period.") LocalDate toDate);
}
