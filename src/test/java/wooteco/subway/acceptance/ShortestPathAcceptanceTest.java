package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("지하철경로 관련 기능")
public class ShortestPathAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("10km 이하의 최단경로를 찾는다.")
    void findShortestPath10KM() {
        //given
        Station 석촌 = new Station(1L, "석촌");
        Station 석촌고분 = new Station(2L, "석촌고분");
        Station 삼전 = new Station(3L, "삼전");

        LineRequest 구호선 = new LineRequest("9호선", "bg-gray-600",
                1L, 2L, 3, 0);

        SectionRequest 석촌고분_삼전 = new SectionRequest(2L, 3L, 1);

        insert(convertRequest(new StationRequest(석촌.getName())), "/stations");
        insert(convertRequest(new StationRequest(석촌고분.getName())), "/stations");
        insert(convertRequest(new StationRequest(삼전.getName())), "/stations");

        insert(convertRequest(구호선), "/lines");

        insert(convertRequest(석촌고분_삼전), "/lines/1/sections");

        ExtractableResponse<Response> response = createPathResponse(1L, 3L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(석촌, 석촌고분, 삼전), 4, 1250);

        assertAll(
                () -> assertThat(actual.getDistance()).isEqualTo(expected.getDistance()),
                () -> assertThat(actual.getStations()).containsAll(expected.getStations()),
                () -> assertThat(actual.getFare()).isEqualTo(expected.getFare())
        );

    }

    @Test
    @DisplayName("50km 이하의 최단경로를 찾는다.")
    void findShortestPath50KM() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");

        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                1L, 2L, 50, 0);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");

        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 2050);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("50km 초과의 최단경로를 찾는다.")
    void findShortestPathGreaterThan50KM() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");

        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                1L, 2L, 53, 0);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");

        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 53, 2150);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("어린이인 경우 350원을 제한 금액의 50%를 할인한다.")
    void findShortestPath50KMChild() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");

        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                1L, 2L, 50, 0);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");

        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 8);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 1200);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("청소년인 경우 350원을 제한 금액의 20%를 할인한다.")
    void findShortestPath50KMTeenager() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");

        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                1L, 2L, 50, 0);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");

        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 15);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 1710);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("초과 운임이 있을 경우 그 운임만큼 추가한다.")
    void findShortestPathWithExtraFare() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");

        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                1L, 2L, 50, 200);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");

        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 2250);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


    @Test
    @DisplayName("초과 운임이 겹치는 경우 가장 높은 초과운임만큼 추가한다.")
    void findShortestPathWithExpensiveExtraFare() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");
        Station 종합운동장 = new Station(3L, "종합운동장");

        LineRequest 일호선 = new LineRequest("1호선", "bg-blue-600",
                1L, 2L, 25, 200);
        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                2L, 3L, 25, 100);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");
        insert(convertRequest(new StationRequest(종합운동장.getName())), "/stations");

        insert(convertRequest(일호선), "/lines");
        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 3L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 50, 2250);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("여러 노선의 환승을 고려하여 최단경로를 찾는다.")
    void findShortestPathWhenMultiLines() {
        Station 잠실 = new Station(1L, "잠실");
        Station 잠실새내 = new Station(2L, "잠실새내");
        Station 종합운동장 = new Station(3L, "종합운동장");

        LineRequest 일호선 = new LineRequest("1호선", "bg-blue-600",
                1L, 2L, 25, 0);
        LineRequest 이호선 = new LineRequest("2호선", "bg-green-600",
                2L, 3L, 25, 0);

        insert(convertRequest(new StationRequest(잠실.getName())), "/stations");
        insert(convertRequest(new StationRequest(잠실새내.getName())), "/stations");
        insert(convertRequest(new StationRequest(종합운동장.getName())), "/stations");

        insert(convertRequest(일호선), "/lines");
        insert(convertRequest(이호선), "/lines");

        ExtractableResponse<Response> response = createPathResponse(1L, 3L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 50, 2050);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외를 발생시킨다.")
    void sameSourceAndTarget() {
        ExtractableResponse<Response> response = createPathResponse(1L, 1L, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("요청에 해당하는 역이 존재하지 않는 경우 예외를 발생시킨다.")
    void stationNotExistByRequest() {
        ExtractableResponse<Response> response = createPathResponse(1L, 13231L, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
