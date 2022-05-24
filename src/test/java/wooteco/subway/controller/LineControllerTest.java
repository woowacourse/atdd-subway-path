package wooteco.subway.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.LineUpdateRequest;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.domain.Station;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;

@WebMvcTest
@AutoConfigureMockMvc
public class LineControllerTest {

    private final String DEFAULT_LINE_URL = "/lines";
    private final Station station1 = new Station(1L, "강남역");
    private final Station station2 = new Station(2L, "역삼역");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @MockBean
    private PathService pathService;

    @MockBean
    private StationService stationService;

    @DisplayName("노선 이름이 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullName() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest(null, "bg-red-600", station1.getId(),
            station2.getId(), 10, 1000);

        // when, then
        mvc.perform(post(DEFAULT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("노선 이름이 비었습니다."));
    }

    @DisplayName("노선 색상이 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullColor() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("2호선", null, station1.getId(),
            station2.getId(), 10, 1000);

        // when, then
        mvc.perform(post(DEFAULT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("노선 색상이 비었습니다."));
    }

    @DisplayName("상행 역 아이디가 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullUpStationId() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", null,
            station2.getId(), 10, 1000);

        // when, then
        mvc.perform(post(DEFAULT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상행 역의 id 가 비었습니다."));
    }

    @DisplayName("하행 역 아이디가 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullDownStationId() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", station1.getId(),
            null, 10, 1000);

        // when, then
        mvc.perform(post(DEFAULT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("하행 역의 id 가 비었습니다."));
    }

    @DisplayName("거리가 1 이하인 생성 요청을 하면 400 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void createLineWithLessThan1(int distance) throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", station1.getId(),
            station2.getId(), distance, 1000);

        // when, then
        mvc.perform(post(DEFAULT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("거리는 1 이상이어야 합니다."));
    }

    @DisplayName("추가 요금이 음수인 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNegative() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", station1.getId(),
            station2.getId(), 10, -1);

        // when, then
        mvc.perform(post(DEFAULT_LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("추가 요금 값은 음수일 수 없습니다."));
    }

    @DisplayName("노선 이름이 빈 수정 요청을 하면 400 에러가 발생한다.")
    @Test
    void updateLineWithNullName() throws Exception {
        // given
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(null, "bg-red-600", 1000);

        // when, then
        mvc.perform(put(DEFAULT_LINE_URL + "/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineUpdateRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("노선 이름이 비었습니다."));
    }

    @DisplayName("노선 색상이 빈 수정 요청을 하면 400 에러가 발생한다.")
    @Test
    void updateLineWithNullColor() throws Exception {
        // given
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("2호선", null, 1000);

        // when, then
        mvc.perform(put(DEFAULT_LINE_URL + "/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineUpdateRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("노선 색상이 비었습니다."));
    }

    @DisplayName("노선 추가요금이 음수인 요청을 하면 400 에러가 발생한다.")
    @Test
    void updateLineWithNullExtraFare() throws Exception {
        // given
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("2호선", "bg-red-600", -1);

        // when, then
        mvc.perform(put(DEFAULT_LINE_URL + "/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(lineUpdateRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("추가 요금 값은 음수일 수 없습니다."));
    }

    @DisplayName("상행 역 아이디가 빈 구간 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createSectionWithNullUpStationId() throws Exception {
        // given
        SectionRequest sectionRequest = new SectionRequest(null, station2.getId(), 10);
        // when, then
        mvc.perform(post(DEFAULT_LINE_URL + "/1/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(sectionRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("상행 역의 id 가 비었습니다."));
    }

    @DisplayName("하행 역 아이디가 빈 구간 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createSectionWithNullDownStationId() throws Exception {
        // given
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), null, 10);
        // when, then
        mvc.perform(post(DEFAULT_LINE_URL + "/1/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(sectionRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("하행 역의 id 가 비었습니다."));
    }

    @DisplayName("구간 거리가 1 미만인 구간 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createSectionWithDistanceLessThan1() throws Exception {
        // given
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 0);
        // when, then
        mvc.perform(post(DEFAULT_LINE_URL + "/1/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(sectionRequest)
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("거리는 1 이상이어야 합니다."));
    }
}
