package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.ui.dto.ExceptionResponse;
import wooteco.subway.ui.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = requestToCreateStation("강남역");
        StationResponse stationResponse = response.as(StationResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(stationResponse.getName()).isEqualTo("강남역")
        );
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성하면 BadRequest를 반환한다..")
    @Test
    void createStation_badRequest_DuplicateName() {
        // given
        requestToCreateStation("강남역");

        // when
        ExtractableResponse<Response> response = requestToCreateStation("강남역");
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("이름은 중복될 수 없습니다.")
        );
    }

    @DisplayName("null로 지하철 역을 생성하려고 하면 badRequest를 응답한다.")
    @Test
    void createStation_badRequest_null() {
        // when
        ExtractableResponse<Response> response = requestToCreateStation(null);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("입력되지 않은 정보가 있습니다.")
        );
    }

    @DisplayName("빈 문자열로 지하철 역을 생성하려고 하면 badRequest를 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createStation_badRequest_empty(String value) {
        // when
        ExtractableResponse<Response> response = requestToCreateStation(value);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("입력되지 않은 정보가 있습니다.")
        );
    }

    @DisplayName("모든 지하철역을 조회한다.")
    @Test
    void findAllStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = requestToCreateStation("강남역");
        ExtractableResponse<Response> createResponse2 = requestToCreateStation("역삼역");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStationById() {
        // given
        ExtractableResponse<Response> createResponse = requestToCreateStation("강남역");

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
}
