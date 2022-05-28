package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.station.StationResponse;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private List<Long> getResultLineIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        // when
        ExtractableResponse<Response> response = postStationResponse(대흥역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        postStationResponse(대흥역);

        // when
        ExtractableResponse<Response> response = postStationResponse(대흥역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = postStationResponse(대흥역);
        ExtractableResponse<Response> createResponse2 = postStationResponse(공덕역);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = getExpectedLineIds(createResponse1, createResponse2);
        List<Long> resultLineIds = getResultLineIds(response);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = postStationResponse(대흥역);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 지하철역을 삭제하면 예외가 발생한다.")
    @Test
    void deleteNotExistLine() {
        /// given

        // when
        ValidatableResponse validatableResponse = RestAssured.given().log().all()
                .when()
                .delete("stations/10")
                .then().log().all();

        // then
        assertThat(validatableResponse.extract().statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        validatableResponse.body("message", equalTo("존재하지 않는 지하철역입니다."));
    }

    @DisplayName("중복되는 이름의 지하철 역을 저장하면 예외가 발생한다.")
    @Test
    void saveSameNameStation() {
        // given
        postStationResponse(대흥역);

        // when
        ValidatableResponse validatableResponse = RestAssured.given().log().all()
                .body(대흥역)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        assertThat(validatableResponse.extract().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        validatableResponse.body("message", equalTo("지하철역 이름이 중복됩니다."));
    }

    @Test
    @DisplayName("잘못된 uri로 요청하면 예외가 발생한다.")
    void invalidUrl() {
        // given

        // when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when()
                .get("/station")
                .then().log().all()
                .extract();

        // then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
