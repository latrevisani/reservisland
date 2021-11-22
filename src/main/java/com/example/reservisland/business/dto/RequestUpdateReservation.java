package com.example.reservisland.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"arrivalDate", "departureDate"})
public class RequestUpdateReservation {

    @NotNull(message = "Arrival date is required.")
    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;

    @NotNull(message = "Departure date is required.")
    @JsonProperty("departure_date")
    private LocalDate departureDate;
}
