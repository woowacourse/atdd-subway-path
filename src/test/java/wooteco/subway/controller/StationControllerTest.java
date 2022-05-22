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
import wooteco.subway.controller.dto.station.StationRequest;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.line.LineRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.ResponseCreator.createPostStationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StationControllerTest {

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
    @DisplayName("경로 생성의 request의 name은 사이즈 255이하이다.")
    void checkPositiveDistance() {
        //given
        StationRequest 가능한역 = new StationRequest("A".repeat(255));
        StationRequest 불가능한역 = new StationRequest("A".repeat(256));
        //when
        ExtractableResponse<Response> 가능한라인응답 = createPostStationResponse(가능한역);
        ExtractableResponse<Response> 불가능한라인응답 = createPostStationResponse(불가능한역);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(불가능한라인응답.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}
