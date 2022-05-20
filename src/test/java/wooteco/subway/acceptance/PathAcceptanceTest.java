package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    private static final String UNREACHABLE_PATH_MESSAGE = "이동 가능한 경로가 존재하지 않습니다";

    /*
     * given
     * 도착역이 같고 구간 길이가 다른 노선 두 개가 등록되어 있다.
     *
     * 등록된 노선
     *  1 - 2 - 3
     *      |   |
     *      4 - 5
     *
     * when
     * 경로를 조회한다.
     *
     * then
     * 총 구간 길이가 더 짧은 경로를 조회한다.
     * */
    @Test
    @DisplayName("출발 역과 도착 역 사이의 경로를 조회한다.")
    void findPath() {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");
        StationResponse station4 = createStation("station4");
        StationResponse station5 = createStation("station5");

        LineResponse line1 = createLine("line1", "color1", station1.getId(),
                station2.getId(), 1);
        addSection(line1, station2, station3, 1);
        addSection(line1, station3, station5, 2);

        LineResponse line2 = createLine("line2", "color2", station2.getId(),
                station4.getId(), 1);
        addSection(line2, station4, station5, 1);

        // when
        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station5.getId())
                .queryParam("age", 18)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        PathResponse expected = new PathResponse(List.of(station1, station2, station4, station5), 3, 1250);
        PathResponse actual = pathResponse.as(PathResponse.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    /*
     * given
     * 서로 만나지 않는 노선 두 개가 등록되어 있다.
     *
     * 등록된 노선
     * 1 - 2 - 3
     * 4 - 5
     *
     * when
     * 이동할 수 없는 경로(1 -> 5)를 조회한다.
     *
     * then
     * 예외 응답을 반환한다.
     * */
    @Test
    @DisplayName("이동할 수 없는 경로를 조회한다.")
    void findUnreachablePath() {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");
        StationResponse station4 = createStation("station4");
        StationResponse station5 = createStation("station5");

        LineResponse line1 = createLine("line1", "color1", station1.getId(),
                station2.getId(), 1);
        addSection(line1, station2, station3, 1);

        LineResponse line2 = createLine("line2", "color2", station4.getId(),
                station5.getId(), 1);

        // when
        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station5.getId())
                .queryParam("age", 18)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(pathResponse.body().jsonPath().getString("message")).isEqualTo(
                        UNREACHABLE_PATH_MESSAGE)
        );
    }


    /*
     * given
     * 노선이 등록되어 있다.
     *
     * when
     * 출발역과 도착역을 같은 역으로 지정하여 경로를 조회한다.
     *
     * then
     * stations는 빈 리스트이고, distance와 fare가 0이다.
     * */
    @Test
    @DisplayName("출발 역과 도착 역을 같은 역으로 경로를 조회한다.")
    void findPathWithSameStation() {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");

        LineResponse line = createLine("line1", "color1", station1.getId(),
                station2.getId(), 1);

        // when
        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station1.getId())
                .queryParam("age", 18)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        PathResponse expected = new PathResponse(Collections.emptyList(), 0, 0);
        PathResponse actual = pathResponse.as(PathResponse.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    /*
     * given
     * 추가 요금이 900원인 노선이 등록되어 있다.
     *
     * when
     * 요금 할인 대상이 아닌 사람이 10키로 미만의 경로를 조회한다.
     *
     * then
     * 추가 요금이 붙은 경로 정보를 응답한다.
     *
     * */
    @DisplayName("추가 요금이 있는 노선의 경로를 조회한다.")
    @Test
    @Disabled
    void getPathInLineWithAdditionalFare() {
        // given
        int extraFare = 900;
        
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");

        LineResponse line = createLine("line1", "color1", station1.getId(),
                station2.getId(), 1, extraFare);

        // when
        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station2.getId())
                .queryParam("age", 25)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        PathResponse expected = new PathResponse(List.of(station1, station2), 1, 2150);
        PathResponse actual = pathResponse.as(PathResponse.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
