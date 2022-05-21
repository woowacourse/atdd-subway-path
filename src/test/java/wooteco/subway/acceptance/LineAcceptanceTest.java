package wooteco.subway.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.LineUpdateRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역과 길이를 활용하여 노선을 생성한다.")
    @Test
    void createLine() {
        Long 선릉역Id = generateStationId("선릉역");
        Long 잠실역Id = generateStationId("잠실역");
        Integer distance = 7;

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new LineRequest("2호선", "bg-green-600", 선릉역Id, 잠실역Id, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @DisplayName("노선 관리 기능을 확인한다.")
    @TestFactory
    Stream<DynamicTest> dynamicTestFromLine() {
        String name1 = "1호선";
        String color1 = "bg-blue-600";
        Long 중동역Id = generateStationId("중동역");
        Long 신도림역Id = generateStationId("신도림역");
        Integer distance1 = 10;

        String name2 = "2호선";
        String color2 = "bg-green-600";
        Long 선릉역Id = generateStationId("선릉역");
        Long 잠실역Id = generateStationId("잠실역");
        Integer distance2 = 10;

        Long line1Id = generateLineId(name1, color1, 중동역Id, 신도림역Id, distance1);
        Long line2Id = generateLineId(name2, color2, 선릉역Id, 잠실역Id, distance2);

        return Stream.of(
                dynamicTest("노선을 전체 조회한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/lines")
                            .then().log().all()
                            .extract();

                    List<Long> expectedLineIds = List.of(line1Id, line2Id);
                    List<Long> resultLineIds = response.jsonPath()
                            .getList(".", LineResponse.class)
                            .stream()
                            .map(LineResponse::getId)
                            .collect(toList());
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(resultLineIds).containsAll(expectedLineIds)
                    );
                }),

                dynamicTest("단일 노선을 조회한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/lines/" + line1Id)
                            .then().log().all()
                            .extract();

                    JsonPath responseJsonPath = response.body().jsonPath();
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(responseJsonPath.getLong("id")).isEqualTo(line1Id)
                    );
                }),

                dynamicTest("노선의 이름와 색깔을 활용하여 노선을 수정한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new LineUpdateRequest("1호선", "bg-green-600"))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .put("/lines/" + line1Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("존재하지 않는 노선을 수정할 경우 404를 반환한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new LineUpdateRequest("2호선", "bg-blue-600"))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .put("/lines/" + 0)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                }),

                dynamicTest("중복된 이름을 가진 노선으로 수정할 경우 예외를 던진다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new LineUpdateRequest("2호선", "bg-green-600"))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .put("/lines/" + line1Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                }),

                dynamicTest("노선의 id를 활용하여 노선을 삭제한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/" + line1Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),

                dynamicTest("존재하지 않는 노선의 id를 삭제할 경우 잘못된 요청이므로 404를 반환한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/" + line1Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                })
        );
    }

    @DisplayName("노선에 구간 추가")
    @TestFactory
    Stream<DynamicTest> dynamicTestFromAddSection() {
        Long 중동역Id = generateStationId("중동역");
        Long 신도림역Id = generateStationId("신도림역");
        Integer basedDistance = 10;

        Long lineId = generateLineId("1호선", "bg-blue-600", 중동역Id, 신도림역Id, basedDistance);

        return Stream.of(
                dynamicTest("상행 종점이 같은 경우 가장 앞단의 구간 보다 길이가 크거나 같으면 400을 반환한다.", () -> {
                    Long upStationId = 중동역Id;
                    Long downStationId = generateStationId("부천역");
                    Integer distance = 10;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                }),

                dynamicTest("상행 종점이 같은 경우 가장 앞단의 구간 보다 길이가 작으면 추가한다.", () -> {
                    Long upStationId = 중동역Id;
                    Long downStationId = generateStationId("역곡역");
                    Integer distance = 4;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("상행 종점에 구간을 추가한다.", () -> {
                    Long upStationId = generateStationId("부평역");
                    Long downStationId = 중동역Id;
                    Integer distance = 7;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("노선 조회 시 등록된 지하철 목록을 확인할 수 있다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/lines/{id}", lineId)
                            .then().log().all()
                            .extract();

                    List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(stations.size()).isEqualTo(4)
                    );
                }),

                dynamicTest("상행 종점 추가 시 지하철이 존재하지 않는 경우 404을 반환한다.", () -> {
                    Long upStationId = 0L;
                    Long downStationId = 중동역Id;
                    Integer distance = 7;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                }),

                dynamicTest("상행 종점 추가 시 상행역이 기존 노선에 존재하는 경우 400을 반환한다.", () -> {
                    Long upStationId = 신도림역Id;
                    Long downStationId = 중동역Id;
                    Integer distance = 7;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                }),

                dynamicTest("하행 종점이 같은 경우 가장 뒷단의 구간보다 길이가 크거나 같으면 400을 반환한디.", () -> {
                    Long upStationId = generateStationId("온수역");
                    Long downStationId = 신도림역Id;
                    Integer distance = 10;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                }),

                dynamicTest("하행 종점이 같은 경우 가장 앞단의 구간보다 길이가 작으면 추가한다.", () -> {
                    Long upStationId = generateStationId("개봉역");
                    Long downStationId = 신도림역Id;
                    Integer distance = 3;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("하행 종점에 구간을 추가한다.", () -> {
                    Long upStationId = 신도림역Id;
                    Long downStationId = generateStationId("영등포역");
                    Integer distance = 10;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("상행역과 하행역이 노선에 모두 존재하면 예외를 던진다.", () -> {
                    Long upStationId = 중동역Id;
                    Long downStationId = 신도림역Id;
                    Integer distance = 1;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                }),

                dynamicTest("상행역과 하행역이 노선에 모두 존재하지 않으면 예외를 던진다.", () -> {
                    Long upStationId = generateStationId("서울역");
                    Long downStationId = generateStationId("노량진역");
                    Integer distance = 1;

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .body(new SectionRequest(upStationId, downStationId, distance))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .post("/lines/{id}/sections", lineId)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                })
        );
    }

    @DisplayName("구간 삭제 기능")
    @TestFactory
    Stream<DynamicTest> dynamicTestFromRemoveSection() {
        Long 신도림역Id = generateStationId("신도림역");
        Long 온수역Id = generateStationId("온수역");
        Long 역곡역Id = generateStationId("역곡역");
        Long 부천역Id = generateStationId("부천역");
        Long 중동역Id = generateStationId("중동역");
        Long lineId = generateLineId("1호선", "bg-blue-600", 신도림역Id, 온수역Id, 5);

        addSection(lineId, 온수역Id, 역곡역Id, 5);
        addSection(lineId, 역곡역Id, 부천역Id, 5);
        addSection(lineId, 부천역Id, 중동역Id, 5);

        return Stream.of(
                dynamicTest("중간에 위치한 역을 삭제한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/{id}/sections?stationId={stationId}", lineId, 온수역Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("상행 종점의 구간을 삭제한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/{id}/sections?stationId={stationId}", lineId, 신도림역Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("존재하지 않는 역을 삭제할 경우 예외를 던진다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/{id}/sections?stationId={stationId}", lineId, 신도림역Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                }),

                dynamicTest("하행 종점의 구간을 삭제한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/{id}/sections?stationId={stationId}", lineId, 중동역Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                }),

                dynamicTest("구간이 한개 뿐인 경우 예외를 던진다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .delete("/lines/{id}/sections?stationId={stationId}", lineId, 온수역Id)
                            .then().log().all()
                            .extract();

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                })
        );
    }

    private Long generateStationId(String name) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new StationRequest(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        return response.jsonPath().getLong("id");
    }

    private Long generateLineId(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new LineRequest(name, color, upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return response.jsonPath().getLong("id");
    }

    private void addSection(Long id, Long upStationId, Long downStationId, Integer distance) {
        RestAssured.given().log().all()
                .body(new SectionRequest(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
    }
}
