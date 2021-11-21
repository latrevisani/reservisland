package com.example.reservisland.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email", "fullName", "arrivalDate", "departureDate"})
public class RequestReservation {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid e-mail format.")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Full name is required.")
    @JsonProperty("full_name")
    private String fullName;

    @NotNull(message = "Arrival date is required.")
    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;

    @NotNull(message = "Departure date is required.")
    @JsonProperty("departure_date")
    private LocalDate departureDate;
}
