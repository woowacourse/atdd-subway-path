package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        LineResponse line_5 = createLine("5호선", "bg-purple-600", 1L, 2L, 10);
        addSection(2L, 3L, 20, line_5.getId());
        createLine("6호선", "bg-yellow-600", 1L, 3L, 5);

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get("/paths?source=1&target=3&age=15");
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
}
