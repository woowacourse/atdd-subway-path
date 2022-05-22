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
import wooteco.subway.controller.dto.section.SectionRequest;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.line.LineRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.ResponseCreator.createPostSectionResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SectionControllerTest {

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
        stationService.createStation("미키");
        lineService.create(new LineRequestDto("2호선", "bg-300-green", 1L, 2L, 10));
    }


    @Test
    @DisplayName("구간 생성의 request의 stationId는 양수값이다.")
    void checkPositiveId() {
        //given
        SectionRequest 가능한구간 = new SectionRequest(2L, 3L, 10);
        SectionRequest 불가능한구간1 = new SectionRequest(0L, 3L, 10);
        SectionRequest 불가능한구간2 = new SectionRequest(2L, 0L, 10);
        //when
        ExtractableResponse<Response> 가능한라인응답 = createPostSectionResponse(1L, 가능한구간);
        ExtractableResponse<Response> 불가능한라인응답1 = createPostSectionResponse(1L, 불가능한구간1);
        ExtractableResponse<Response> 불가능한라인응답2 = createPostSectionResponse(1L, 불가능한구간2);
        // then
        assertAll(
                () -> assertThat(가능한라인응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(불가능한라인응답1.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                () -> assertThat(불가능한라인응답2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}
