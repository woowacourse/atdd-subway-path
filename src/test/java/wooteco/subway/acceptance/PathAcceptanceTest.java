package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로를 조회한다.")
    @Test
    void createLines() {
        // given
        createStation(new StationRequest("이대역"));
        createStation(new StationRequest("학동역"));
        createStation(new StationRequest("이수역"));
        createStation(new StationRequest("건대역"));
        createStation(new StationRequest("사가정역"));

        createLine(new LineRequest("분당선", "bg-red-600", 4L, 5L, 3, 900));
        createLine(new LineRequest("2호선", "bg-green-600", 5L, 6L, 3, 900));
        createLine(new LineRequest("3호선", "bg-yellow-600", 7L, 8L, 5, 900));
        createLine(new LineRequest("4호선", "bg-blue-600", 8L, 4L, 7, 900));

        createSection(2L, new SectionRequest(6L, 7L, 4));

        Map<String, Object> params = new HashMap<>();
        params.put("source", 4);
        params.put("target", 7);
        params.put("age", 15);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .params(params)
                .get("/paths")
                .then().log().all()
                .extract();

        List<Station> stations = response.body().jsonPath().getList("stations", StationResponse.class).stream()
                .map(it -> new Station(it.getId(), it.getName()))
                .collect(Collectors.toList());

        assertAll(() -> assertThat(stations).isEqualTo(List.of(new Station(4L, "이대역"), new Station(5L, "학동역")
                , new Station(6L, "이수역"), new Station(7L, "건대역"))));
    }

    private void createSection(Long lineId, SectionRequest sectionRequest) {
        ExtractableResponse<Response> sectionResponse = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private void createLine(LineRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private void createStation(StationRequest stationRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
