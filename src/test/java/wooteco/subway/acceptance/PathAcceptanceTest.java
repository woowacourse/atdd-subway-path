package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
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
public class PathAcceptanceTest extends AcceptanceTest{

    @Test
    @DisplayName("최단 경로를 계산해서 보여준다.")
    void showShortestPathOfBasicFare() {
        // given
        Long 선릉역_id = httpPost("/stations", new StationRequest("선릉역")).jsonPath().getLong("id");
        Long 종합운동장역_id = httpPost("/stations", new StationRequest("종합운동장역")).jsonPath().getLong("id");
        Long 선정릉역_id = httpPost("/stations", new StationRequest("선정릉역")).jsonPath().getLong("id");
        Long 삼전역_id = httpPost("/stations", new StationRequest("삼전역")).jsonPath().getLong("id");

        httpPost("/lines", new LineRequest("2호선", "초록색", 선릉역_id, 종합운동장역_id, 10, 1000));
        httpPost("/lines", new LineRequest("수인분당선", "노란색", 선릉역_id, 선정릉역_id, 15, 1000));
        Long 구호선_id = httpPost("/lines", new LineRequest("9호선", "갈색", 종합운동장역_id, 삼전역_id, 5, 1000)).jsonPath().getLong("id");

        httpPost("/lines/" + 구호선_id + "/sections", new SectionRequest(선정릉역_id, 종합운동장역_id, 20));

        // when
        ExtractableResponse<Response> response = httpGet(
                "/paths?source=" + 선릉역_id + "&target=" + 삼전역_id + "&age="+ 20);

        // then
        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(15);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2350);
    }

    @Test
    @DisplayName("최단 경로를 조회할 때 추가 운임을 받는 노선을 경유할 경우 추가 운임 비용까지 계산하여 조회한다.")
    void showShortestPathOfAdditionalFare() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 역삼역_id = httpPost("/stations", new StationRequest("역삼역")).jsonPath().getLong("id");
        Long 양재역_id = httpPost("/stations", new StationRequest("양재역")).jsonPath().getLong("id");
        Long 양재시민의숲역_id = httpPost("/stations", new StationRequest("양재시민의숲역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 역삼역_id, 15, 1000)).jsonPath().getLong("id");
        Long 신분당선_id = httpPost("/lines", new LineRequest("신분당선", "빨간색", 강남역_id, 양재시민의숲역_id, 20, 1000)).jsonPath().getLong("id");

        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(교대역_id, 강남역_id, 5));

        httpPost("/lines/" + 신분당선_id + "/sections", new SectionRequest(강남역_id, 양재역_id, 5));

        // when
        ExtractableResponse<Response> response = httpGet(
                "/paths?source=" + 교대역_id + "&target=" + 양재시민의숲역_id + "&age=" + 19);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations")).extracting("name")
                        .containsExactly("교대역", "강남역", "양재역", "양재시민의숲역")
        );
    }

    @Test
    @DisplayName("청소년은 총 금액에서 350원을 제외한 20% 할인된 금액을 제공한다.")
    void showTeenagersReceive20PercentDiscount() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 역삼역_id = httpPost("/stations", new StationRequest("역삼역")).jsonPath().getLong("id");


        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 역삼역_id, 15, 1000)).jsonPath().getLong("id");

        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(교대역_id, 강남역_id, 5));

        // when
        ExtractableResponse<Response> response = httpGet(
                "/paths?source=" + 교대역_id + "&target=" + 역삼역_id + "&age=" + 16);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1950);
    }

    @Test
    @DisplayName("어린이는 총 금액에서 350원을 제외한 50% 할인된 금액을 제공한다.")
    void showChildrenReceive20PercentDiscount() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 역삼역_id = httpPost("/stations", new StationRequest("역삼역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 역삼역_id, 15, 1000)).jsonPath().getLong("id");

        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(교대역_id, 강남역_id, 5));

        // when
        ExtractableResponse<Response> response = httpGet(
                "/paths?source=" + 교대역_id + "&target=" + 역삼역_id + "&age=" + 8);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350);
    }

    @Test
    @DisplayName("유아일 경우 운임 요금을 받지 않는다.")
    void showInfantReceive20PercentDiscount() {
        // given
        Long 교대역_id = httpPost("/stations", new StationRequest("교대역")).jsonPath().getLong("id");
        Long 강남역_id = httpPost("/stations", new StationRequest("강남역")).jsonPath().getLong("id");
        Long 역삼역_id = httpPost("/stations", new StationRequest("역삼역")).jsonPath().getLong("id");

        Long 이호선_id = httpPost("/lines", new LineRequest("2호선", "초록색", 교대역_id, 역삼역_id, 15, 1000)).jsonPath().getLong("id");

        httpPost("/lines/" + 이호선_id + "/sections", new SectionRequest(교대역_id, 강남역_id, 5));

        // when
        ExtractableResponse<Response> response = httpGet(
                "/paths?source=" + 교대역_id + "&target=" + 역삼역_id + "&age=" + 4);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(0);
    }
}
