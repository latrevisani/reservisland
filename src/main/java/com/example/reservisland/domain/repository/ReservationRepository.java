package com.example.reservisland.domain.repository;

import com.example.reservisland.domain.entity.Reservation;
import com.example.reservisland.domain.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByVisitor(Visitor visitor);

    Optional<Reservation> findByBookingId(String bookingId);
}
