package com.example.reservisland.integration;

import com.example.reservisland.ControllerIntegrationTests;
import com.example.reservisland.business.dto.RequestReservation;
import com.example.reservisland.business.dto.RequestUpdateReservation;
import com.example.reservisland.business.dto.ResponseReservation;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BusinessErrorsScenarios extends ControllerIntegrationTests {

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
    void shouldThrowErrorOnInvalidDates() {
        var specRequest = createRequestBuilder().build();

        var specResponse = createResponseBuilder(HttpStatus.BAD_REQUEST)
                .expectBody(is("From date must be before after date."))
                .build();

        get(String.format(CHECK_AVAILABILITY, LocalDate.now().plusDays(10), LocalDate.now().plusDays(5)), specRequest, specResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidBookingParams")
    void shouldThrowErrorOnInvalidDates(LocalDate from, LocalDate to, String errorMessage) throws Exception {
        makePeriodAvailable();
        bookReservation("test@test.com", "John Doe", LocalDate.now().plusDays(25), LocalDate.now().plusDays(28));

        var request = RequestReservation.builder()
                .email("test@test.com")
                .fullName("John Doe")
                .arrivalDate(from)
                .departureDate(to)
                .build();
        var specRequest = createRequestBuilder(request).build();

        var specResponse = createResponseBuilder(HttpStatus.BAD_REQUEST)
                .expectContentType(ContentType.TEXT)
                .expectBody(is(errorMessage))
                .build();

        post(CREATE_RESERVATION, specRequest, specResponse);
    }

    private static Stream<Arguments> invalidBookingParams() {
        return Stream.of(
                Arguments.of(LocalDate.now().plusDays(-10), LocalDate.now().plusDays(5), "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance."),
                Arguments.of(LocalDate.now().plusDays(40), LocalDate.now().plusDays(43), "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance."),
                Arguments.of(LocalDate.now().plusDays(2), LocalDate.now().plusDays(15), "The campsite can be reserved for max 3 days."),
                Arguments.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), "The campsite is not available in the period."),
                Arguments.of(LocalDate.now().plusDays(26), LocalDate.now().plusDays(29), "The visitor already has a reservation in the given time.")
        );
    }

    private void makePeriodAvailable() {
        var fromDate = LocalDate.now().plusDays(2);
        var toDate = fromDate.plusMonths(1);

        var specRequest = createRequestBuilder().build();

        var specResponse = createResponseBuilder(HttpStatus.NO_CONTENT).build();

        put(String.format(MAKE_PERIOD_AVAILABLE, fromDate, toDate), specRequest, specResponse);
    }

    private String bookReservation(String email, String fullName, LocalDate from, LocalDate to) throws Exception {
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

    @Test
    void shouldNotFindBookingId() {
        var request = RequestUpdateReservation.builder()
                .arrivalDate(LocalDate.now().plusDays(4))
                .departureDate(LocalDate.now().plusDays(7))
                .build();
        var specRequest = createRequestBuilder(request).build();

        var specResponse = createResponseBuilder(HttpStatus.BAD_REQUEST)
                .expectBody(is("Booking ID not found"))
                .build();

        put(String.format(CHANGE_RESERVATION, "invalidBookingId"), specRequest, specResponse);
    }
}
