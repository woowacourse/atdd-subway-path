package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.controller.dto.path.PathRequest;
import wooteco.subway.controller.dto.section.SectionRequest;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.path.PathRequestDto;
import wooteco.subway.service.dto.path.PathResponse;
import wooteco.subway.service.dto.section.SectionRequestDto;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.acceptance.ResponseCreator.createGetPathResponse;

@WebMvcTest
@AutoConfigureMockMvc
class PathControllerTest {


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
    @DisplayName("경로 생성의 request의 stationId는 양수값이다.")
    void checkPositiveId() throws Exception {
        //given
        PathRequest 가능한라인 = new PathRequest(1L, 2L, 15);
        PathRequest 불가능한라인1 = new PathRequest(0L, 2L, 15);
        PathRequest 불가능한라인2 = new PathRequest(1L, 0L, 15);
        //when
        ResultActions 가능한라인응답 = getPath(가능한라인);
        ResultActions 불가능한라인응답1 = getPath(불가능한라인1);
        ResultActions 불가능한라인응답2 = getPath(불가능한라인2);
        // then
        assertAll(
                () -> 가능한라인응답.andExpect(status().isOk()),
                () -> 불가능한라인응답1.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 출발 ID는 양수입니다.")),
                () -> 불가능한라인응답2.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 도착 ID는 양수입니다."))
        );
    }

    @Test
    @DisplayName("경로 생성의 request의 나이는 양수이다.")
    void checkPositiveDistance() throws Exception {
        //given
        PathRequest 가능한라인 = new PathRequest(1L, 2L, 15);
        PathRequest 불가능한라인 = new PathRequest(1L, 2L, 0);
        //when
        ResultActions 가능한라인응답 = getPath(가능한라인);
        ResultActions 불가능한라인응답 = getPath(불가능한라인);
        // then
        assertAll(
                () -> 가능한라인응답.andExpect(status().isOk()),
                () -> 불가능한라인응답.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 나이는 양수입니다."))
        );
    }

    private ResultActions getPath(PathRequest pathRequest) throws Exception {
        return mockMvc.perform(get(("/paths?source=" + pathRequest.getSource() + "&target=" + pathRequest.getTarget() + "&age=" + pathRequest.getAge())));
    }
}