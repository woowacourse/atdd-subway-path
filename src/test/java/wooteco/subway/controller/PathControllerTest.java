package wooteco.subway.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;

@WebMvcTest
@AutoConfigureMockMvc
public class PathControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PathService pathService;

    @MockBean
    private LineService lineService;

    @MockBean
    private StationService stationService;

    @DisplayName("상행 역 Id 가 빈 요청을 하면 400 에러가 발생한다.")
    @Test
    void pathEmptyUpStationId() throws Exception {
        // when, then
        mvc.perform(get("/paths?source=" + "&target=" + 1L + "&age=15"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("출발 역의 id 가 비었습니다."));
    }

    @DisplayName("하행 역 Id 가 빈 요청을 하면 400 에러가 발생한다.")
    @Test
    void pathEmptyDownStationId() throws Exception {
        // when, then
        mvc.perform(get("/paths?source=" + 1L + "&target=" + "&age=15"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("도착 역의 id 가 비었습니다."));
    }

    @DisplayName("나이가 옳지 않은 빈 요청을 하면 400 에러가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {":convert", "-1:연령 값은 음수일 수 없습니다."}, delimiter = ':')
    void pathInvalidAge(String age, String message) throws Exception {
        // when, then
        MvcResult result = mvc.perform(get("/paths?source=" + 1L + "&target=" + 2L + "&age=" + age))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8).contains(message)).isTrue();
    }
}
