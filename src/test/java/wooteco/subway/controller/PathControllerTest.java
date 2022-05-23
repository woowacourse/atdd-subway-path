package wooteco.subway.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import wooteco.subway.controller.dto.PathRequest;
import wooteco.subway.controller.dto.PathResponse;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.PathInfo;

class PathControllerTest extends ControllerTest {

	@DisplayName("최단 경로 찾기 요청을 보낸다.")
	@Test
	void findPath() throws Exception {
		// given
		PathInfo info = new PathInfo(List.of(), 20, 1000);
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		given(stationService.findOne(1L))
			.willReturn(station1);
		given(stationService.findOne(2L))
			.willReturn(station2);

		given(pathService.findPath(station1, station2, 20))
			.willReturn(info);

		// when
		mockMvc.perform(get("/paths?source=1&target=2&age=20")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new PathRequest(1L, 2L, 20)))
			)

			//then
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(PathResponse.from(info))));
	}

	@DisplayName("없는 역으로 경로를 찾으면 예외가 발생한다.")
	@Test
	void findPathNotFound() throws Exception {
		// given
		given(stationService.findOne(anyLong()))
			.willThrow(NoSuchElementException.class);

		// when
		mockMvc.perform(get("/paths?source=1&target=2&age=20")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new PathRequest(1L, 2L, 20)))
			)

			//then
			.andExpect(status().isNotFound());
		verify(pathService, never()).findPath(any(Station.class), any(Station.class), anyInt());
	}

	@DisplayName("갈 수 없는 곳으로 경로를 찾으면 예외가 발생한다.")
	@Test
	void findPathBadRequest() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		given(stationService.findOne(1L))
			.willReturn(station1);
		given(stationService.findOne(2L))
			.willReturn(station2);

		given(pathService.findPath(station1, station2, 20))
			.willThrow(IllegalArgumentException.class);

		// when
		mockMvc.perform(get("/paths?source=1&target=2&age=20")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new PathRequest(1L, 2L, 20))
				))

			//then
			.andExpect(status().isBadRequest());
		verify(pathService).findPath(station1, station2, 20);
	}
}
