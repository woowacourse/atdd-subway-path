package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/schema-test.sql","/truncate.sql"})
public class ShortestStationPathAcceptanceTest extends AcceptanceTest {
    @Autowired
    StationRepository stationRepository;
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("강남역 부터 잠실역 까지 실제 노선도를 만들어 최단거리를 계산한다.")
    @Test
    void findShortestStationPath() {
        //Given 출발역부 종착역사이의 역들이 저장돼있다.
        createStations();
        //And 출발역부터 종착역사이의 노선이 2개가 있다.
        createLines();
        //And 2개의 노선은 같은 환승역이 존재 한다.

        //2호선
        addLineStation(1L, null, 1L, 10, 10);
        addLineStation(1L, 1L, 2L,10,10);
        addLineStation(1L, 2L, 3L,10,10);
        addLineStation(1L, 3L, 4L,10,10);
        addLineStation(1L, 4L, 5L,10,10);
        addLineStation(1L, 5L, 6L,10,10);
        addLineStation(1L, 6L, 7L,10,10);

        //3호선
        addLineStation(2L,null,8L,1,10);
        addLineStation(2L,8L,9L,1,10);
        addLineStation(2L,9L,10L,1,10);
        addLineStation(2L,10L,11L,10,10);
        addLineStation(2L,11L,12L,10,10);
        addLineStation(2L,12L,13L,10,10);
        addLineStation(2L,13L,14L,10,10);
        addLineStation(2L,14L,15L,1,10);

        //8호선
        addLineStation(3L,null,15L,1,10);
        addLineStation(3L,15L,16L,1,10);
        addLineStation(3L,16L,17L,1,10);
        addLineStation(3L,17L,7L,1,10);

        //분당선
        addLineStation(4L,null,3L,10,10);
        addLineStation(4L,3L,22L,10,10);
        addLineStation(4L,22L,10L,1,10);
        addLineStation(4L,10L,23L,1,10);
        addLineStation(4L,23L,24L,1,10);
        addLineStation(4L,24L,25L,1,10);
        addLineStation(4L,25L,14L,1,10);
        //신분당선
        addLineStation(5L, null,1L,1,10);
        addLineStation(5L, 1L,8L,1,10);

        PathResponse shortestStationPath = getShortestStationPath("강남역", "잠실역", "DISTANCE");

        assertThat(shortestStationPath.getDistance()).isEqualTo(11);
    }

    @DisplayName("강남역 부터 잠실역 까지 실제 노선도를 만들어 최단거리를 계산한다.")
    @Test
    void findFastestStationPath() {
        //Given 출발역부 종착역사이의 역들이 저장돼있다.
        createStations();
        //And 출발역부터 종착역사이의 노선이 2개가 있다.
        createLines();
        //And 2개의 노선은 같은 환승역이 존재 한다.

        //2호선
        addLineStation(1L, null, 1L, 10, 1);
        addLineStation(1L, 1L, 2L,10,1);
        addLineStation(1L, 2L, 3L,10,1);
        addLineStation(1L, 3L, 4L,10,1);
        addLineStation(1L, 4L, 5L,10,1);
        addLineStation(1L, 5L, 6L,10,1);
        addLineStation(1L, 6L, 7L,10,1);

        //3호선
        addLineStation(2L,null,8L,1,10);
        addLineStation(2L,8L,9L,1,10);
        addLineStation(2L,9L,10L,1,10);
        addLineStation(2L,10L,11L,10,10);
        addLineStation(2L,11L,12L,10,10);
        addLineStation(2L,12L,13L,10,10);
        addLineStation(2L,13L,14L,10,10);
        addLineStation(2L,14L,15L,1,10);

        //8호선
        addLineStation(3L,null,15L,1,10);
        addLineStation(3L,15L,16L,1,10);
        addLineStation(3L,16L,17L,1,10);
        addLineStation(3L,17L,7L,1,10);

        //분당선
        addLineStation(4L,null,3L,10,10);
        addLineStation(4L,3L,22L,10,10);
        addLineStation(4L,22L,10L,1,10);
        addLineStation(4L,10L,23L,1,10);
        addLineStation(4L,23L,24L,1,10);
        addLineStation(4L,24L,25L,1,10);
        addLineStation(4L,25L,14L,1,10);
        //신분당선
        addLineStation(5L, null,1L,1,10);
        addLineStation(5L, 1L,8L,1,10);

        PathResponse shortestStationPath = getShortestStationPath("강남역", "잠실역", "DURATION");

        assertThat(shortestStationPath.getDuration()).isEqualTo(6);
    }

    private PathResponse getShortestStationPath(String source, String target, String pathType) {
        String path = String.format("/stations/shortest-path?source=%s&target=%s&pathType=%s", source, target, pathType);
        return get(path, PathResponse.class);
    }

    private void createLines() {
        createLine("2호선");
        createLine("3호선");
        createLine("8호선");
        createLine("신분당선");
        createLine("분당선");
    }

    private void createStations() {
        createStation(강남역);
        createStation(역삼역);
        createStation(선릉역);
        createStation(삼성역);
        createStation(종합운동장역);
        createStation(잠실새내역);
        createStation(잠실역);
        createStation(양재역);
        createStation(매봉역);
        createStation(도곡역);
        createStation(대치역);
        createStation(학여울역);
        createStation(대청역);
        createStation(수서역);
        createStation(가락시장역);
        createStation(송파역);
        createStation(석촌역);
        createStation(양재시민의숲역);
        createStation(청계산입구역);
        createStation(판교역);
        createStation(정자역);
        createStation(한티역);
        createStation(구룡역);
        createStation(개포동역);
        createStation(대모산입구역);
    }
}


