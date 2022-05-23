package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Sql("/pathInitSchema.sql")
class ShortestPathAcceptanceTest extends AcceptanceTest {

    @ParameterizedTest
    @DisplayName("나이에 따라서 최단 경로와 요금을 조회한다.")
    @CsvSource(value = {"12, 1900", "18, 2830", "20, 3450"})
    void findPath(int age, int expectedFare) {
        // given
        long source = 1L;
        long target = 7L;

        // when
        ExtractableResponse<Response> response =
                requestHttpGet("/paths?source=" + source + "&target=" + target + "&age=" + age);

        // then
        PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);
        List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(stationNames).containsExactly("신도림역", "왕십리역", "상일동역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(expectedFare)
        );
    }
}
