package wooteco.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

import static org.hamcrest.Matchers.is;
import static wooteco.subway.acceptance.AcceptanceFixture.insert;
import static wooteco.subway.acceptance.AcceptanceFixture.select;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("최단 경로 조회")
    void showPath() {
        insert(new StationRequest("교대역"), "/stations", 201);
        insert(new StationRequest("강남역"), "/stations", 201);
        insert(new StationRequest("역삼역"), "/stations", 201);
        insert(new StationRequest("선릉역"), "/stations", 201);
        LineRequest lineRequest = new LineRequest("2호선", "green", 1L, 2L, 10, 0);
        long id = insert(lineRequest, "/lines", 201).extract().jsonPath().getLong("id");

        insert(new SectionRequest(2L, 3L, 40), "/lines/" + id + "/sections", 201);

        select("/paths?source=1&target=3&age=15", 200)
                .body("stations.size()", is(3))
                .body("distance", is(50))
                .body("fare", is(2050));
    }
}
