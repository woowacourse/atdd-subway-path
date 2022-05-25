package wooteco.subway.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.LineResponse;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;

class LineControllerTest extends ControllerTest {

	@DisplayName("노선 생성 요청을 받으면 노선을 생성한다.")
	@Test
	void create() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		given(stationService.findOne(1L))
			.willReturn(station1);
		given(stationService.findOne(2L))
			.willReturn(station2);

		Section section = new Section(station1, station2, 10);
		Line result = new Line(1L, "2호선", "red", 1000, List.of(section));
		given(lineService.create("2호선", "red", section, 1000))
			.willReturn(result);

		// when
		LineRequest request = new LineRequest(
			"2호선", "red", 1L, 2L, 10, 1000);
		mockMvc.perform(post("/lines")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			// then
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(
				LineResponse.from(result)
			)));
		verify(lineService).create("2호선", "red", section, 1000);
	}

	@DisplayName("없는 역으로 노선을 생성하지 못 한다.")
	@Test
	void createLineWithStationNotExist() throws Exception {
		// given
		given(stationService.findOne(any()))
			.willThrow(NoSuchElementException.class);

		// when
		LineRequest request = new LineRequest(
			"2호선", "red", 1L, 2L, 10, 1000);
		mockMvc.perform(post("/lines")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			// then
			.andExpect(status().isNotFound());
		verify(lineService, never())
			.create(eq("2호선"), eq("red"), any(Section.class), eq(1000));
	}

	@DisplayName("존재하는 이름으로 노선을 생성하지 못 한다.")
	@Test
	void createLineWithIllegalArgumentException() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		given(stationService.findOne(1L))
			.willReturn(station1);
		given(stationService.findOne(2L))
			.willReturn(station2);

		Section section = new Section(station1, station2, 10);
		given(lineService.create("2호선", "red", section, 1000))
			.willThrow(IllegalArgumentException.class);

		// when
		LineRequest request = new LineRequest(
			"2호선", "red", 1L, 2L, 10, 1000);
		mockMvc.perform(post("/lines")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			// then
			.andExpect(status().isBadRequest());
		verify(lineService).create("2호선", "red", section, 1000);
	}

	@DisplayName("지하철 노선 조회 요청을 보내면 노선 목록을 입력 받는다.")
	@Test
	void getLines() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		Section section1 = new Section(1L, station1, station2, 10);
		Section section2 = new Section(2L, station2, station1, 10);
		Line line1 = new Line(1L, "2호선", "red", 1000, List.of(section1));
		Line line2 = new Line(2L, "3호선", "red", 1000, List.of(section2));
		given(lineService.listLines())
			.willReturn(List.of(line1, line2));

		// when
		mockMvc.perform(get("/lines"))

			// then
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(
				List.of(LineResponse.from(line1), LineResponse.from(line2))
			)));
		verify(lineService).listLines();
	}

	@DisplayName("하나의 노선 조회를 요청한다.")
	@Test
	void getLine() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		Section section = new Section(1L, station1, station2, 10);
		Line line = new Line(1L, "2호선", "red", 1000, List.of(section));
		given(lineService.findOne(1L))
			.willReturn(line);

		// when
		mockMvc.perform(get("/lines/1"))

			// then
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(LineResponse.from(line))));
		verify(lineService).findOne(1L);
	}

	@DisplayName("없는 노선을 조회하면 예외가 발생한다.")
	@Test
	void getLineNotExist() throws Exception {
		// given
		given(lineService.findOne(1L))
			.willThrow(NoSuchElementException.class);

		// when
		mockMvc.perform(get("/lines/1"))

			// then
			.andExpect(status().isNotFound());
		verify(lineService).findOne(1L);
	}

	@DisplayName("없는 노선을 조회하면 예외가 발생한다.")
	@Test
	void updateLine() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		Section section = new Section(1L, station1, station2, 10);
		Line line = new Line(1L, "2호선", "blue", 1000, List.of(section));
		LineRequest request = new LineRequest(
			"2호선", "blue",
			1L, 2L,
			10, 1000
		);
		given(lineService.update(request.toEntity(1L)))
			.willReturn(line);

		// when
		mockMvc.perform(put("/lines/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))

			// then
			.andExpect(status().isOk());
		verify(lineService).update(request.toEntity(1L));
	}

	@DisplayName("라인 삭제 요청을 보낸다.")
	@Test
	void deleteLine() throws Exception {
		// when
		mockMvc.perform(delete("/lines/1"))
			.andExpect(status().isNoContent());

		// then
		verify(lineService).remove(1L);
	}

	@DisplayName("없는 라인으로 삭제 요청을 보낸다.")
	@Test
	void deleteLineNotExist() throws Exception {
		// given
		doThrow(new NoSuchElementException())
			.when(lineService).remove(1L);

		// when
		mockMvc.perform(delete("/lines/1"))
			.andExpect(status().isNotFound());

		// then
		verify(lineService).remove(1L);
	}

	@DisplayName("구간 생성 요청을 보낸다.")
	@Test
	void createSection() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		given(stationService.findOne(1L))
			.willReturn(station1);
		given(stationService.findOne(2L))
			.willReturn(station2);

		// when
		mockMvc.perform(post("/lines/1/sections")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					new SectionRequest(2L, 3L, 10))
				))

			// then
			.andExpect(status().isOk());
	}

	@DisplayName("구간 을 추가할 수 없으면 예외가 발생한다.")
	@Test
	void createSectionException() throws Exception {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "역삼역");
		given(stationService.findOne(1L))
			.willReturn(station1);
		given(stationService.findOne(2L))
			.willReturn(station2);
		doThrow(new IllegalArgumentException())
			.when(lineService).addSection(1L, new Section(station1, station2, 10));

		// when
		mockMvc.perform(post("/lines/1/sections")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
					new SectionRequest(1L, 2L, 10))
				))

			// then
			.andExpect(status().isBadRequest());
	}

	@DisplayName("구간 삭제 요청을 보낸다.")
	@Test
	void deleteSection() throws Exception {
		// when
		mockMvc.perform(delete("/lines/1/sections?stationId=1"))

			// then
			.andExpect(status().isOk());
		verify(lineService).deleteSection(1L, 1L);
	}

	@DisplayName("삭제할 수 없는 구간일 때 예외가 발생한다.")
	@Test
	void deleteSectionException() throws Exception {
		// given
		doThrow(new IllegalArgumentException())
			.when(lineService).deleteSection(1L, 1L);
		// when
		mockMvc.perform(delete("/lines/1/sections?stationId=1"))

			// then
			.andExpect(status().isBadRequest());
		verify(lineService).deleteSection(1L, 1L);
	}
}
