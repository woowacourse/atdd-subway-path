package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
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
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .param("source", 1L)
            .param("target", 2L)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("source와 target이 같은 경우 400에러를 발생시킨다.")
    @Test
    void searchPathBySourceSameAsTarget() {
        long upStationId = requestCreateStation("강남역").jsonPath().getLong("id");
        long downStationId = requestCreateStation("역삼역").jsonPath().getLong("id");
        requestCreateLine("신분당선", "bg-red-600", upStationId, downStationId, 10);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .param("source", upStationId)
            .param("target", upStationId)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 경로 탐색")
    @Test
    void searchPath() {
        long station1 = requestCreateStation("강남역").jsonPath().getLong("id");
        long station2 = requestCreateStation("역삼역").jsonPath().getLong("id");
        long station3 = requestCreateStation("잠실역").jsonPath().getLong("id");
        long station4 = requestCreateStation("선릉역").jsonPath().getLong("id");
        long line1 = requestCreateLine("신분당선", "bg-red-600", station1, station2, 10).jsonPath()
            .getLong("id");
        long line2 = requestCreateLine("1호선", "bg-blue-600", station1, station4, 10).jsonPath()
            .getLong("id");
        requestAddSection(line1, station2, station3, 10);
        requestAddSection(line2, station3, station1, 5);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .param("source", station4)
            .param("target", station2)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int fare = response.jsonPath().getObject("fare", Integer.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(station4, station1, station2);
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("source에서 target으로 가는 경로가 없는 경우 400 에러를 반환한다.")
    @Test
    void searchUnreachablePath() {
        long station1 = requestCreateStation("강남역").jsonPath().getLong("id");
        long station2 = requestCreateStation("역삼역").jsonPath().getLong("id");
        long station3 = requestCreateStation("부산역").jsonPath().getLong("id");
        long station4 = requestCreateStation("서면역").jsonPath().getLong("id");

        requestCreateLine("신분당선", "bg-red-600", station1, station2, 10);
        requestCreateLine("1호선", "bg-blue-600", station3, station4, 10);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .param("source", station1)
            .param("target", station4)
            .when()
            .get("/paths")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
