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
import wooteco.subway.controller.dto.path.PathRequest;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.line.LineRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.ResponseCreator.createGetPathResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PathControllerTest {

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
        lineService.create(new LineRequestDto("2호선", "bg-300-green", 1L, 2L, 10));
    }

    @Test
    @DisplayName("경로 생성의 request의 stationId는 양수값이다.")
    void checkPositiveId() {
        //given
        PathRequest 가능한라인 = new PathRequest(1L, 2L, 15);
        PathRequest 불가능한라인1 = new PathRequest(0L, 2L, 15);
        PathRequest 불가능한라인2 = new PathRequest(1L, 0L, 15);
        //when
        ExtractableResponse<Response> 가능한라인응답 = createGetPathResponse(가능한라인);
        ExtractableResponse<Response> 불가능한라인응답1 = createGetPathResponse(불가능한라인1);
        ExtractableResponse<Response> 불가능한라인응답2 = createGetPathResponse(불가능한라인2);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(불가능한라인응답1.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                () -> assertThat(불가능한라인응답2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    @Test
    @DisplayName("경로 생성의 request의 distance는 양수값이다.")
    void checkPositiveDistance() {
        //given
        PathRequest 가능한라인 = new PathRequest(1L, 2L, 15);
        PathRequest 불가능한라인 = new PathRequest(1L, 2L, 0);
        //when
        ExtractableResponse<Response> 가능한라인응답 = createGetPathResponse(가능한라인);
        ExtractableResponse<Response> 불가능한라인응답 = createGetPathResponse(불가능한라인);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(불가능한라인응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}