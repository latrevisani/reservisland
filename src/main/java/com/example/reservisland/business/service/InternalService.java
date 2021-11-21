package com.example.reservisland.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class InternalService {

    @Autowired
    private AvailabilityService availabilityService;

    @Transactional
    public void makePeriodAvailable(LocalDate fromDate, LocalDate toDate) {
        availabilityService.makePeriodAvailable(fromDate, toDate);
    }
}
