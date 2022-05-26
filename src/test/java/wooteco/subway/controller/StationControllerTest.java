package wooteco.subway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.service.StationService;
import wooteco.subway.ui.StationController;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성할 경우 예외를 발생한다.")
    void createStation_throwsExceptionIfStationNameIsDuplicated() throws Exception {
        final StationRequest stationRequest = new StationRequest("강남역");
        final String content = objectMapper.writeValueAsString(stationRequest);

        given(stationService.createStation(any())).willThrow(new DuplicateNameException("이미 존재하는 지하철 역입니다."));

        mockMvc.perform(post("/stations").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpect(content().string("이미 존재하는 지하철 역입니다."));
    }
}
