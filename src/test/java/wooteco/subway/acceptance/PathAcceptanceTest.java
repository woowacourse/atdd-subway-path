package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로 조회 관련 기능을 확인한다.")
    @TestFactory
    Stream<DynamicTest> dynamicTestFromPath() {
        Long 건대입구역Id = generateStationId("건대입구역");
        Long 강남구청역Id = generateStationId("강남구청역");
        Long 대림역Id = generateStationId("대림역");
        Long 낙성대역Id = generateStationId("낙성대역");

        Long line7 = generateLineId("7호선", "deep green", 건대입구역Id, 강남구청역Id, 10);
        addSection(line7, 강남구청역Id, 대림역Id, 10);

        Long line2 = generateLineId("2호선", "green", 건대입구역Id, 낙성대역Id, 5);
        addSection(line2, 낙성대역Id, 대림역Id, 5);

        return Stream.of(
                dynamicTest("서로 다른 노선을 포함한 경로를 조회한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/paths?source={sourceId}&target={targetId}&age={age}",
                                    건대입구역Id, 대림역Id, 24)
                            .then().log().all()
                            .extract();

                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(10)
                    );
                }),

                dynamicTest("서로 다른 노선을 포함한 경로를 조회한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/paths?source={sourceId}&target={targetId}&age={age}",
                                    강남구청역Id, 낙성대역Id, 24)
                            .then().log().all()
                            .extract();

                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(15)
                    );
                }),

                dynamicTest("서로 같은 역의 경로를 조회한다.", () -> {
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/paths?source={sourceId}&target={targetId}&age={age}",
                                    강남구청역Id, 강남구청역Id, 24)
                            .then().log().all()
                            .extract();

                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(0)
                    );
                }),

                dynamicTest("다른 노선의 갈 수 없는 경로를 조회한다.", () -> {
                    Long 부천역Id = generateStationId("부천역");
                    Long 중동역Id = generateStationId("중동역");

                    generateLineId("1호선", "blue", 부천역Id, 중동역Id, 10);

                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .when()
                            .get("/paths?source={sourceId}&target={targetId}&age={age}",
                                    부천역Id, 대림역Id, 24)
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
