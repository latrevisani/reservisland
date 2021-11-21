package com.example.reservisland.api;

import com.example.reservisland.api.error.DefaultErrorHandler;
import com.example.reservisland.business.service.InternalService;
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

import static com.example.reservisland.config.Constants.DATE_FORMAT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = { InternalControllerImpl.class, DefaultErrorHandler.class })
@EnableAutoConfiguration
class InternalControllerTests {

	public static final String INTERNAL_API_MAKE_PERIOD_AVAILABLE = "/internal-api/make-period-available";

	@Autowired
	MockMvc mockMvc;

	@MockBean
	InternalService internalService;

	@Test
	void shouldMakePeriodAvailable() throws Exception {
		var fromDate = LocalDate.now().plusDays(1);
		var toDate = fromDate.plusMonths(1);
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(put(INTERNAL_API_MAKE_PERIOD_AVAILABLE)
						.queryParam("from-date", dateFormatter.format(fromDate))
						.queryParam("to-date", dateFormatter.format(toDate))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful());

		verify(internalService, only()).makePeriodAvailable(fromDate, toDate);
	}

	@Test
	void shouldThrowWhenDateNotProvided() throws Exception {
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(put(INTERNAL_API_MAKE_PERIOD_AVAILABLE)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		verify(internalService, never()).makePeriodAvailable(any(), any());
	}

	@Test
	void shouldThrowWhenDateHasInvalidFormat() throws Exception {
		var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		mockMvc.perform(put(INTERNAL_API_MAKE_PERIOD_AVAILABLE)
						.queryParam("from-date", "10-12-2020")
						.queryParam("to-date", "10-10-2020")
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		verify(internalService, never()).makePeriodAvailable(any(), any());
	}

}
