package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findPath() {
        // given
        createStation("아차산역");
        createStation("군자역");
        createStation("장한평역");
        LineResponse line_5 = createLine("5호선", "bg-purple-600", 1L, 2L, 10, 0);
        addSection(2L, 3L, 20, line_5.getId());
        createLine("6호선", "bg-yellow-600", 1L, 3L, 5, 0);

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get("/paths?source=1&target=3&age=20");
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        int distance = response.jsonPath().getInt("distance");
        int fare = response.jsonPath().getInt("fare");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(distance).isEqualTo(5),
                () -> assertThat(fare).isEqualTo(1250),
                () -> assertThat(stations).extracting("name")
                        .containsExactly("아차산역", "장한평역")
        );
    }

    @DisplayName("나이에 따라 요금이 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {"2,0", "6,450", "14,720", "20,1250"})
    void findPath_withAgeDiscount(int age, int resultFare) {
        // given
        createStation("아차산역");
        createStation("군자역");
        createStation("장한평역");
        LineResponse line_5 = createLine("5호선", "bg-purple-600", 1L, 2L, 10, 0);
        addSection(2L, 3L, 20, line_5.getId());
        createLine("6호선", "bg-yellow-600", 1L, 3L, 5, 0);

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get("/paths?source=1&target=3&age=" + age);
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        int distance = response.jsonPath().getInt("distance");
        int fare = response.jsonPath().getInt("fare");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(distance).isEqualTo(5),
                () -> assertThat(fare).isEqualTo(resultFare),
                () -> assertThat(stations).extracting("name")
                        .containsExactly("아차산역", "장한평역")
        );
    }

    @DisplayName("경로에서 거쳐가는 노선 중 가장 큰 추가 요금이 붙는다.")
    @Test
    void findPath_withExtraFare() {
        // given
        Long 아차산역 = createStation("아차산역");
        Long 군자역 = createStation("군자역");
        Long 장한평역 = createStation("장한평역");
        Long 신림역 = createStation("신림역");
        Long 봉천역 = createStation("봉천역");

        LineResponse line_5 = createLine("5호선", "bg-purple-600", 아차산역, 군자역, 1, 200);
        addSection(군자역, 장한평역, 1, line_5.getId());
        LineResponse line_2 = createLine("2호선", "bg-green-600", 장한평역, 신림역, 1, 500);
        addSection(신림역, 봉천역, 1, line_2.getId());

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get(
                String.format("/paths?source=%d&target=%d&age=20", 아차산역, 봉천역));
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        int distance = response.jsonPath().getInt("distance");
        int fare = response.jsonPath().getInt("fare");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(distance).isEqualTo(4),
                () -> assertThat(fare).isEqualTo(1750),
                () -> assertThat(stations).extracting("name")
                        .containsExactly("아차산역", "군자역", "장한평역", "신림역", "봉천역")
        );
    }
}
