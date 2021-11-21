package com.example.reservisland.business.service;

import com.example.reservisland.domain.entity.Availability;
import com.example.reservisland.domain.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public List<Availability> findAvailabilityByDate(LocalDate from, LocalDate to) {
        return availabilityRepository.findByDateBetweenOrderByDate(from, to);
    }

    public void decrementAvailabilityBetweenDates(LocalDate from, LocalDate to) {
        from.datesUntil(to).forEach(date -> {
            var availability = availabilityRepository.findByDate(date);
            availability.setReservations(availability.getReservations() - 1);
            availabilityRepository.save(availability);
        });
    }

    public void incrementAvailabilityBetweenDates(LocalDate from, LocalDate to) {
        from.datesUntil(to).forEach(date -> {
            var availability = availabilityRepository.findByDate(date);
            availability.setReservations(availability.getReservations() + 1);
            availabilityRepository.save(availability);
        });
    }

    public void makePeriodAvailable(LocalDate from, LocalDate to) {
        from.datesUntil(to.plusDays(1)).forEach(date -> {
            var availability = availabilityRepository.findByDate(date);
            if (isNull(availability)) {
                availabilityRepository.save(Availability.builder().date(date).reservations(0).build());
            }
        });
    }
}
