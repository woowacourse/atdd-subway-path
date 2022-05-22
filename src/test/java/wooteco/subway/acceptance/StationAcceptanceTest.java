package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        RestAssured.given().log().all()
                .body(Map.of("name", "강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("stations"));
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        post("/stations", Map.of("name", "강남역"));

        RestAssured.given().log().all()
                .body(Map.of("name", "강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        post("/stations", Map.of("name", "강남역"));
        post("/stations", Map.of("name", "역삼역"));

        RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].name", equalTo("강남역"))
                .body("[1].name", equalTo("역삼역"));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        ExtractableResponse<Response> createResponse = post("/stations", Map.of("name", "강남역"));

        String uri = createResponse.header("Location");
        RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
