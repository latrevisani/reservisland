package com.example.reservisland.api;

import com.example.reservisland.business.dto.RequestReservation;
import com.example.reservisland.business.dto.RequestUpdateReservation;
import com.example.reservisland.business.dto.ResponseAvailability;
import com.example.reservisland.business.dto.ResponseReservation;
import com.example.reservisland.business.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

import static com.example.reservisland.config.Constants.DATE_FORMAT;

@RestController
@RequestMapping(path = "/reservation-api")
public class BookingControllerImpl implements BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping(path = "/check-availability")
    public ResponseEntity<ResponseAvailability> checkAvailability(
            @RequestParam(name = "from", required = false)  @DateTimeFormat(pattern = DATE_FORMAT) Optional<LocalDate> from,
            @RequestParam(name = "to", required = false)  @DateTimeFormat(pattern = DATE_FORMAT) Optional<LocalDate> to) {

        var fromDate = from.orElseGet(() -> LocalDate.now().plusDays(1));
        var toDate = to.orElseGet(() -> fromDate.plusMonths(1));

        return ResponseEntity.ok(bookingService.checkAvailability(fromDate, toDate));
    }

    @PostMapping(path = "/reservation")
    public ResponseEntity<ResponseReservation> bookReservation(@Valid @RequestBody RequestReservation requestReservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookReservation(requestReservation));
    }

    @PutMapping(path = "/reservation/{booking-id}")
    public ResponseEntity<ResponseReservation> updateReservation(
            @PathVariable("booking-id") String bookingId,
            @Valid @RequestBody RequestUpdateReservation requestUpdateReservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.updateReservation(bookingId, requestUpdateReservation));
    }

    @DeleteMapping(path = "/reservation/{booking-id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("booking-id") String bookingId) {
        bookingService.cancelReservation(bookingId);
        return ResponseEntity.noContent().build();
    }
}
