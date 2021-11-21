package com.example.reservisland.domain.repository;

import com.example.reservisland.domain.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByDateBetweenOrderByDate(LocalDate from, LocalDate to);

    Availability findByDate(LocalDate date);
}
