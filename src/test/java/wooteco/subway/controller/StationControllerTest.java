package wooteco.subway.controller;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.acceptance.RestUtil.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.ori.acceptancetest.SpringBootAcceptanceTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.controller.dto.StationRequest;

@SpringBootAcceptanceTest
public class StationControllerTest {

    @Test
    @DisplayName("이름이 빈 값을 요청하면 400 응답을 던진다.")
    void createStationWithNull() {
        // when
        ExtractableResponse<Response> response = post("/stations", new StationRequest());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("역 이름이 비었습니다.");
    }
}
