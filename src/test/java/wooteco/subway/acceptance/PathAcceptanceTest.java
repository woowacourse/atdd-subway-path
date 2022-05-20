package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createLineResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createPathResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createSectionResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createStationResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("지하철경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private final StationRequest 잠실 = new StationRequest("잠실");
    private final StationRequest 잠실새내 = new StationRequest("잠실새내");
    private final StationRequest 종합운동장 = new StationRequest("종합운동장");
    private final StationRequest 석촌 = new StationRequest("석촌");
    private final StationRequest 석촌고분 = new StationRequest("석촌고분");
    private final StationRequest 삼전 = new StationRequest("삼전");

    private final LineRequest 이호선 =
            new LineRequest("2호선", "bg-green-600",
                    1L, 3L, 103, 900);
    private final LineRequest 팔호선 =
            new LineRequest("8호선", "bg-red-600",
                    1L, 4L, 10, 900);
    private final LineRequest 구호선 =
            new LineRequest("9호선", "bg-gray-600",
                    4L, 3L, 3, 900);

    private final SectionRequest 잠실_잠실새내 = new SectionRequest(1L, 2L, 50);
    private final SectionRequest 석촌_석촌고분 = new SectionRequest(4L, 5L, 1);
    private final SectionRequest 석촌고분_삼전 = new SectionRequest(5L, 6L, 1);

    @BeforeEach
    void init() {
        createStationResponse(잠실);
        createStationResponse(잠실새내);
        createStationResponse(종합운동장);
        createStationResponse(석촌);
        createStationResponse(석촌고분);
        createStationResponse(삼전);

        createLineResponse(이호선);
        createLineResponse(팔호선);
        createLineResponse(구호선);

        createSectionResponse(1L, 잠실_잠실새내);
        createSectionResponse(3L, 석촌_석촌고분);
        createSectionResponse(3L, 석촌고분_삼전);
    }

    @Test
    @DisplayName("10km 이하의 최단경로를 찾는다.")
    void findShortestPath10KM() {
        ExtractableResponse<Response> response = createPathResponse(4L, 6L, 10);

        checkByValidPath(response, 2, 1250, 석촌.getName(), 석촌고분.getName(), 삼전.getName());
    }

    @Test
    @DisplayName("50km 이하의 최단경로를 찾는다.")
    void findShortestPath50KM() {
        ExtractableResponse<Response> response = createPathResponse(1L, 2L, 10);

        checkByValidPath(response, 50, 2050, 잠실.getName(), 잠실새내.getName());
    }

    @Test
    @DisplayName("50km 초과의 최단경로를 찾는다.")
    void findShortestPathGreaterThan50KM() {
        ExtractableResponse<Response> response = createPathResponse(2L, 3L, 10);

        checkByValidPath(response, 53, 2150, 잠실새내.getName(), 종합운동장.getName());
    }

    @Test
    @DisplayName("여러 노선의 환승을 고려하여 최단경로를 찾는다.")
    void findShortestPathWhenMultiLines() {
        ExtractableResponse<Response> response = createPathResponse(1L, 3L, 10);

        checkByValidPath(response, 13, 1350,
                잠실.getName(), 석촌.getName(), 석촌고분.getName(), 삼전.getName(), 종합운동장.getName());
    }

    private void checkByValidPath(ExtractableResponse<Response> response,
                                  int expectedDistance, int expectedFare, String... expectedStationNames) {
        List<String> stationNames = response.jsonPath().getList("stations", StationResponse.class)
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toUnmodifiableList());
        int distance = response.jsonPath().getInt("distance");
        int fare = response.jsonPath().getInt("fare");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).containsExactly(expectedStationNames),
                () -> assertThat(distance).isEqualTo(expectedDistance),
                () -> assertThat(fare).isEqualTo(expectedFare)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외를 발생시킨다.")
    void sameSourceAndTarget() {
        ExtractableResponse<Response> response = createPathResponse(1L, 1L, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("요청에 해당하는 역이 존재하지 않는 경우 예외를 발생시킨다.")
    void stationNotExistByRequest() {
        ExtractableResponse<Response> response = createPathResponse(1L, 13231L, 10);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
