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
import wooteco.subway.controller.dto.line.LineRequest;
import wooteco.subway.controller.dto.section.SectionRequest;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.line.LineRequestDto;
import wooteco.subway.service.dto.line.LineResponse;
import wooteco.subway.service.dto.section.SectionRequestDto;
import wooteco.subway.service.dto.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.acceptance.ResponseCreator.createGetLineResponseById;
import static wooteco.subway.acceptance.ResponseCreator.createPostLineResponse;

@WebMvcTest
@AutoConfigureMockMvc
class LineControllerTest {

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
    @DisplayName("라인 생성의 request의 name은 255자 까지이며 공백이 불가능하다.")
    void checkNameLine() throws Exception {
        //given
        LineRequest 가능한라인 = new LineRequest("c".repeat(255), "color1", 1L, 2L, 10);
        LineRequest 불가능한라인1 = new LineRequest("c".repeat(256), "color2", 1L, 2L, 10);
        LineRequest 불가능한라인2 = new LineRequest(" ", "color2", 1L, 2L, 10);
        makeLineResponse(가능한라인);
        //when
        ResultActions 가능한라인응답 = postLine(가능한라인);
        ResultActions 불가능한라인응답1 = postLine(불가능한라인1);
        ResultActions 불가능한라인응답2 = postLine(불가능한라인2);
        // then
        assertAll(
                () -> 가능한라인응답.andExpect(status().isCreated()),
                () -> 불가능한라인응답1.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 라인 이름은 255자 이하입니다.")),
                () -> 불가능한라인응답2.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 라인 이름은 공백일 수 없습니다."))
        );
    }


    @Test
    @DisplayName("라인 생성의 request의 color은 20자 까지가능하며, 공백은 불가능하다.")
    void checkColorLine() throws Exception {
        //given
        LineRequest 가능한라인 = new LineRequest("name1", "c".repeat(20), 1L, 2L, 10);
        LineRequest 불가능한라인1 = new LineRequest("name2", "c".repeat(21), 1L, 2L, 10);
        LineRequest 불가능한라인2 = new LineRequest("name2", " ", 1L, 2L, 10);
        makeLineResponse(가능한라인);
        //when
        ResultActions 가능한라인응답 = postLine(가능한라인);
        ResultActions 불가능한라인응답1 = postLine(불가능한라인1);
        ResultActions 불가능한라인응답2 = postLine(불가능한라인2);
        // then
        assertAll(
                () -> 가능한라인응답.andExpect(status().isCreated()),
                () -> 불가능한라인응답1.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 라인 색은 20자 이하입니다.")),
                () -> 불가능한라인응답2.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 라인 색은 공백일 수 없습니다."))
        );
    }

    @Test
    @DisplayName("라인 생성의 request의 StationId는 양수값이다.")
    void checkPositiveStationId() throws Exception {
        //given
        LineRequest 가능한라인1 = new LineRequest("name1", "color1", 1L, 2L, 10);
        LineRequest 불가능한라인1 = new LineRequest("name2", "color2", 0L, 2L, 10);
        LineRequest 불가능한라인2 = new LineRequest("name3", "color3", 1L, 0L, 10);
        makeLineResponse(가능한라인1);
        //when
        ResultActions 가능한라인응답1 = postLine(가능한라인1);
        ResultActions 불가능한라인응답1 = postLine(불가능한라인1);
        ResultActions 불가능한라인응답2 = postLine(불가능한라인2);
        // then
        assertAll(
                () -> 가능한라인응답1.andExpect(status().isCreated()),
                () -> 불가능한라인응답1.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 상행선은 양수입니다.")),
                () -> 불가능한라인응답2.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 하행선은 양수입니다."))
        );
    }

    @Test
    @DisplayName("라인 생성의 request의 distance는 양수값이다.")
    void checkPositiveDistance() throws Exception {
        //given
        LineRequest 가능한라인 = new LineRequest("name1", "color1", 1L, 2L, 10);
        LineRequest 불가능한라인 = new LineRequest("name2", "color2", 1L, 2L, 0);
        makeLineResponse(가능한라인);
        //when
        ResultActions 가능한라인응답 = postLine(가능한라인);
        ResultActions 불가능한라인응답 = postLine(불가능한라인);
        // then
        assertAll(
                () -> 가능한라인응답.andExpect(status().isCreated()),
                () -> 불가능한라인응답.andExpect(status().isBadRequest())
                        .andExpect(content().string("[ERROR] 거리는 양수입니다."))
        );
    }

    private void makeLineResponse(LineRequest 가능한구간) {
        Mockito.when(lineService.create(가능한구간.toServiceRequest()))
                .thenReturn(new LineResponse(1L, 가능한구간.getName(), 가능한구간.getColor(), new ArrayList<>()));
    }

    private ResultActions postLine(LineRequest lineRequest) throws Exception {
        return mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(lineRequest)
                ));
    }
}
