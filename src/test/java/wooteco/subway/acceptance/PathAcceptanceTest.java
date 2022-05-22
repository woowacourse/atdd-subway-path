package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("역이 없는 경우에 404 에러를 발생시킨다.")
    @Test
    void searchPathByNotFoundStation() {
        ExtractableResponse<Response> response = requestSearchPath(1L, 2L, 21);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("source와 target이 같은 경우 400에러를 발생시킨다.")
    @Test
    void searchPathBySourceSameAsTarget() {
        long upStationId = requestCreateStation("강남역").jsonPath().getLong("id");
        long downStationId = requestCreateStation("역삼역").jsonPath().getLong("id");
        requestCreateLine("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);

        ExtractableResponse<Response> response = requestSearchPath(upStationId, upStationId, 21);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isNotBlank();
    }

    @DisplayName("지하철 경로 탐색")
    @Test
    void searchPath() {
        long 강남역_id = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역_id = requestCreateStation("역삼역").jsonPath().getLong("id");
        long 잠실역_id = requestCreateStation("잠실역").jsonPath().getLong("id");
        long 선릉역_id = requestCreateStation("선릉역").jsonPath().getLong("id");

        long 신분당선_id = requestCreateLine("신분당선", "bg-red-600", 강남역_id, 역삼역_id, 10, 0).jsonPath()
            .getLong("id");
        long 일호선_id = requestCreateLine("1호선", "bg-blue-600", 강남역_id, 선릉역_id, 10, 0).jsonPath()
            .getLong("id");
        requestAddSection(신분당선_id, 역삼역_id, 잠실역_id, 10);
        requestAddSection(일호선_id, 잠실역_id, 강남역_id, 5);

        ExtractableResponse<Response> response = requestSearchPath(선릉역_id, 역삼역_id, 21);

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int fare = response.jsonPath().getObject("fare", Integer.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(선릉역_id, 강남역_id, 역삼역_id);
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("source에서 target으로 가는 경로가 없는 경우 400 에러를 반환한다.")
    @Test
    void searchUnreachablePath() {
        long 강남역_id = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역_id = requestCreateStation("역삼역").jsonPath().getLong("id");
        long 부산역_id = requestCreateStation("부산역").jsonPath().getLong("id");
        long 서면역_id = requestCreateStation("서면역").jsonPath().getLong("id");
        requestCreateLine("신분당선", "bg-red-600", 강남역_id, 역삼역_id, 10, 0);
        requestCreateLine("1호선", "bg-blue-600", 부산역_id, 서면역_id, 10, 0);

        ExtractableResponse<Response> response = requestSearchPath(강남역_id, 서면역_id, 21);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isNotBlank();
    }

    @DisplayName("추가 요금이 있는 지하철 노선 경로 탐색")
    @Test
    void searchPathHasExtraFareLine() {
        long 강남역_id = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역_id = requestCreateStation("역삼역").jsonPath().getLong("id");
        long 잠실역_id = requestCreateStation("잠실역").jsonPath().getLong("id");
        long 선릉역_id = requestCreateStation("선릉역").jsonPath().getLong("id");
        long 신분당선_id = requestCreateLine("신분당선", "bg-red-600", 강남역_id, 역삼역_id, 10, 1000).jsonPath()
            .getLong("id");
        long 일호선_id = requestCreateLine("1호선", "bg-blue-600", 강남역_id, 선릉역_id, 10, 900).jsonPath()
            .getLong("id");
        requestAddSection(신분당선_id, 역삼역_id, 잠실역_id, 10);
        requestAddSection(일호선_id, 잠실역_id, 강남역_id, 5);

        ExtractableResponse<Response> response = requestSearchPath(선릉역_id, 역삼역_id, 21);

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int fare = response.jsonPath().getObject("fare", Integer.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(선릉역_id, 강남역_id, 역삼역_id);
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(2450);
    }

    @DisplayName("청소년 지하철 경로 탐색")
    @Test
    void searchPathByYouth() {
        long 강남역_id = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역_id = requestCreateStation("역삼역").jsonPath().getLong("id");
        long 잠실역_id = requestCreateStation("잠실역").jsonPath().getLong("id");
        long 선릉역_id = requestCreateStation("선릉역").jsonPath().getLong("id");

        long 신분당선_id = requestCreateLine("신분당선", "bg-red-600", 강남역_id, 역삼역_id, 10, 0).jsonPath()
            .getLong("id");
        long 일호선_id = requestCreateLine("1호선", "bg-blue-600", 강남역_id, 선릉역_id, 10, 0).jsonPath()
            .getLong("id");
        requestAddSection(신분당선_id, 역삼역_id, 잠실역_id, 10);
        requestAddSection(일호선_id, 잠실역_id, 강남역_id, 5);

        ExtractableResponse<Response> response = requestSearchPath(선릉역_id, 역삼역_id, 15);

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int fare = response.jsonPath().getObject("fare", Integer.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(선릉역_id, 강남역_id, 역삼역_id);
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(880);
    }
}
