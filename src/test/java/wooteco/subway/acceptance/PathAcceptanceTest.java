package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.SectionRequest;
import wooteco.subway.service.dto.StationRequest;

public class PathAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> response_신설동;
    ExtractableResponse<Response> response_용두;
    ExtractableResponse<Response> response_신답;
    ExtractableResponse<Response> response_성수;
    ExtractableResponse<Response> response_건대입구;

    ExtractableResponse<Response> 일호선_응답;
    ExtractableResponse<Response> 이호선_응답;
    ExtractableResponse<Response> 삼호선_응답;

    ExtractableResponse<Response> 신설_용두_응답;
    ExtractableResponse<Response> 용두_신답_응답;
    ExtractableResponse<Response> 신답_성수_응답;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        response_신설동 = postWithBody("/stations", new StationRequest("신설동역"));
        response_용두 = postWithBody("/stations", new StationRequest("용두역"));
        response_신답 = postWithBody("/stations", new StationRequest("신답역"));
        response_성수 = postWithBody("/stations", new StationRequest("성수역"));
        response_건대입구 = postWithBody("/stations", new StationRequest("건대입구역"));

        LineRequest 이호선_요청 = new LineRequest("2호선", "green", response_신설동.jsonPath().getLong("id"),
                response_성수.jsonPath().getLong("id"), 42, 0);
        이호선_응답 = postWithBody("/lines", 이호선_요청);

        SectionRequest 신설_용두_요청 = new SectionRequest(response_신설동.jsonPath().getLong("id"),
                response_용두.jsonPath().getLong("id"), 10);
        SectionRequest 용두_신답_요청 = new SectionRequest(response_용두.jsonPath().getLong("id"),
                response_신답.jsonPath().getLong("id"), 20);
        SectionRequest 신답_성수_요청 = new SectionRequest(response_신답.jsonPath().getLong("id"),
                response_성수.jsonPath().getLong("id"), 12);

        신설_용두_응답 = postWithBody("/lines/" + 이호선_응답.jsonPath().getLong("id") + "/sections", 신설_용두_요청);
        용두_신답_응답 = postWithBody("/lines/" + 이호선_응답.jsonPath().getLong("id") + "/sections", 용두_신답_요청);
        신답_성수_응답 = postWithBody("/lines/" + 이호선_응답.jsonPath().getLong("id") + "/sections", 신답_성수_요청);

        LineRequest 일호선_요청 = new LineRequest("1호선", "red", response_용두.jsonPath().getLong("id"),
                response_성수.jsonPath().getLong("id"), 17, 800);
        LineRequest 삼호선_요청 = new LineRequest("3호선", "orange", response_성수.jsonPath().getLong("id"),
                response_건대입구.jsonPath().getLong("id"), 10, 900);

        일호선_응답 = postWithBody("/lines", 일호선_요청);
        삼호선_응답 = postWithBody("/lines", 삼호선_요청);
    }

    @ParameterizedTest
    @CsvSource(value = {"21,1650", "13,1040", "6,650", "3,0"})
    @DisplayName("선택한 출발지와 목적지에 해당하는 경로를 조회한다.")
    void getPath(int age, int fare) {
        // given
        String url = "/paths?source="
                + response_신설동.jsonPath().getLong("id") + "&target="
                + response_신답.jsonPath().getLong("id") + "&age=" + age;
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(3);
            assertThat(pathResponse.getDistance()).isEqualTo(30);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,2450", "13,1680", "6,1050", "3,0"})
    @DisplayName("추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.")
    void getPath_WhenIncludeExtraFareLine(int age, int fare) {
        // given
        String url = "/paths?source="
                + response_신설동.jsonPath().getLong("id") + "&target="
                + response_성수.jsonPath().getLong("id") + "&age=" + age;
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(3);
            assertThat(pathResponse.getDistance()).isEqualTo(27);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,2750", "13,1920", "6,1200", "3,0"})
    @DisplayName("추가 요금이 있는 노선을 여러 개 이용 할 경우 가장 높은 추가 금액만 적용하여 측정된 요금에 추가한다.")
    void getPath_WhenIncludeSomeExtraFareLine(int age, int fare) {
        // given
        String url = "/paths?source="
                + response_신설동.jsonPath().getLong("id") + "&target="
                + response_건대입구.jsonPath().getLong("id") + "&age=" + age;
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(4);
            assertThat(pathResponse.getDistance()).isEqualTo(37);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }
}
