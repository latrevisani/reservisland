package com.example.reservisland.business.service;

import com.example.reservisland.business.exception.ReservationException;
import com.example.reservisland.domain.entity.Reservation;
import com.example.reservisland.domain.entity.Visitor;
import com.example.reservisland.domain.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public boolean visitorHasOverlappingReservation(Visitor visitor, LocalDate from, LocalDate to) {
        var visitorReservations = reservationRepository.findByVisitor(visitor);
        return visitorReservations.stream().anyMatch(reservation ->
                (from.isAfter(reservation.getArrivalDate()) && from.isBefore(reservation.getDepartureDate())) ||
                        (to.isAfter(reservation.getArrivalDate()) && to.isBefore(reservation.getDepartureDate())));
    }

    public Reservation getByBookingId(String bookingId) {
        var reservation = reservationRepository.findByBookingId(bookingId);

        if (reservation.isEmpty()) {
            throw new ReservationException("Booking ID not found");
        }

        return reservation.get();
    }

    public Reservation save(Visitor visitor, LocalDate arrivalDate, LocalDate departureDate) {
        var bookingId = UUID.randomUUID().toString();
        return reservationRepository.save(Reservation.builder()
                .bookingId(bookingId)
                .visitor(visitor)
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .build());
    }

    public Reservation update(Reservation reservation, LocalDate arrivalDate, LocalDate departureDate) {
        reservation.setArrivalDate(arrivalDate);
        reservation.setDepartureDate(departureDate);
        return reservationRepository.save(reservation);
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }
}
