package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("최단 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("최단 경로를 계산해서 보여준다.")
    void showShortestPath() {
        // given
        Long 선릉역_id = httpPost("/stations", new StationRequest("선릉역")).jsonPath().getLong("id");
        Long 종합운동장역_id = httpPost("/stations", new StationRequest("종합운동장역")).jsonPath().getLong("id");
        Long 선정릉역_id = httpPost("/stations", new StationRequest("선정릉역")).jsonPath().getLong("id");
        Long 삼전역_id = httpPost("/stations", new StationRequest("삼전역")).jsonPath().getLong("id");

        httpPost("/lines", new LineRequest("2호선", "초록색", 선릉역_id, 종합운동장역_id, 10));
        httpPost("/lines", new LineRequest("수인분당선", "노란색", 선릉역_id, 선정릉역_id, 15));
        Long 구호선_id = httpPost("/lines", new LineRequest("9호선", "갈색", 종합운동장역_id, 삼전역_id, 5)).jsonPath().getLong("id");

        httpPost("/lines/" + 구호선_id + "/sections", new SectionRequest(선정릉역_id, 종합운동장역_id, 20));

        // when
        ExtractableResponse<Response> response = httpGet(
                "/paths?source=" + 선릉역_id + "&target=" + 삼전역_id + "&age=20");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                        .containsExactly("선릉역", "종합운동장역", "삼전역"),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(15),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350)
        );
    }

    @Test
    @DisplayName("추가 요금이 없는 노선에서 최단 경로와 요금을 구한다.")
    void showNoExtraFareShortestPath() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 역삼역_id = httpPost("/stations", new StationRequest("역삼역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 역삼역_id, 10))
                .jsonPath().getLong("id");

        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(교대역_id, 강남역_id, 5));

        // when
        ExtractableResponse<Response> response = httpGet("/paths?source=" + 교대역_id + "&target=" + 역삼역_id + "&age=20");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                        .containsExactly("교대역", "강남역", "역삼역"),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    @Test
    @DisplayName("추가 요금이 있는 노선에서 최단 경로와 요금을 구한다.")
    void showExtraFareShortestPath() {
        // given
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 양재역_id = httpPost("/stations", new StationRequest("양재역")).jsonPath().getLong("id");

        Long 신분당선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 강남역_id, 양재역_id, 10, 900))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = httpGet("/paths?source=" + 강남역_id + "&target=" + 양재역_id + "&age=20");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("강남역", "양재역"),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }

    @Test
    @DisplayName("추가 요금이 있는 노선과 없는 노선을 환승했을 때 최단 경로와 요금을 구한다.")
    void showTransferLowestExtraShortestPath() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 양재역_id = httpPost("/stations", new StationRequest("양재역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 강남역_id, 5))
                .jsonPath().getLong("id");
        Long 신분당선_id = httpPost("/lines", new LineRequest("신분당선", "빨간색", 강남역_id, 양재역_id, 5, 900))
                .jsonPath().getLong("id");

        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(교대역_id, 강남역_id, 5));
        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(강남역_id, 양재역_id, 5));

        // when
        ExtractableResponse<Response> response = httpGet("/paths?source=" + 교대역_id + "&target=" + 양재역_id + "&age=20");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                        .containsExactly("교대역", "강남역", "양재역"),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }

    @Test
    @DisplayName("청소년이 추가 요금이 있는 노선과 없는 노선을 환승했을 때 최단 경로와 요금을 구한다.")
    void showTransferLowestExtraShortestPath_Teenager() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 양재역_id = httpPost("/stations", new StationRequest("양재역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 강남역_id, 5))
                .jsonPath().getLong("id");
        Long 신분당선_id = httpPost("/lines", new LineRequest("신분당선", "빨간색", 강남역_id, 양재역_id, 5, 900))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = httpGet("/paths?source=" + 교대역_id + "&target=" + 양재역_id + "&age=18");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                        .containsExactly("교대역", "강남역", "양재역"),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo((int)(2150 - (2150 - 350) * 0.2))
        );
    }

    @Test
    @DisplayName("어린이의 추가 요금이 있는 노선과 없는 노선을 환승했을 때 최단 경로와 요금을 구한다.")
    void showTransferLowestExtraShortestPath_Child() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 양재역_id = httpPost("/stations", new StationRequest("양재역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 강남역_id, 5))
                .jsonPath().getLong("id");
        Long 신분당선_id = httpPost("/lines", new LineRequest("신분당선", "빨간색", 강남역_id, 양재역_id, 5, 900))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = httpGet("/paths?source=" + 교대역_id + "&target=" + 양재역_id + "&age=6");

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                        .containsExactly("교대역", "강남역", "양재역"),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo((int)(2150 - (2150 - 350) * 0.5))
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 같을 경우 예외를 반환한다.")
    void sameSourceAndTarget_throws_exception() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 양재역_id = httpPost("/stations", new StationRequest("양재역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 강남역_id, 5))
                .jsonPath().getLong("id");
        Long 신분당선_id = httpPost("/lines", new LineRequest("신분당선", "빨간색", 강남역_id, 양재역_id, 5, 900))
                .jsonPath().getLong("id");

        // when && then
        ExtractableResponse<Response> response = httpGet("/paths?source=" + 교대역_id + "&target=" + 교대역_id + "&age=6");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
