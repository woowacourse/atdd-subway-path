package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.controller.dto.station.StationRequest;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.station.StationResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;
    @MockBean
    private LineService lineService;
    @MockBean
    private PathService pathService;
    @MockBean
    private SectionService sectionService;

    @Test
    @DisplayName("이름 request의 사이즈는 255이하이다.")
    void checkPositiveDistance() throws Exception {
        // given
        StationRequest 가능한역 = new StationRequest("A".repeat(255));
        StationRequest 불가능한역 = new StationRequest("A".repeat(256));
        makeStationResponse(가능한역);
        // when
        ResultActions 가능한역응답 = postStations(가능한역);
        ResultActions 불가능한역응답 = postStations(불가능한역);
        // then
        assertAll(
                () -> 가능한역응답.andExpect(status().isCreated()),
                () -> 불가능한역응답.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 역 이름은 255자 이하입니다."))
        );
    }

    @Test
    @DisplayName("경로 생성의 request의 name은 공백일 수 없다.")
    void checkNotBlank() throws Exception {
        //given
        StationRequest 불가능한역 = new StationRequest(" ");
        //when
        ResultActions 불가능한역응답 = postStations(불가능한역);
        // then
        불가능한역응답.andExpect(status().isBadRequest())
                .andExpect(content().string("[ERROR] 역 이름은 공백일 수 없습니다."));
    }

    private void makeStationResponse(StationRequest 가능한역) {
        Mockito.when(stationService.createStation(가능한역.getName()))
                .thenReturn(new StationResponse(1L, 가능한역.getName()));
    }

    private ResultActions postStations(StationRequest stationRequest) throws Exception {
        return mockMvc.perform(post("/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(stationRequest)
                ));
    }
}

