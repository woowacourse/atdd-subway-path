package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

public class PathAcceptanceTestRelatedAge extends AcceptanceTest {

    private long 강남역;
    private long 역삼역;

    @BeforeEach
    void set() {
        강남역 = requestCreateStation("강남역").jsonPath().getLong("id");
        역삼역 = requestCreateStation("역삼역").jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> getPath(int age) {
        return RestAssured.given().log().all()
                .param("source", 강남역)
                .param("target", 역삼역)
                .param("age", age)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    @DisplayName("나이가 청소년인 경우 경로 탐색")
    @ParameterizedTest
    @CsvSource({"13", "15", "18"})
    void searchPathInCaseOfYouth(int age) {
        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);
        ExtractableResponse<Response> response = getPath(age);

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = (int) ((1250 - 350) * 0.8);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(8);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("나이가 어린이인 경우 경로 탐색")
    @CsvSource({"6", "9", "12"})
    void searchPathInCaseOfAChild(int age) {
        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);
        ExtractableResponse<Response> response = getPath(age);

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
        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);
        ExtractableResponse<Response> response = getPath(age);

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
        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 8, 0);
        ExtractableResponse<Response> response = getPath(age);

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
        requestCreateLine("2호선", "bg-red-600", 강남역, 역삼역, 20, 900);
        ExtractableResponse<Response> response = getPath(age);

        List<Long> actualStationIds = response.jsonPath().getList("stations.id", Long.class);
        int distance = response.jsonPath().getObject("distance", Integer.class);
        int actualFare = response.jsonPath().getObject("fare", Integer.class);
        int expectedFare = (int) ((1450 + 900 - 350) * 0.8);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualStationIds).containsExactly(강남역, 역삼역);
        assertThat(distance).isEqualTo(20);
        assertThat(actualFare).isEqualTo(expectedFare);
    }
}
