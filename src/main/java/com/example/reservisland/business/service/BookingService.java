package com.example.reservisland.business.service;

import com.example.reservisland.business.dto.AvailableDate;
import com.example.reservisland.business.dto.RequestReservation;
import com.example.reservisland.business.dto.RequestUpdateReservation;
import com.example.reservisland.business.dto.ResponseAvailability;
import com.example.reservisland.business.dto.ResponseReservation;
import com.example.reservisland.business.exception.AvailabilityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private VisitorService visitorService;

    @Transactional(readOnly = true)
    public ResponseAvailability checkAvailability(LocalDate from, LocalDate to) {
        validatorService.validateFromAndToDates(from, to);

        var maximumOccupation = configService.getMaximumOccupation();
        var reservationsByDay = availabilityService.findAvailabilityByDate(from, to);

        if (reservationsByDay.isEmpty()) {
            throw new AvailabilityException("The island will not be open during the requested period.");
        }

        var dailyAvailability = reservationsByDay.stream().map(day ->
                AvailableDate.builder().date(day.getDate()).availableCount(maximumOccupation - day.getReservations()).build()).collect(Collectors.toList());

        return ResponseAvailability.builder().availableDates(dailyAvailability).build();
    }

    @Transactional
    public ResponseReservation bookReservation(RequestReservation request) {
        var visitor = visitorService.getOrCreateVisitor(request.getEmail(), request.getFullName());

        validatorService.validateBooking(visitor, request.getArrivalDate(), request.getDepartureDate());
        availabilityService.incrementAvailabilityBetweenDates(request.getArrivalDate(), request.getDepartureDate());

        var reservation = reservationService.save(visitor, request.getArrivalDate(), request.getDepartureDate());

        return ResponseReservation.builder()
                .bookingId(reservation.getBookingId())
                .build();
    }

    @Transactional
    public ResponseReservation updateReservation(String bookingId, RequestUpdateReservation request) {
        var reservation = reservationService.getByBookingId(bookingId);

        availabilityService.decrementAvailabilityBetweenDates(reservation.getArrivalDate(), reservation.getDepartureDate());
        validatorService.validateBooking(reservation.getVisitor(), request.getArrivalDate(), request.getDepartureDate());
        availabilityService.incrementAvailabilityBetweenDates(request.getArrivalDate(), request.getDepartureDate());
        reservationService.update(reservation, request.getArrivalDate(), request.getDepartureDate());

        return ResponseReservation.builder()
                .bookingId(reservation.getBookingId())
                .build();
    }

    @Transactional
    public void cancelReservation(String bookingId) {
        var reservation = reservationService.getByBookingId(bookingId);

        availabilityService.decrementAvailabilityBetweenDates(reservation.getArrivalDate(), reservation.getDepartureDate());
        reservationService.delete(reservation);
    }
}
