package wooteco.subway.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static wooteco.subway.acceptance.RestUtil.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import com.ori.acceptancetest.SpringBootAcceptanceTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootAcceptanceTest
public class PathControllerTest {

    @DisplayName("상행 역 Id 가 빈 요청을 하면 400 에러가 발생한다.")
    @Test
    void pathEmptyUpStationId() {
        // when
        ExtractableResponse<Response> response = get("/paths?source=" + "&target=" + 1L + "&age=15");

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("출발 역의 id 가 비었습니다.")
        );
    }

    @DisplayName("하행 역 Id 가 빈 요청을 하면 400 에러가 발생한다.")
    @Test
    void pathEmptyDownStationId() {
        // when
        ExtractableResponse<Response> response = get("/paths?source=" + 1L + "&target=" + "&age=15");

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).isEqualTo("도착 역의 id 가 비었습니다.")
        );
    }

    @DisplayName("나이가 옳지 않은 빈 요청을 하면 400 에러가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {":convert", "-1:연령 값은 음수일 수 없습니다."}, delimiter = ':')
    void pathInvalidAge(String age, String message) {
        // when
        ExtractableResponse<Response> response = get("/paths?source=" + 1L + "&target=" + 2L + "&age=" + age);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.jsonPath().getString("message")).contains(message)
        );
    }
}
