package wooteco.subway.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.controller.dto.line.LineRequest;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.line.LineRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.ResponseCreator.createGetLineResponseById;
import static wooteco.subway.acceptance.ResponseCreator.createPostLineResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LineControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationService stationService;
    private LineService lineService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        stationService = new StationService(new JdbcStationDao(jdbcTemplate));
        lineService = new LineService(new JdbcLineDao(jdbcTemplate),
                new StationService(new JdbcStationDao(jdbcTemplate)),
                new SectionService(new JdbcSectionDao(jdbcTemplate)));

        stationService.createStation("에덴");
        stationService.createStation("제로");
        stationService.createStation("서초");
    }

    @Test
    @DisplayName("라인 생성의 request의 name은 255자 까지 가능하다.")
    void checkNameLine() {
        //given
        LineRequest 가능한라인 = new LineRequest("c".repeat(255), "color1", 1L, 2L, 10);
        LineRequest 불가능한라인 = new LineRequest("c".repeat(256), "color2", 1L, 2L, 10);
        //when
        ExtractableResponse<Response> 가능한라인응답 = createPostLineResponse(가능한라인);
        ExtractableResponse<Response> 불가능한라인응답 = createPostLineResponse(불가능한라인);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(불가능한라인응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    @Test
    @DisplayName("라인 생성의 request의 color은 20자 까지 가능하다.")
    void checkColorLine() {
        //given
        LineRequest 가능한라인 = new LineRequest("name1", "c".repeat(20), 1L, 2L, 10);
        LineRequest 불가능한라인 = new LineRequest("name2", "c".repeat(21), 1L, 2L, 10);
        //when
        ExtractableResponse<Response> 가능한라인응답 = createPostLineResponse(가능한라인);
        ExtractableResponse<Response> 불가능한라인응답 = createPostLineResponse(불가능한라인);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(불가능한라인응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    @Test
    @DisplayName("라인 생성의 request의 StationId는 양수값이다.")
    void checkPositiveStationId() {
        //given
        LineRequest 가능한라인1 = new LineRequest("name1", "color1", 1L, 2L, 10);
        LineRequest 가능한라인2 = new LineRequest("name2", "color2", 1L, 2L, 10);
        LineRequest 불가능한라인1 = new LineRequest("name3", "color3", 0L, 2L, 10);
        LineRequest 불가능한라인2 = new LineRequest("name4", "color4", 1L, 0L, 10);
        //when
        ExtractableResponse<Response> 가능한라인응답1 = createPostLineResponse(가능한라인1);
        ExtractableResponse<Response> 가능한라인응답2 = createPostLineResponse(가능한라인2);
        ExtractableResponse<Response> 불가능한라인응답1 = createPostLineResponse(불가능한라인1);
        ExtractableResponse<Response> 불가능한라인응답2 = createPostLineResponse(불가능한라인2);
        // then
        assertAll(
                () -> assertThat(가능한라인응답1.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(가능한라인응답2.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(불가능한라인응답1.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                () -> assertThat(불가능한라인응답2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    @Test
    @DisplayName("라인 생성의 request의 distance는 양수값이다.")
    void checkPositiveDistance() {
        //given
        LineRequest 가능한라인 = new LineRequest("name1", "color1", 1L, 2L, 10);
        LineRequest 불가능한라인 = new LineRequest("name2", "color2", 1L, 2L, 0);
        //when
        ExtractableResponse<Response> 가능한라인응답 = createPostLineResponse(가능한라인);
        ExtractableResponse<Response> 불가능한라인응답 = createPostLineResponse(불가능한라인);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(불가능한라인응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    @Test
    @DisplayName("라인 생성의 request의 id는 양수값이다.")
    void checkPositiveId() {
        //given
        lineService.create(new LineRequestDto("name", "color", 1L, 2L, 10));
        //when
        ExtractableResponse<Response> 가능한라인응답 = createGetLineResponseById("1");
        ExtractableResponse<Response> 불가능한라인응답 = createGetLineResponseById("0");
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(불가능한라인응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }
}
