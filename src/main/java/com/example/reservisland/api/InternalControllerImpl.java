package com.example.reservisland.api;

import com.example.reservisland.business.service.InternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.example.reservisland.config.Constants.DATE_FORMAT;

@RestController
@RequestMapping(path = "/internal-api")
public class InternalControllerImpl implements InternalController {

    @Autowired
    private InternalService internalService;

    @PutMapping(path = "/make-period-available")
    public ResponseEntity<Void> makePeriodAvailable(
            @RequestParam(name = "from-date") @DateTimeFormat(pattern = DATE_FORMAT) LocalDate fromDate,
            @RequestParam(name = "to-date") @DateTimeFormat(pattern = DATE_FORMAT) LocalDate toDate) {
        internalService.makePeriodAvailable(fromDate, toDate);
        return ResponseEntity.noContent().build();
    }
}
