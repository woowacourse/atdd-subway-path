package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.controller.dto.section.SectionRequest;
import wooteco.subway.controller.dto.station.StationRequest;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.line.LineRequestDto;
import wooteco.subway.service.dto.section.SectionRequestDto;
import wooteco.subway.service.dto.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.acceptance.ResponseCreator.createPostSectionResponse;

@WebMvcTest
@AutoConfigureMockMvc
class SectionControllerTest {

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
    @DisplayName("구간 생성의 request의 stationId, 거리는 양수값이다.")
    void checkPositiveId() throws Exception {
        //given
        SectionRequest 불가능한구간1 = new SectionRequest(0L, 3L, 10);
        SectionRequest 불가능한구간2 = new SectionRequest(2L, 0L, 10);
        SectionRequest 불가능한구간3 = new SectionRequest(2L, 3L, 0);
        SectionRequest 가능한구간 = new SectionRequest(2L, 3L, 10);
        makeSectionResponse(가능한구간);
        //when
        ResultActions 불가능한구간응답1 = postSections(1L, 불가능한구간1);
        ResultActions 불가능한구간응답2 = postSections(1L, 불가능한구간2);
        ResultActions 불가능한구간응답3 = postSections(1L, 불가능한구간3);
        ResultActions 가능한구간응답 = postSections(1L, 가능한구간);
        // then
        assertAll(
                () -> 가능한구간응답.andExpect(status().isOk()),
                () -> 불가능한구간응답1.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 상행선 ID는 양수입니다.")),
                () -> 불가능한구간응답2.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 하행선 ID는 양수입니다.")),
                () -> 불가능한구간응답3.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 거리는 양수입니다."))
        );
    }

    private void makeSectionResponse(SectionRequest 가능한구간) {
        Mockito.when(sectionService.create(new SectionRequestDto(1L, 가능한구간.getUpStationId(), 가능한구간.getDownStationId(), 가능한구간.getDistance())))
                .thenReturn(1L);
    }

    private ResultActions postSections(Long lineId, SectionRequest sectionRequest) throws Exception {
        return mockMvc.perform(post("/lines/" + lineId + "/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(sectionRequest)
                ));
    }
}
