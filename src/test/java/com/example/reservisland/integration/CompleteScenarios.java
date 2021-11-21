package com.example.reservisland.integration;

import com.example.reservisland.ControllerIntegrationTests;
import com.example.reservisland.business.dto.RequestReservation;
import com.example.reservisland.business.dto.RequestUpdateReservation;
import com.example.reservisland.business.dto.ResponseReservation;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class CompleteScenarios extends ControllerIntegrationTests {

    private static final String MAKE_PERIOD_AVAILABLE = "/internal-api/make-period-available?from-date=%s&to-date=%s";
    private static final String CHECK_AVAILABILITY = "/reservation-api/check-availability?from=%s&to=%s";
    private static final String CREATE_RESERVATION = "/reservation-api/reservation";
    private static final String CHANGE_RESERVATION = "/reservation-api/reservation/%s";

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldRunCompleteScenario() throws Exception {
        shouldMakePeriodAvailable();
        shouldCheckAvailability(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 30), 10);
        var bookingId = shouldBookReservation("test@test.com", "John Doe", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 4));
        shouldCheckAvailability(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 4), 9);
        shouldUpdateReservation(bookingId, LocalDate.of(2021, 12, 10), LocalDate.of(2021, 12, 13));
        shouldCheckAvailability(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 4), 10);
        shouldCheckAvailability(LocalDate.of(2021, 12, 10), LocalDate.of(2021, 12, 13), 9);
        shouldCancelReservation(bookingId);
        shouldCheckAvailability(LocalDate.of(2021, 12, 10), LocalDate.of(2021, 12, 13), 10);
    }

    void shouldMakePeriodAvailable() {
        var fromDate = LocalDate.of(2021, 11, 1);
        var toDate = LocalDate.of(2022, 12, 31);

        var specRequest = createRequestBuilder().build();

        var specResponse = createResponseBuilder(HttpStatus.NO_CONTENT).build();

        put(String.format(MAKE_PERIOD_AVAILABLE, fromDate, toDate), specRequest, specResponse);
    }

    void shouldCheckAvailability(LocalDate from, LocalDate to, int valueExpected) {
        var specRequest = createRequestBuilder().build();

        var specResponse = createResponseBuilder(HttpStatus.OK)
                .expectBody("available_dates[0].available_count", is(valueExpected))
                .expectBody("available_dates[1].available_count", is(valueExpected))
                .expectBody("available_dates[2].available_count", is(valueExpected))
                .build();

        get(String.format(CHECK_AVAILABILITY, from, to), specRequest, specResponse);
    }

    String shouldBookReservation(String email, String fullName, LocalDate from, LocalDate to) throws Exception {
        var request = RequestReservation.builder()
                .email(email)
                .fullName(fullName)
                .arrivalDate(from)
                .departureDate(to)
                .build();
        var specRequest = createRequestBuilder(request).build();

        var specResponse = createResponseBuilder(HttpStatus.CREATED)
                .expectBody("booking_id", notNullValue())
                .build();

        var response = post(CREATE_RESERVATION, specRequest, specResponse);
        ResponseReservation reservation = objectMapper.readValue(response, ResponseReservation.class);

        return reservation.getBookingId();
    }

    void shouldUpdateReservation(String bookingId, LocalDate from, LocalDate to) {
        var request = RequestUpdateReservation.builder()
                .arrivalDate(from)
                .departureDate(to)
                .build();
        var specRequest = createRequestBuilder(request).build();

        var specResponse = createResponseBuilder(HttpStatus.CREATED)
                .expectBody("booking_id", is(bookingId))
                .build();

        put(String.format(CHANGE_RESERVATION, bookingId), specRequest, specResponse);
    }

    void shouldCancelReservation(String bookingId) {
        var specRequest = createRequestBuilder().build();

        var specResponse = createResponseBuilder(HttpStatus.NO_CONTENT).build();

        delete(String.format(CHANGE_RESERVATION, bookingId), specRequest, specResponse);
    }
}
