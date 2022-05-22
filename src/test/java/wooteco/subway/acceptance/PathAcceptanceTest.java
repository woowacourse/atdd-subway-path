package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Sql("/pathInitSchema.sql")
class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("최단 경로와 요금을 조회한다. - 일반 요금")
    void findPath() {
        // given
        final long source = 1L;
        final long target = 7L;
        final int age = 27;

        // when
        final ExtractableResponse<Response> response =
                requestHttpGet("/paths?source=" + source + "&target=" + target + "&age=" + age);

        // then
        final PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);
        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(stationNames).containsExactly("신도림역", "왕십리역", "상일동역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2450)
        );
    }

    @Test
    @DisplayName("최단 경로와 요금을 조회한다. - 어린이 요금")
    void findPathChildren() {
        // given
        final long source = 1L;
        final long target = 7L;
        final int age = 6;

        // when
        final ExtractableResponse<Response> response =
                requestHttpGet("/paths?source=" + source + "&target=" + target + "&age=" + age);

        // then
        final PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);
        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(stationNames).containsExactly("신도림역", "왕십리역", "상일동역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2000)
        );
    }

    @Test
    @DisplayName("최단 경로와 요금을 조회한다. - 청소년 요금")
    void findPathTeenager() {
        // given
        final long source = 1L;
        final long target = 7L;
        final int age = 13;

        // when
        final ExtractableResponse<Response> response =
                requestHttpGet("/paths?source=" + source + "&target=" + target + "&age=" + age);

        // then
        final PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);
        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(stationNames).containsExactly("신도림역", "왕십리역", "상일동역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2270)
        );
    }
}
