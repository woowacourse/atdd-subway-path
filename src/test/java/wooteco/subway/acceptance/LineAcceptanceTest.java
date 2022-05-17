package wooteco.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.StationRequest;

import static org.hamcrest.Matchers.is;
import static wooteco.subway.acceptance.AcceptanceFixture.insert;

public class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선 등록")
    void createLine() {
        insert(new StationRequest("강남역"), "/stations", 201);
        insert(new StationRequest("역삼역"), "/stations", 201);

        LineRequest lineRequest = new LineRequest("2호선", "green", 1L, 2L, 10, 1250);
        insert(lineRequest, "/lines", 201)
                .body("id", is(1))
                .body("name", is("2호선"))
                .body("extraFare", is(1250))
                .body("stations.size()", is(2));
    }
}
