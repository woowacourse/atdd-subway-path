package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortestStationPathAcceptanceTest extends AcceptanceTest {

    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "신촌";
    private static final String STATION_NAME6 = "합정";
    private static final String STATION_NAME7 = "을지로3가";
    private static final String STATION_NAME8 = "압구정";
    private static final String STATION_NAME9 = "홍대입구";

    @BeforeEach
    void setUp() {

    }

    @DisplayName("최단 경로를 찾는다")
    @Test
    void findShortestStationPath() {
//        //Given 출발역과 종착역이 저장돼있다.
//        createStation(STATION_NAME1);
//        createStation(STATION_NAME2);
//        createStation(STATION_NAME3);
//        createStation(STATION_NAME4);
//        createStation(STATION_NAME5);
//        createStation(STATION_NAME6);
//        createStation(STATION_NAME7);
//        createStation(STATION_NAME8);
//        createStation(STATION_NAME9);
//        //And 출발역과 종착역이 같은 경로가 2개 저장돼있다.
//        createLine("1호선");
//        createLine("2호선");
//
//        addLineStation(1L, null, 1L);
//        addLineStation(1L, 1L, 2L);
//        addLineStation(1L, 2L, 3L);
//        addLineStation(1L, 3L, 4L);
//        addLineStation(1L, 4L, 9L);
//
//        addLineStation(2L, null, 1L);
//        addLineStation(2L, 1L, 5L);
//        addLineStation(2L, 5L, 6L);
//        addLineStation(2L, 6L, 7L);
//        addLineStation(2L, 7L, 8L);
//        addLineStation(2L, 8L, 9L);

        //When 출발역과 종착역의 최단경로를 요청한다.
        LineDetailResponse shortedStationPath = getShortestStationPath();

        //Then 최단 경로와 최단 거리가 나온다.
        assertThat(shortedStationPath.getName()).isEqualTo("1호선");

    }

    private LineDetailResponse getShortestStationPath() {
        return given().when().
                    get("/stations/shortest-pathsss").
                then().
                    log().all().
                    extract().jsonPath().get();
    }
}


