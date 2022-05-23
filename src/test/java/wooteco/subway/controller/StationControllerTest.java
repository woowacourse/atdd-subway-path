package wooteco.subway.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import wooteco.subway.controller.dto.StationRequest;
import wooteco.subway.domain.Station;

class StationControllerTest extends ControllerTest {

	private static final String name = "강남역";

	@DisplayName("역 생성 요청을 받으면 역을 생성한다.")
	@Test
	void createStation() throws Exception{
		// given
		Station result = new Station(1L, name);
		given(stationService.create(name))
			.willReturn(result);
		StationRequest request = new StationRequest(name);

		// when
		mockMvc.perform(
				post("/stations")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON))

			// then
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(result)));
	}

	@DisplayName("역을 생성할 때 예외가 발생한다.")
	@Test
	void stationDuplicatedName() throws Exception{
		// given
		given(stationService.create(name))
			.willThrow(IllegalArgumentException.class);

		// when
		mockMvc.perform(
				post("/stations")
					.content(objectMapper.writeValueAsString(new StationRequest(name)))
					.contentType(MediaType.APPLICATION_JSON))

			// then
			.andExpect(status().isBadRequest());
		verify(stationService).create(name);
	}

	@DisplayName("역 목록을 조회한다.")
	@Test
	void getStations() throws Exception{
		// given
		List<Station> result = List.of(
			new Station(1L, "강남역"),
			new Station(2L, "역삼역")
		);
		given(stationService.findAllStations())
			.willReturn(result);

		// when
		mockMvc.perform(
				get("/stations"))

			// then
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(result)));
		verify(stationService).findAllStations();
	}

	@DisplayName("역을 제거한다.")
	@Test
	void deleteStation() throws Exception {
		// when
		mockMvc.perform(
			delete("/stations/1"))

			// then
			.andExpect(status().isNoContent());
		verify(stationService).remove(1L);
	}

	@DisplayName("역을 제거할 때 예외가 발생한다.")
	@Test
	void deleteStationException() throws Exception {
		// given
		doThrow(new IllegalArgumentException())
			.when(stationService).remove(1L);

		// when
		mockMvc.perform(
				delete("/stations/1"))

			// then
			.andExpect(status().isBadRequest());
		verify(stationService).remove(1L);
	}
}
