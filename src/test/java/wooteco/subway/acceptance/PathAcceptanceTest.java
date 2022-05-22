package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("지하철 경로 관련 기능")
class PathAcceptanceTest extends AcceptanceTest {

    private StationRequest stationRequest1;
    private StationRequest stationRequest2;
    private StationRequest stationRequest3;
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long lineId;

    @BeforeEach
    void setup() {
        stationRequest1 = new StationRequest("강남역");
        stationRequest2 = new StationRequest("역삼역");
        stationRequest3 = new StationRequest("선릉역");
        ExtractableResponse<Response> stationResponse1 = createStation(stationRequest1);
        ExtractableResponse<Response> stationResponse2 = createStation(stationRequest2);
        ExtractableResponse<Response> stationResponse3 = createStation(stationRequest3);

        stationId1 = Long.parseLong(stationResponse1.header("Location").split("/")[2]);
        stationId2 = Long.parseLong(stationResponse2.header("Location").split("/")[2]);
        stationId3 = Long.parseLong(stationResponse3.header("Location").split("/")[2]);

        lineId = createLine(new LineRequest("2호선", "bg-green-600", stationId1, stationId2, 10, 0));
        createSection(new SectionRequest(stationId2, stationId3, 5));
    }

    @DisplayName("최단 경로를 생성한다.")
    @Test
    void createPath() {
        // when
        final ExtractableResponse<Response> response =
                get("/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=20");

        // then
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        final double distance = response.jsonPath().getDouble("distance");
        final int fare = response.jsonPath().getInt("fare");

        assertAll(
                () -> assertThat(stations).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(stationRequest1, stationRequest2, stationRequest3)),
                () -> assertThat(distance).isEqualTo(15),
                () -> assertThat(fare).isEqualTo(1350)
        );
    }

    @DisplayName("잘못된 값으로 경로를 추가할 시 예외를 발생한다.")
    @ParameterizedTest
    @MethodSource("provideForInvalidRequests")
    void createPath_throwsExceptionOnInvalidRequest(final Long source,
                                                    final Long target,
                                                    final int age,
                                                    final String message) {

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", source)
                .param("target", target)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().htmlPath().getString(".")).isEqualTo(message);

    }

    private static Stream<Arguments> provideForInvalidRequests() {
        return Stream.of(
                Arguments.of(null, 2L, 10, "출발역은 공백일 수 없습니다."),
                Arguments.of(1L, null, 10, "도착역은 공백일 수 없습니다."),
                Arguments.of(1L, 2L, 0, "나이는 공백이거나 음수일 수 없습니다.")
        );
    }

    private ExtractableResponse<Response> createStation(final StationRequest stationRequest) {
        return post("/stations", stationRequest);
    }

    private Long createLine(final LineRequest lineRequest) {
        final ExtractableResponse<Response> response = post("/lines", lineRequest);

        return response.jsonPath().getLong("id");
    }

    private void createSection(final SectionRequest sectionRequest) {
        post("/lines/" + lineId + "/sections", sectionRequest);
    }
}

