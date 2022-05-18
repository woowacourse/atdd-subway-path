package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.response.ExceptionResponse;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.PathNotFoundException;
import wooteco.subway.exception.RowNotFoundException;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("최단경로를 조회한다.")
    public void findPath() {
        // given
        setUpSubway();

        // when
        final ExtractableResponse<Response> response = SimpleRestAssured.get("/paths?source=1&target=3&age=123");
        final PathResponse pathResponse = SimpleRestAssured.toObject(response, PathResponse.class);

        // then
        Assertions.assertAll(
            () -> assertThat(pathResponse.getStations()).hasSize(3),
            () -> assertThat(pathResponse.getDistance()).isEqualTo(16),
            () -> assertThat(pathResponse.getFare()).isEqualTo(1450)
        );
    }

    @Test
    @DisplayName("기존에 존재하는 역이 아닐 경우 경로 조회시 예외가 발생한다.")
    public void throwsExceptionWithAnyEmptyId() {
        // given
        setUpSubway();

        // when
        final ExtractableResponse<Response> response = SimpleRestAssured.get("/paths?source=1&target=5&age=123");
        final ExceptionResponse exceptionResponse = SimpleRestAssured.toObject(response, ExceptionResponse.class);

        // then
        assertThat(exceptionResponse.getException()).isEqualTo(RowNotFoundException.class);
    }

    @Test
    @DisplayName("경로가 존재하지 않을 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFound() {
        // given
        setUpSubway();
        SimpleRestAssured.post("/stations", Map.of("name", "D역"));

        // when
        final ExtractableResponse<Response> response = SimpleRestAssured.get("/paths?source=1&target=4&age=123");
        final ExceptionResponse exceptionResponse = SimpleRestAssured.toObject(response, ExceptionResponse.class);

        // then
        assertThat(exceptionResponse.getException()).isEqualTo(PathNotFoundException.class);
    }

    @Test
    @DisplayName("출발지와 도착지가 같은 경우 예외를 던진다.")
    public void throwsExceptionWithSameSourceAndTarget() {
        // given
        setUpSubway();

        // when
        final ExtractableResponse<Response> response = SimpleRestAssured.get("/paths?source=1&target=1&age=123");
        final ExceptionResponse exceptionResponse = SimpleRestAssured.toObject(response, ExceptionResponse.class);

        // then
        assertThat(exceptionResponse.getException()).isEqualTo(PathNotFoundException.class);
    }

    private void setUpSubway() {
        SimpleRestAssured.post("/stations", Map.of("name", "A역"));
        SimpleRestAssured.post("/stations", Map.of("name", "B역"));
        SimpleRestAssured.post("/stations", Map.of("name", "C역"));

        Map<String, String> lineParams = Map.of(
            "name", "신분당선",
            "color", "bg-red-600",
            "upStationId", "1",
            "downStationId", "2",
            "distance", "10");
        SimpleRestAssured.post("/lines", lineParams);
        Map<String, String> sectionParams = Map.of(
            "upStationId", "2",
            "downStationId", "3",
            "distance", "6"
        );
        SimpleRestAssured.post("/lines/1/sections", sectionParams);
    }
}
