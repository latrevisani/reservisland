package com.example.reservisland.integration;

import com.example.reservisland.ControllerIntegrationTests;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;

class CheckAvailabilityScenarios extends ControllerIntegrationTests {

    private static final String CHECK_AVAILABILITY = "/reservation-api/check-availability?from=%s&to=%s";

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Sql(statements = {
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1001, '2021-12-01', 0)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1002, '2021-12-02', 1)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1003, '2021-12-03', 1)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1004, '2021-12-04', 10)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1005, '2021-12-05', 9)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1006, '2021-12-06', 9)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1007, '2021-12-07', 0)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1008, '2021-12-08', 0)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1009, '2021-12-09', 0)",
            "insert into AVAILABILITY(ID_AVAILABILITY, date, reservations) values (1010, '2021-12-10', 0)"
    })
    void shouldCheckAvailability() {
        var fromDate = LocalDate.of(2021, 12, 1);
        var toDate = LocalDate.of(2021, 12, 10);

        var specRequest = createRequestBuilder(new HashMap<>()).build();

        var specResponse = createResponseBuilder(HttpStatus.OK)
                .expectBody("available_dates[0].available_count", is(10))
                .expectBody("available_dates[3].available_count", is(0))
                .expectBody("available_dates[4].available_count", is(1))
                .expectBody("available_dates[9].available_count", is(10))
                .build();

        get(String.format(CHECK_AVAILABILITY, fromDate, toDate), specRequest, specResponse);
    }
}
