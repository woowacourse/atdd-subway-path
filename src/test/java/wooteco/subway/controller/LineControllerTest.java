package wooteco.subway.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.ori.acceptancetest.SpringBootAcceptanceTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.RestUtil;
import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.LineUpdateRequest;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.controller.dto.StationRequest;

@SpringBootAcceptanceTest
public class LineControllerTest {

    private Long stationId1;
    private Long stationId2;

    @BeforeEach
    void setStation() {
        ExtractableResponse<Response> stationResponse1 = RestUtil.post(new StationRequest("강남역"));
        ExtractableResponse<Response> stationResponse2 = RestUtil.post(new StationRequest("역삼역"));
        stationId1 = RestUtil.getIdFromStation(stationResponse1);
        stationId2 = RestUtil.getIdFromStation(stationResponse2);
    }

    @DisplayName("노선 이름이 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullName() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineRequest(null, "bg-red-600", stationId1, stationId2, 10, 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("노선 이름이 비었습니다.")
        );
    }

    @DisplayName("노선 색상이 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullColor() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineRequest("2호선", null, stationId1, stationId2, 10, 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("노선 색상이 비었습니다.")
        );
    }

    @DisplayName("상행 역 아이디가 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullUpStationId() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineRequest("2호선", "bg-red-600", null, stationId2, 10, 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("상행 역의 id 가 비었습니다.")
        );
    }

    @DisplayName("하행 역 아이디가 빈 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNullDownStationId() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineRequest("2호선", "bg-red-600", stationId1, null, 10, 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("하행 역의 id 가 비었습니다.")
        );
    }

    @DisplayName("거리가 1 이하인 생성 요청을 하면 400 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void createLineWithLessThan1(int distance) {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineRequest("2호선", "bg-red-600", stationId1, stationId2, distance, 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("거리는 1 이상이어야 합니다.")
        );
    }

    @DisplayName("추가 요금이 음수인 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createLineWithNegative() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineRequest("2호선", "bg-red-600", stationId1, stationId2, 10, -1))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("추가 요금 값은 음수일 수 없습니다.")
        );
    }

    @DisplayName("노선 이름이 빈 수정 요청을 하면 400 에러가 발생한다.")
    @Test
    void updateLineWithNullName() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineUpdateRequest(null, "bg-red-600", 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/lines/1")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("노선 이름이 비었습니다.")
        );
    }

    @DisplayName("노선 색상이 빈 수정 요청을 하면 400 에러가 발생한다.")
    @Test
    void updateLineWithNullColor() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineUpdateRequest("2호선", null, 1000))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/lines/1")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("노선 색상이 비었습니다.")
        );
    }

    @DisplayName("노선 추가요금이 음수인 요청을 하면 400 에러가 발생한다.")
    @Test
    void updateLineWithNullExtraFare() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new LineUpdateRequest("2호선", "bg-red-600", -1))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/lines/1")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("추가 요금 값은 음수일 수 없습니다.")
        );
    }

    @DisplayName("상행 역 아이디가 빈 구간 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createSectionWithNullUpStationId() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new SectionRequest(null, stationId2, 10))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("상행 역의 id 가 비었습니다.")
        );
    }

    @DisplayName("하행 역 아이디가 빈 구간 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createSectionWithNullDownStationId() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new SectionRequest(stationId1, null, 10))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("하행 역의 id 가 비었습니다.")
        );
    }

    @DisplayName("구간 거리가 1 미만인 구간 생성 요청을 하면 400 에러가 발생한다.")
    @Test
    void createSectionWithDistanceLessThan1() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(new SectionRequest(stationId1, stationId2, -1))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("거리는 1 이상이어야 합니다.")
        );
    }
}
