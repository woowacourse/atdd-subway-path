package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineEditRequest;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("두 역을 생성한 뒤 노선을 생성한다.")
    @Test
    void 역_생성_후_노선_생성() {
        Long upStationId = postStationThenReturnId("강남역");
        Long downStationId = postStationThenReturnId("선릉역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", upStationId, downStationId, 10, 100);

        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("line"))
                .body("name", equalTo("2호선"))
                .body("extraFare", equalTo(100));

    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성하면 404코드를 반환한다.")
    @Test
    void 존재하는_노선_이름_생성_예외() {
        Long upStationId = postStationThenReturnId("강남역");
        Long downStationId = postStationThenReturnId("선릉역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", upStationId, downStationId, 10, 100);
        postToLines(lineRequest);

        Long otherStationId = postStationThenReturnId("잠실역");
        LineRequest duplicatedLineNameRequest = new LineRequest(
                "2호선", "bg-red-600", downStationId, otherStationId, 10, 100);

        RestAssured.given().log().all()
                .body(duplicatedLineNameRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("복수의 역을 등록하고 두 노선을 생성 후, 전체 노선을 조회한다.")
    @Test
    void 전체_노선_조회() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        LineRequest lineRequest1 = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId1 = postToLines(lineRequest1).jsonPath().getLong("id");

        Long 양재역Id = postStationThenReturnId("양재역");
        LineRequest lineRequest2 = new LineRequest(
                "신분당선", "bg-green-600", 강남역Id, 양재역Id, 3, 100);
        Long lineId2 = postToLines(lineRequest2).jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo(lineId1.intValue()))
                .body("[1].id", equalTo(lineId2.intValue()));
    }

    @DisplayName("두 역과 둘을 잇는 노선을 생성 후 조회한다.")
    @Test
    void 단일_노선_조회() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        LineRequest request = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(request).jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("2호선"))
                .body("color", equalTo("bg-green-600"))
                .body("stations[0].id", equalTo(강남역Id.intValue()))
                .body("extraFare", equalTo(100));
    }

    @DisplayName("두 역과 둘을 잇는 노선을 생성 후, 이름과 색상을 수정한다.")
    @Test
    void 노선_수정() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        LineRequest lineRequest1 = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(lineRequest1).jsonPath().getLong("id");

        LineEditRequest request = new LineEditRequest("3호선", "bg-green-300");

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + lineId)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("두 역과 둘을 잇는 노선을 생성 후 삭제한다.")
    @Test
    void 노선_삭제() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(lineRequest).jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lineId)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("세 역을 등록하고 둘만 잇는 노선을 생성한 후, 사이에 나머지 한 역을 포함한 구간을 추가한다.")
    @Test
    void 구간_추가() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        Long 역삼역Id = postStationThenReturnId("역삼역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(lineRequest).jsonPath().getLong("id");

        SectionRequest request = new SectionRequest(역삼역Id, 선릉역Id, 1);

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("둘만 잇는 노선을 생성하고, 길이 상 추가 불가능한 구간을 추가할 시 404코드를 반환한다.")
    @Test
    void 구간_추가_예외() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        Long 역삼역Id = postStationThenReturnId("역삼역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(lineRequest).jsonPath().getLong("id");

        SectionRequest request = new SectionRequest(역삼역Id, 선릉역Id, 5);

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("세 역과 셋을 잇는 구간을 생성 후, 중간 역을 삭제해 구간을 삭제한다.")
    @Test
    void 구간_삭제() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        Long 역삼역Id = postStationThenReturnId("역삼역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(lineRequest).jsonPath().getLong("id");

        SectionRequest request = new SectionRequest(역삼역Id, 선릉역Id, 1);

        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();

        RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lineId + "/sections?stationId=" + 선릉역Id)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("두 역과 둘을 잇는 노선을 생성 후, 한 쪽 역을 삭제 시도하면 404코드를 반환한다.")
    @Test
    void 구간_삭제_예외() {
        Long 강남역Id = postStationThenReturnId("강남역");
        Long 선릉역Id = postStationThenReturnId("선릉역");
        LineRequest lineRequest = new LineRequest(
                "2호선", "bg-green-600", 강남역Id, 선릉역Id, 3, 100);
        Long lineId = postToLines(lineRequest).jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lineId + "/sections?stationId=" + 선릉역Id)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> postToLines(LineRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
        return response;
    }

    private Long postStationThenReturnId(String name) {
        StationRequest request = new StationRequest(name);
        Long stationId = RestAssured.given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getLong("id");
        return stationId;
    }
}
