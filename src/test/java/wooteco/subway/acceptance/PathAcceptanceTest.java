package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        Long 아차산역_id = createStation("아차산역");
        Long 군자역_id = createStation("군자역");
        Long 장한평역_id = createStation("장한평역");
        LineResponse line_5 = createLine(new LineRequest("5호선", "bg-purple-600", 아차산역_id, 군자역_id, 10));
        createSection(new SectionRequest(군자역_id, 장한평역_id, 20), line_5.getId());
        createLine(new LineRequest("6호선", "bg-yellow-600", 아차산역_id, 장한평역_id, 5));
    }

    private void createSection(SectionRequest sectionRequest, Long lineId) {
        AcceptanceTestFixture.post("/lines/" + lineId + "/sections", sectionRequest);
    }

    private LineResponse createLine(LineRequest lineRequest) {
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines", lineRequest);
        return response.jsonPath().getObject(".", LineResponse.class);
    }

    private Long createStation(final String name) {
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/stations",
                new StationRequest(name));

        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findPath() {
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
