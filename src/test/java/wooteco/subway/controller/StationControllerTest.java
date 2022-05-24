package wooteco.subway.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.subway.controller.dto.StationRequest;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;

@WebMvcTest
@AutoConfigureMockMvc
public class StationControllerTest {

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

    @Test
    @DisplayName("이름이 빈 값을 요청하면 400 응답을 던진다.")
    void createStationWithNull() throws Exception {
        // when, then
        mvc.perform(post("/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(new StationRequest())
                ))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("역 이름이 비었습니다."));
    }
}
