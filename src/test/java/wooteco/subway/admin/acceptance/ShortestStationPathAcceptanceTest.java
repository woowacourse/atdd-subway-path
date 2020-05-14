package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/schema-test.sql","/truncate.sql"})
public class ShortestStationPathAcceptanceTest extends AcceptanceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역"; //환승역
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "종합운동장역";
    private static final String STATION_NAME6 = "잠실새내역";
    private static final String STATION_NAME7 = "잠실역";
    private static final String STATION_NAME8 = "양재역";
    private static final String STATION_NAME9 = "매봉역";
    private static final String STATION_NAME10 = "도곡역";
    private static final String STATION_NAME11 = "대치역";
    private static final String STATION_NAME12 = "학여울역";
    private static final String STATION_NAME13 = "대청역";
    private static final String STATION_NAME14 = "수서역";
    private static final String STATION_NAME15 = "가락시장역";
    private static final String STATION_NAME16 = "송파역";
    private static final String STATION_NAME17 = "석촌역";
    private static final String STATION_NAME18 = "양재시민의숲역";
    private static final String STATION_NAME19 = "청계산입구역";
    private static final String STATION_NAME20 = "판교역";
    private static final String STATION_NAME21 = "정자역";
    private static final String STATION_NAME22 = "한티역";
    private static final String STATION_NAME23 = "구룡역";
    private static final String STATION_NAME24 = "개포동역";
    private static final String STATION_NAME25 = "대모산입구역";


    @Autowired
    StationRepository stationRepository;
    @LocalServerPort
    int port;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }


    @DisplayName("최단 경로를 찾는다")
    @Test
    void findShortestStationPath() {
        //Given 출발역과 종착역이 저장돼있다.
        createStation(STATION_NAME1);
        createStation(STATION_NAME2);
        createStation(STATION_NAME3);
        createStation(STATION_NAME4);
        createStation(STATION_NAME5);
        createStation(STATION_NAME6);
        createStation(STATION_NAME7);
        createStation(STATION_NAME8);
        createStation(STATION_NAME9);

        //And 출발역과 종착역이 같은 경로가 2개 저장돼있다.
        createLine("1호선");
        createLine("2호선");

        addLineStation(1L, null, 1L);
        addLineStation(1L, 1L, 2L);
        addLineStation(1L, 2L, 3L);
        addLineStation(1L, 3L, 4L);
        addLineStation(1L, 4L, 9L);

        addLineStation(2L, null, 1L);
        addLineStation(2L, 1L, 5L);
        addLineStation(2L, 5L, 6L);
        addLineStation(2L, 6L, 7L);
        addLineStation(2L, 7L, 8L);
        addLineStation(2L, 8L, 9L);

        //When 출발역과 종착역의 최단경로를 요청한다.
        PathResponse shortedStationPath = getShortestStationPath("강남역", "매봉역", "DISTANCE");


        //Then 최단 경로와 최단 거리가 나온다.
        System.out.println(shortedStationPath.getDistance());
        assertThat(shortedStationPath.getStations().size()).isEqualTo(5);
    }

    @DisplayName("2개의 노선에 같은 환승역이 존재 할때 최단 경로를 찾는다")
    @Test
    void findShortestStationPathWhenTransferStation() {
        //Given 출발역부 종착역사이의 역들이 저장돼있다.
        createStation(STATION_NAME1);
        createStation(STATION_NAME2);
        createStation(STATION_NAME3);
        createStation(STATION_NAME4);
        createStation(STATION_NAME5);
        createStation(STATION_NAME6);
        createStation(STATION_NAME7);

        createStation(STATION_NAME8);
        createStation(STATION_NAME9);
        createStation(STATION_NAME10);
        createStation(STATION_NAME11);
        createStation(STATION_NAME12);
        createStation(STATION_NAME13);
        createStation(STATION_NAME14);
        createStation(STATION_NAME15);
        createStation(STATION_NAME16);
        createStation(STATION_NAME17);
        createStation(STATION_NAME18);
        //And 출발역부터 종착역사이의 노선이 2개가 있다.
        createLine("1호선");
        createLine("2호선");
        //And 2개의 노선은 같은 환승역이 존재 한다.
        addLineStation(1L, null, 1L,10,10);
        addLineStation(1L, 1L, 2L,10,10);
        addLineStation(1L, 2L, 3L,10,10);
        addLineStation(1L, 3L, 4L,10,10);
        addLineStation(1L, 4L, 5L,10,10);
        addLineStation(1L, 5L, 6L,10,10);
        addLineStation(1L, 6L, 7L,10,10);

        addLineStation(2L, null, 1L, 1,1);
        addLineStation(2L, 1L, 8L,1,1);
        addLineStation(2L, 8L, 3L,1,1);
        addLineStation(2L, 3L, 9L,1,1);
        addLineStation(2L, 9L, 10L,1,1);
        addLineStation(2L, 10L, 11L,1,1);
        addLineStation(2L, 11L, 12L,1,1);
        addLineStation(2L, 12L, 13L,1,1);
        addLineStation(2L, 13L, 14L,1,1);
        addLineStation(2L, 14L, 15L,1,1);
        addLineStation(2L, 15L, 16L,1,1);
        addLineStation(2L, 16L, 17L,1,1);
        addLineStation(2L, 17L, 7L,1,1);

        //when 출발역과 종착역의 최단 경로를 요청한다.
        PathResponse shortestStationPath = getShortestStationPath("강남역", "잠실역", "DISTANCE");

        //then 최단 경로와 최단 거리가 나온다.
        assertThat(shortestStationPath.getStations()).hasSize(13);

    }

    @DisplayName("2개의 노선에 같은 환승역이 존재하고 환승역은 또다른 노선을 가지고 있을 때 최단 경로를 찾는다")
    @Test
    void findShortestStationPathWhenTransferStationHasAnotherLine() {
        //Given 출발역부 종착역사이의 역들이 저장돼있다.
        createStation(STATION_NAME1);
        createStation(STATION_NAME2);
        createStation(STATION_NAME3);
        createStation(STATION_NAME4);
        createStation(STATION_NAME5);
        createStation(STATION_NAME6);
        createStation(STATION_NAME7);

        createStation(STATION_NAME8);
        createStation(STATION_NAME9);
        createStation(STATION_NAME10);
        createStation(STATION_NAME11);
        createStation(STATION_NAME12);
        createStation(STATION_NAME13);
        createStation(STATION_NAME14);
        createStation(STATION_NAME15);
        createStation(STATION_NAME16);
        createStation(STATION_NAME17);
        createStation(STATION_NAME18);
        createStation(STATION_NAME19);
        createStation(STATION_NAME20);
        createStation(STATION_NAME21);
        //And 출발역부터 종착역사이의 노선이 2개가 있다.
        createLine("1호선");
        createLine("2호선");
        createLine("신분당선");
        //And 2개의 노선은 같은 환승역이 존재 한다.
        addLineStation(1L, null, 1L,10,10);
        addLineStation(1L, 1L, 2L,10,10);
        addLineStation(1L, 2L, 3L,10,10);
        addLineStation(1L, 3L, 4L,10,10);
        addLineStation(1L, 4L, 5L,10,10);
        addLineStation(1L, 5L, 6L,10,10);
        addLineStation(1L, 6L, 7L,10,10);

        addLineStation(2L, null, 1L, 1,1);
        addLineStation(2L, 1L, 8L,1,1);
        addLineStation(2L, 8L, 3L,1,1);
        addLineStation(2L, 3L, 9L,1,1);
        addLineStation(2L, 9L, 10L,1,1);
        addLineStation(2L, 10L, 11L,1,1);
        addLineStation(2L, 11L, 12L,1,1);
        addLineStation(2L, 12L, 13L,1,1);
        addLineStation(2L, 13L, 14L,1,1);
        addLineStation(2L, 14L, 15L,1,1);
        addLineStation(2L, 15L, 16L,1,1);
        addLineStation(2L, 16L, 17L,1,1);
        addLineStation(2L, 17L, 7L,1,1);

        addLineStation(3L, null, 1L, 1, 1);
        addLineStation(3L, 1L, 18L, 1, 1);
        addLineStation(3L, 18L, 19L, 1, 1);
        addLineStation(3L, 19L, 20L, 1, 1);
        addLineStation(3L, 20L, 21L, 1, 1);

        PathResponse shortestStationPath = getShortestStationPath("강남역", "잠실역", "DISTANCE");
        assertThat(shortestStationPath.getStations()).hasSize(13);
    }

    @DisplayName("강남역 부터 잠실역 까지 실제 노선도를 만들어 최단거리를 계산한다.")
    @Test
    void findShortestStationPathRealCase() {
        //Given 출발역부 종착역사이의 역들이 저장돼있다.
        createStation(STATION_NAME1);
        createStation(STATION_NAME2);
        createStation(STATION_NAME3);
        createStation(STATION_NAME4);
        createStation(STATION_NAME5);
        createStation(STATION_NAME6);
        createStation(STATION_NAME7);
        createStation(STATION_NAME8);
        createStation(STATION_NAME9);
        createStation(STATION_NAME10);
        createStation(STATION_NAME11);
        createStation(STATION_NAME12);
        createStation(STATION_NAME13);
        createStation(STATION_NAME14);
        createStation(STATION_NAME15);
        createStation(STATION_NAME16);
        createStation(STATION_NAME17);
        createStation(STATION_NAME18);
        createStation(STATION_NAME19);
        createStation(STATION_NAME20);
        createStation(STATION_NAME21);
        createStation(STATION_NAME22);
        createStation(STATION_NAME23);
        createStation(STATION_NAME24);
        createStation(STATION_NAME25);
        //And 출발역부터 종착역사이의 노선이 2개가 있다.
        createLine("2호선");
        createLine("3호선");
        createLine("8호선");
        createLine("신분당선");
        createLine("분당선");
        //And 2개의 노선은 같은 환승역이 존재 한다.

        //2호선
        addLineStation(1L, null, 1L,10,10);
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


    private PathResponse getShortestStationPath(String source, String target, String pathType) {
        return given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(String.format("/stations/shortest-path?source=%s&target=%s&pathType=%s",source, target, pathType)).
                then().
                log().all().
                extract().as(PathResponse.class);
    }
}


