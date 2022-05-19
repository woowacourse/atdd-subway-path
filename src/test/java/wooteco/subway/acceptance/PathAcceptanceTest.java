package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("최단 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest{

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
        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(15);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350);
    }
}
