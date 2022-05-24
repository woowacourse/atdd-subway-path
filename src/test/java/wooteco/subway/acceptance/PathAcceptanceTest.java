package wooteco.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

import static org.hamcrest.Matchers.is;
import static wooteco.subway.acceptance.AcceptanceFixture.insert;
import static wooteco.subway.acceptance.AcceptanceFixture.select;

public class PathAcceptanceTest extends AcceptanceTest {

    @ParameterizedTest
    @CsvSource(value = {"5,4,0,1250", "10,40,0,2050", "10,41,0,2150", "5,4,100,1350", "10,40,50,2100", "10,41,1000,3150"})
    @DisplayName("최단 경로 조회")
    void showPath(int distance1, int distance2, int extraFare, float fare) {
        /*
         * Scenario: 최단 경로 조회
         *   Given 지하철 역들이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   And 지하철 구간이 등록되어 있다.
         *   When 출발 역과 도착역의 최단 경로를 요청한다.
         *   Then 최단 경로의 역과 거리, 요금을 응답받는다.
         * */

        //given
        insert(new StationRequest("교대역"), "/stations", 201);
        insert(new StationRequest("강남역"), "/stations", 201);
        insert(new StationRequest("역삼역"), "/stations", 201);
        insert(new StationRequest("선릉역"), "/stations", 201);

        LineRequest lineRequest = new LineRequest("2호선", "green", 1L, 2L, distance1, extraFare);
        long id = insert(lineRequest, "/lines", 201).extract().jsonPath().getLong("id");

        insert(new SectionRequest(2L, 3L, distance2), "/lines/" + id + "/sections", 201);

        //when & then
        select("/paths?source=1&target=3&age=20", 200)
                .body("stations.size()", is(3))
                .body("distance", is(distance1 + distance2))
                .body("fare", is(fare));
    }
}
