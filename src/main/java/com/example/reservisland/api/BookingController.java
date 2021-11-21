package com.example.reservisland.api;

import com.example.reservisland.business.dto.RequestReservation;
import com.example.reservisland.business.dto.RequestUpdateReservation;
import com.example.reservisland.business.dto.ResponseAvailability;
import com.example.reservisland.business.dto.ResponseReservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

@Tag(name = "Reservation Island System", description = "Provides an API to check, book and update reservations in the volcano island.")
public interface BookingController {

    @Operation(description = "Checks availability between two dates provided.")
    ResponseEntity<ResponseAvailability> checkAvailability(
            @Parameter(name = "from", example = "2021-04-20", in = ParameterIn.QUERY,
                    description = "From date. If not provided, it considers tomorrow.")Optional<LocalDate> from,
            @Parameter(name = "to", example = "2021-04-30", in = ParameterIn.QUERY,
                    description = "To date. If not provided, it considers 1 month ahead of the from date.")Optional<LocalDate> to);

    @Operation(description = "Book a reservation during the provided period and visitor.")
    ResponseEntity<ResponseReservation> bookReservation(@Parameter(name = "RequestReservation", required = true,
            description = "Body containing email, full name, and desired period.") RequestReservation requestReservation);

    @Operation(description = "Update the period of a reservation.")
    ResponseEntity<ResponseReservation> updateReservation(
            @Parameter(name = "booking-id", required = true, in = ParameterIn.PATH, description = "Booking id to be updated.") String bookingId,
            @Parameter(name = "RequestReservation", required = true, description = "Body containing desired period.") RequestUpdateReservation requestUpdateReservation);

    @Operation(description = "Cancel a reservation.")
    ResponseEntity<Void> cancelReservation(
            @Parameter(name = "booking-id", required = true, in = ParameterIn.PATH, description = "Booking id to be canceled.") String bookingId);

}
