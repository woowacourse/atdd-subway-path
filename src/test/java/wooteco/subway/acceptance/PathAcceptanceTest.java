package wooteco.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        createStation("아차산역");
        createStation("군자역");
        createStation("장한평역");
        LineResponse line_5 = createLine("5호선", "bg-purple-600", 1L, 2L, 10);
        addSection(2L, 3L, 20, line_5.getId());
        createLine("6호선", "bg-yellow-600", 1L, 3L, 5);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findPath() {
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get("/paths?source=1&target=3&age=15");
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        int distance = response.jsonPath().getInt("distance");
        int fare = response.jsonPath().getInt("fare");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(distance).isEqualTo(5),
                () -> assertThat(fare).isEqualTo(720),
                () -> assertThat(stations).extracting("name")
                        .containsExactly("아차산역", "장한평역")
        );
    }

    @DisplayName("연령값이 올바르지 않으면 예외를 반환한다.")
    @Test
    void thrown_invalidRequest() {
        final String uri = "/paths?source=1&target=3&age=-1";
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get(uri);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("연령은 양수이어야 합니다.")
        );
    }
}
