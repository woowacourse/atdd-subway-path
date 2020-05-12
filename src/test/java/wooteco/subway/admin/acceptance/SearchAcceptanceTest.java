package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchAcceptanceTest extends AcceptanceTest {
    /**
     * Given 지하철 역이 여러개 추가되어있다.
     * And 지하철 노선이 추가되어있다.
     * And 지하철 구간이 추가되어있다.
     * <p>
     * When 출발역과 도착역에 대한 최단 거리 요청을 보낸다.
     * Then 최단 거리 기준으로 경로와 총 소요시간, 총 거리를 응답 받는다.
    */

    @Test
    void find_shortestDistancePath() throws UnsupportedEncodingException {
        LineResponse lineResponse1 = createLine("1호선");
        LineResponse lineResponse2 = createLine("2호선");
        LineResponse lineResponse3 = createLine("3호선");

        StationResponse stationResponse1 = createStation("환-강남역");
        StationResponse stationResponse2 = createStation("1-역삼역");
        StationResponse stationResponse3 = createStation("환-삼성역");
        StationResponse stationResponse4 = createStation("1-삼송역");
        StationResponse stationResponse5 = createStation("환-지축역");
        StationResponse stationResponse6 = createStation("2-역삼역");
        StationResponse stationResponse7 = createStation("2-삼송역");
        StationResponse stationResponse8 = createStation("3-역삼역");
        StationResponse stationResponse9 = createStation("3-삼송역");

        addLineStation(lineResponse1.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 10, 10);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId(), 10, 10);
        addLineStation(lineResponse1.getId(), stationResponse3.getId(), stationResponse4.getId(), 10, 10);
        addLineStation(lineResponse1.getId(), stationResponse4.getId(), stationResponse5.getId(), 10, 10);

        addLineStation(lineResponse2.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse6.getId(), 20, 20);
        addLineStation(lineResponse2.getId(), stationResponse6.getId(), stationResponse3.getId(), 20, 20);
        addLineStation(lineResponse2.getId(), stationResponse3.getId(), stationResponse7.getId(), 20, 20);
        addLineStation(lineResponse2.getId(), stationResponse7.getId(), stationResponse5.getId(), 20, 20);

        addLineStation(lineResponse3.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse1.getId(), stationResponse8.getId(), 50, 50);
        addLineStation(lineResponse3.getId(), stationResponse8.getId(), stationResponse3.getId(), 50, 50);
        addLineStation(lineResponse3.getId(), stationResponse3.getId(), stationResponse9.getId(), 50, 50);
        addLineStation(lineResponse3.getId(), stationResponse9.getId(), stationResponse5.getId(), 50, 50);

        PathResponse pathResponse = retrieveShortestDistancePath("환-강남역", "환-지축역");

        assertThat(pathResponse.getDuration()).isEqualTo(40);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getStations().get(1).getName()).isEqualTo("1-역삼역");

    }

    private PathResponse retrieveShortestDistancePath(String source, String target) throws UnsupportedEncodingException {
        String encodedSource = URLEncoder.encode(source, "UTF-8");
        String encodedTarget = URLEncoder.encode(target, "UTF-8");
        return given()
                .when()
                    .get("/paths?source=" + encodedSource + "&target=" + encodedTarget)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(PathResponse.class);
    }
}
