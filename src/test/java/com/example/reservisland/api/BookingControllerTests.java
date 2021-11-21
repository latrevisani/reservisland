package com.example.reservisland.api;

import com.example.reservisland.api.error.DefaultErrorHandler;
import com.example.reservisland.business.dto.RequestReservation;
import com.example.reservisland.business.dto.RequestUpdateReservation;
import com.example.reservisland.business.dto.ResponseAvailability;
import com.example.reservisland.business.dto.ResponseReservation;
import com.example.reservisland.business.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.example.reservisland.config.Constants.DATE_FORMAT;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = { BookingControllerImpl.class, DefaultErrorHandler.class })
@EnableAutoConfiguration
class BookingControllerTests {

	private static final String BASE = "/reservation-api";
	private static final String CHECK_AVAILABILITY = BASE + "/check-availability";
	private static final String CREATE_RESERVATION = BASE + "/reservation";
	private static final String CHANGE_RESERVATION = BASE + "/reservation/{booking-id}";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	BookingService bookingService;

	@Test
	void shouldCheckAvailabilityPassingPeriod() throws Exception {
		var fromDate = LocalDate.now().plusMonths(1);
		var toDate = fromDate.plusMonths(2);
		given(bookingService.checkAvailability(fromDate, toDate)).willReturn(ResponseAvailability.builder().build());
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(get(CHECK_AVAILABILITY)
						.queryParam("from", dateFormatter.format(fromDate))
						.queryParam("to", dateFormatter.format(toDate))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful());

		verify(bookingService, only()).checkAvailability(fromDate, toDate);
	}

	@Test
	void shouldCheckAvailabilityPassingFromDate() throws Exception {
		var fromDate = LocalDate.now().plusMonths(1);
		var toDate = fromDate.plusMonths(1);
		given(bookingService.checkAvailability(fromDate, toDate)).willReturn(ResponseAvailability.builder().build());
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(get(CHECK_AVAILABILITY)
						.queryParam("from", dateFormatter.format(fromDate))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful());

		verify(bookingService, only()).checkAvailability(fromDate, toDate);
	}

	@Test
	void shouldCheckAvailabilityPassingToDate() throws Exception {
		var fromDate = LocalDate.now().plusDays(1);
		var toDate = fromDate.plusDays(10);
		given(bookingService.checkAvailability(fromDate, toDate)).willReturn(ResponseAvailability.builder().build());
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(get(CHECK_AVAILABILITY)
						.queryParam("to", dateFormatter.format(toDate))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful());

		verify(bookingService, only()).checkAvailability(fromDate, toDate);
	}

	@Test
	void shouldThrowWhenDateHasInvalidFormat() throws Exception {
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(get(CHECK_AVAILABILITY)
						.queryParam("from", "10-12-2020")
						.queryParam("to", "10-10-2020")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		verify(bookingService, never()).checkAvailability(any(), any());
	}

	@Test
	void shouldBookReservation() throws Exception {
		var email = "test@test.com";
		var fullName = "Full Name";
		var fromDate = LocalDate.now().plusDays(5);
		var toDate = fromDate.plusDays(3);
		var request = RequestReservation.builder()
				.email(email)
				.fullName(fullName)
				.arrivalDate(fromDate)
				.departureDate(toDate)
				.build();
		var bookingId = UUID.randomUUID().toString();
		var response = ResponseReservation.builder().bookingId(bookingId).build();
		given(bookingService.bookReservation(request)).willReturn(response);

		mockMvc.perform(post(CREATE_RESERVATION)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.booking_id", is(bookingId)));

		verify(bookingService, only()).bookReservation(request);
	}

	@Test
	void shouldThrowWithMissingAttributes() throws Exception {
		var request = RequestReservation.builder().build();
		var bookingId = UUID.randomUUID().toString();
		var response = ResponseReservation.builder().bookingId(bookingId).build();

		mockMvc.perform(post(CREATE_RESERVATION)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		verify(bookingService, never()).bookReservation(request);
	}

	@Test
	void shouldUpdateReservation() throws Exception {
		var bookingId = UUID.randomUUID().toString();
		var fromDate = LocalDate.now().plusDays(5);
		var toDate = fromDate.plusDays(3);
		var request = RequestUpdateReservation.builder()
				.arrivalDate(fromDate)
				.departureDate(toDate)
				.build();
		var response = ResponseReservation.builder().bookingId(bookingId).build();
		given(bookingService.updateReservation(bookingId, request)).willReturn(response);

		mockMvc.perform(put(CHANGE_RESERVATION, bookingId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.booking_id", is(bookingId)));

		verify(bookingService, only()).updateReservation(bookingId, request);
	}

	@Test
	void shouldNotUpdateReservationDueMissintAttributes() throws Exception {
		var bookingId = UUID.randomUUID().toString();
		var request = RequestUpdateReservation.builder().build();

		mockMvc.perform(put(CHANGE_RESERVATION, bookingId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		verify(bookingService, never()).updateReservation(bookingId, request);
	}

	@Test
	void shouldCancelReservation() throws Exception {
		var bookingId = UUID.randomUUID().toString();

		mockMvc.perform(delete(CHANGE_RESERVATION, bookingId)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful());

		verify(bookingService, only()).cancelReservation(bookingId);
	}

}
