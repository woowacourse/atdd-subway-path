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
    private static final String STATION_NAME_KANGNAM = "강남역";
    private static final String STATION_NAME_YEOKSAM = "역삼역";
    private static final String STATION_NAME_SEOLLEUNG = "선릉역"; //환승역
    private static final String STATION_NAME_SAMSUNG = "삼성역";
    private static final String STATION_NAME_SPORTS_COMPLEX = "종합운동장역";
    private static final String STATION_NAME_JAMSILSAENAE = "잠실새내역";
    private static final String STATION_NAME_JAMSIL = "잠실역";
    private static final String STATION_NAME_YANGJAE = "양재역";
    private static final String STATION_NAME_MAEBONG = "매봉역";
    private static final String STATION_NAME_DOGOK = "도곡역";
    private static final String STATION_NAME_DAECHI = "대치역";
    private static final String STATION_NAME_HANGNYEOUL = "학여울역";
    private static final String STATION_NAME_DAECHUNG = "대청역";
    private static final String STATION_NAME_SUSEO = "수서역";
    private static final String STATION_NAME_GARAK_MARKET = "가락시장역";
    private static final String STATION_NAME_SONGPA = "송파역";
    private static final String STATION_NAME_SEOKCHON = "석촌역";
    private static final String STATION_NAME_YANGJAE_CITIZEN_FOREST = "양재시민의숲역";
    private static final String STATION_NAME_CHEONGGYESAN = "청계산입구역";
    private static final String STATION_NAME_PANGYO = "판교역";
    private static final String STATION_NAME_JUNGJA = "정자역";
    private static final String STATION_NAME_HANTI = "한티역";
    private static final String STATION_NAME_GURYONG = "구룡역";
    private static final String STATION_NAME_GAEPODONG = "개포동역";
    private static final String STATION_NAME_DAEMOSAN = "대모산입구역";

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
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_SAMSUNG);
        createStation(STATION_NAME_SPORTS_COMPLEX);
        createStation(STATION_NAME_JAMSILSAENAE);
        createStation(STATION_NAME_JAMSIL);
        createStation(STATION_NAME_YANGJAE);
        createStation(STATION_NAME_MAEBONG);
        createStation(STATION_NAME_DOGOK);
        createStation(STATION_NAME_DAECHI);
        createStation(STATION_NAME_HANGNYEOUL);
        createStation(STATION_NAME_DAECHUNG);
        createStation(STATION_NAME_SUSEO);
        createStation(STATION_NAME_GARAK_MARKET);
        createStation(STATION_NAME_SONGPA);
        createStation(STATION_NAME_SEOKCHON);
        createStation(STATION_NAME_YANGJAE_CITIZEN_FOREST);
        createStation(STATION_NAME_CHEONGGYESAN);
        createStation(STATION_NAME_PANGYO);
        createStation(STATION_NAME_JUNGJA);
        createStation(STATION_NAME_HANTI);
        createStation(STATION_NAME_GURYONG);
        createStation(STATION_NAME_GAEPODONG);
        createStation(STATION_NAME_DAEMOSAN);
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

    @DisplayName("강남역 부터 잠실역 까지 실제 노선도를 만들어 최단거리를 계산한다.")
    @Test
    void findFastestStationPath() {
        //Given 출발역부 종착역사이의 역들이 저장돼있다.
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_SAMSUNG);
        createStation(STATION_NAME_SPORTS_COMPLEX);
        createStation(STATION_NAME_JAMSILSAENAE);
        createStation(STATION_NAME_JAMSIL);
        createStation(STATION_NAME_YANGJAE);
        createStation(STATION_NAME_MAEBONG);
        createStation(STATION_NAME_DOGOK);
        createStation(STATION_NAME_DAECHI);
        createStation(STATION_NAME_HANGNYEOUL);
        createStation(STATION_NAME_DAECHUNG);
        createStation(STATION_NAME_SUSEO);
        createStation(STATION_NAME_GARAK_MARKET);
        createStation(STATION_NAME_SONGPA);
        createStation(STATION_NAME_SEOKCHON);
        createStation(STATION_NAME_YANGJAE_CITIZEN_FOREST);
        createStation(STATION_NAME_CHEONGGYESAN);
        createStation(STATION_NAME_PANGYO);
        createStation(STATION_NAME_JUNGJA);
        createStation(STATION_NAME_HANTI);
        createStation(STATION_NAME_GURYONG);
        createStation(STATION_NAME_GAEPODONG);
        createStation(STATION_NAME_DAEMOSAN);
        //And 출발역부터 종착역사이의 노선이 2개가 있다.
        createLine("2호선");
        createLine("3호선");
        createLine("8호선");
        createLine("신분당선");
        createLine("분당선");
        //And 2개의 노선은 같은 환승역이 존재 한다.

        //2호선
        addLineStation(1L, null, 1L,10,1);
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
        return given().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get(String.format("/stations/shortest-path?source=%s&target=%s&pathType=%s",source, target, pathType)).
                then().
                log().all().
                extract().as(PathResponse.class);
    }
}


