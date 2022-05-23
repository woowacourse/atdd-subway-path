package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createLineResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createPathResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createSectionResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createStationResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("지하철경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private final Station 잠실 = new Station(1L, "잠실");
    private final Station 잠실새내 = new Station(2L, "잠실새내");
    private final Station 종합운동장 = new Station(3L, "종합운동장");
    private final Station 석촌 = new Station(4L, "석촌");
    private final Station 석촌고분 = new Station(5L, "석촌고분");
    private final Station 삼전 = new Station(6L, "삼전");
    private final Station 선릉 = new Station(7L, "선릉");
    private final Station 선정릉 = new Station(8L, "선정릉");
    private final Station 강남구청 = new Station(9L, "강남구청");

    private final LineRequest 이호선 =
            new LineRequest("2호선", "bg-green-600",
                    1L, 3L, 103, 0);
    private final LineRequest 팔호선 =
            new LineRequest("8호선", "bg-red-600",
                    1L, 4L, 10, 0);
    private final LineRequest 구호선 =
            new LineRequest("9호선", "bg-gray-600",
                    4L, 3L, 3, 0);
    private final LineRequest 수인분당선 =
            new LineRequest("수인분당선", "bg-yellow-600",
                    7L, 8L, 7, 500);
    private final LineRequest 수인분당선2 =
            new LineRequest("수인분당선2", "bg-yellow-600",
                    8L, 9L, 7, 700);

    private final SectionRequest 잠실_잠실새내 = new SectionRequest(1L, 2L, 50);
    private final SectionRequest 석촌_석촌고분 = new SectionRequest(4L, 5L, 1);
    private final SectionRequest 석촌고분_삼전 = new SectionRequest(5L, 6L, 1);

    @BeforeEach
    void init() {
        createStationResponse(new StationRequest(잠실.getName()));
        createStationResponse(new StationRequest(잠실새내.getName()));
        createStationResponse(new StationRequest(종합운동장.getName()));
        createStationResponse(new StationRequest(석촌.getName()));
        createStationResponse(new StationRequest(석촌고분.getName()));
        createStationResponse(new StationRequest(삼전.getName()));
        createStationResponse(new StationRequest(선릉.getName()));
        createStationResponse(new StationRequest(선정릉.getName()));
        createStationResponse(new StationRequest(강남구청.getName()));

        createLineResponse(이호선);
        createLineResponse(팔호선);
        createLineResponse(구호선);
        createLineResponse(수인분당선);
        createLineResponse(수인분당선2);

        createSectionResponse(1L, 잠실_잠실새내);
        createSectionResponse(3L, 석촌_석촌고분);
        createSectionResponse(3L, 석촌고분_삼전);
    }

    @Test
    @DisplayName("10km 이하의 최단경로를 찾는다.")
    void findShortestPath10KM() {
        ExtractableResponse<Response> response = createPathResponse(4L, 6L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(석촌, 석촌고분, 삼전), 2, 1250);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("50km 이하의 최단경로를 찾는다.")
    void findShortestPath50KM() {
        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 2050);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("50km 초과의 최단경로를 찾는다.")
    void findShortestPathGreaterThan50KM() {
        ExtractableResponse<Response> response = createPathResponse(2L, 3L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실새내, 종합운동장), 53, 2150);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("어린이인 경우 350원을 제한 금액의 50%를 할인한다.")
    void findShortestPath50KMChild() {
        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 8);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 1200);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("청소년인 경우 350원을 제한 금액의 20%를 할인한다.")
    void findShortestPath50KMTeenager() {
        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 17);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 잠실새내), 50, 1710);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("초과 운임이 있을 경우 그 운임만큼 추가한다.")
    void findShortestPathWithExtraFare() {
        ExtractableResponse<Response> response = createPathResponse(7L, 8L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(선릉, 선정릉), 7, 1750);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }


    @Test
    @DisplayName("초과 운임이 겹치는 경우 가장 높은 초과운임만큼 추가한다.")
    void findShortestPathWithExpensiveExtraFare() {
        ExtractableResponse<Response> response = createPathResponse(7L, 9L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(선릉, 선정릉, 강남구청), 14, 2050);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("여러 노선의 환승을 고려하여 최단경로를 찾는다.")
    void findShortestPathWhenMultiLines() {
        ExtractableResponse<Response> response = createPathResponse(1L, 3L, 20);

        final PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);
        final PathResponse expected = new PathResponse(List.of(잠실, 석촌, 석촌고분, 삼전, 종합운동장), 13, 1350);

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
