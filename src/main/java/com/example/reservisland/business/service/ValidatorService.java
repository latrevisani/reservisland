package com.example.reservisland.business.service;

import com.example.reservisland.business.exception.InvalidBookingException;
import com.example.reservisland.business.exception.InvalidDateRangeException;
import com.example.reservisland.domain.entity.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class ValidatorService {

    @Autowired
    private ConfigService configService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private ReservationService reservationService;

    public void validateFromAndToDates(LocalDate from, LocalDate to) {
        if (Objects.isNull(from)) {
            throw new InvalidDateRangeException("From date must be provided.");
        }

        if (Objects.isNull(to)) {
            throw new InvalidDateRangeException("To date must be provided.");
        }

        if (!from.isBefore(to)) {
            throw new InvalidDateRangeException("From date must be before after date.");
        }
    }

    public void validateBooking(Visitor visitor, LocalDate from, LocalDate to) {
        validateFromAndToDates(from, to);

        if (!from.isAfter(LocalDate.now()) || from.isAfter(LocalDate.now().plusMonths(1))) {
            throw new InvalidBookingException("The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.");
        }

        if (ChronoUnit.DAYS.between(from, to) > 3L) {
            throw new InvalidBookingException("The campsite can be reserved for max 3 days.");
        }

        if (reservationService.visitorHasOverlappingReservation(visitor, from, to)) {
            throw new InvalidBookingException("The visitor already has a reservation in the given time.");
        }

        var maximumOccupation = configService.getMaximumOccupation();
        var availability = availabilityService.findAvailabilityByDate(from, to);

        if (availability.stream().anyMatch(available -> available.getReservations() >= maximumOccupation)) {
            throw new InvalidBookingException("The campsite is fully occupied in the period.");
        }
    }

}
