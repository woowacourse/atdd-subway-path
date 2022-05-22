package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("역이 없는 경우에 404 에러를 발생시킨다.")
    @Test
    void searchPathByNotFoundStation() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 1L)
                .param("target", 2L)
                .param("age", 20)
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
        requestCreateLine("신분당선", "bg-red-600", upStationId, downStationId, 10, 900);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", upStationId)
                .param("target", upStationId)
                .param("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 경로 탐색")
    @Test
    void searchPath() {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");
        long 잠실역 = requestCreateStation("잠실역").jsonPath().getLong("id");
        long 선릉역 = requestCreateStation("선릉역").jsonPath().getLong("id");
        long 신분당선 = requestCreateLine("신분당선", "bg-red-600", 강남역, 역삼역, 10, 0).jsonPath()
                .getLong("id");
        long 일호선 = requestCreateLine("1호선", "bg-blue-600", 강남역, 선릉역, 10, 0).jsonPath()
                .getLong("id");
        requestAddSection(신분당선, 역삼역, 잠실역, 10);
        requestAddSection(일호선, 잠실역, 강남역, 5);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 선릉역)
                .param("target", 역삼역)
                .param("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int fare = response.jsonPath().getObject("fare", Integer.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(선릉역, 강남역, 역삼역);
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("source에서 target으로 가는 경로가 없는 경우 400 에러를 반환한다.")
    @Test
    void searchUnreachablePath() {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");
        long 부산역 = requestCreateStation("부산역").jsonPath().getLong("id");
        long 서면역 = requestCreateStation("서면역").jsonPath().getLong("id");

        requestCreateLine("신분당선", "bg-red-600", 강남역, 역삼역, 10, 900);
        requestCreateLine("1호선", "bg-blue-600", 부산역, 서면역, 10, 0);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 서면역)
                .param("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("age가 음수이거나 150초과이면 400 에러를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"151", "1000", "0", "-1", " -1000"})
    void searchPathWithRidiculousAge(int age) {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("신분당선", "bg-red-600", 강남역, 역삼역, 10, 900);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에 추가 요금이 있을 경우 경로 탐색")
    @Test
    void searchPathWithExtraFare() {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("신분당선", "bg-red-600", 강남역, 역삼역, 20, 900);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int fare = response.jsonPath().getObject("fare", Integer.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(20);
        assertThat(fare).isEqualTo(1450+900);
    }

    @DisplayName("나이가 청소년인 경우 경로 탐색")
    @ParameterizedTest
    @CsvSource({"13", "15", "18"})
    void searchPathInCaseOfYouth(int age) {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = (int) ((1250-350)*0.8);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(8);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("나이가 어린이인 경우 경로 탐색")
    @CsvSource({"6", "9", "12"})
    void searchPathInCaseOfAChild(int age) {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = 0;

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(8);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("나이가 유아인 경우 경로 탐색")
    @CsvSource({"5", "3", "1"})
    void searchPathInCaseOfAInfant(int age) {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = 0;

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(8);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("나이가 노약자인 경우 경로 탐색")
    @CsvSource({"65", "85", "100", "150"})
    void searchPathInCaseOfElderlyLikeMyGrandfather(int age) {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = 0;

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(8);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("나이가 청소년이고 노선 추가 요금이 있을 경우 경로 탐색")
    @ParameterizedTest
    @CsvSource({"13", "15", "18"})
    void searchPathInCaseOfYouthWithExtraFare(int age) {
        long 강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        long 역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");

        requestCreateLine("신분당선", "bg-red-600", 강남역, 역삼역, 20, 900);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = (int) ((1450+900-350)*0.8);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(20);
        assertThat(actualFare).isEqualTo(expectedFare);
    }
}
