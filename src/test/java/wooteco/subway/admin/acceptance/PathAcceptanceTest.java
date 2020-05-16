package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.line.LineResponse;
import wooteco.subway.admin.dto.path.PathResponse;
import wooteco.subway.admin.dto.station.StationResponse;

import static org.assertj.core.api.Assertions.*;

public class PathAcceptanceTest extends AcceptanceTest {
    StationResponse jamsil;
    StationResponse jamsilsaenae;
    StationResponse playgound;
    StationResponse samjun;
    StationResponse sukchongobun;
    StationResponse sukchon;
    StationResponse bongensa;
    StationResponse busan;
    StationResponse daegu;

    LineResponse line2;
    LineResponse line8;
    LineResponse line9;
    LineResponse ktx;

    /**
     *              잠실 ------20km------ 석촌
     *               |                    |
     *               10km                 |
     *               |                    |
     *              잠실새내              10km
     *               |                      |
     *               10km                    |
     *               |                        |
     *  봉은사 -10km- 종합운동장 -10km- 삼전 -10km-- 석촌고분
     *
     *  duration 은 모두 1
     */
    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        jamsil = createStation("잠실");
        jamsilsaenae = createStation("잠실새내");
        playgound = createStation("종합운동장");
        samjun = createStation("삼전");
        sukchongobun = createStation("석촌고분");
        sukchon = createStation("석촌");
        bongensa = createStation("봉은사");
        busan = createStation("부산");
        daegu = createStation("대구");

        line2 = createLine("2호선");
        line8 = createLine("8호선");
        line9 = createLine("9호선");
        ktx = createLine("ktx");

        addLineStation(line2.getId(), null, jamsil.getId(), 0, 0);
        addLineStation(line2.getId(), jamsil.getId(), jamsilsaenae.getId(), 10, 1);
        addLineStation(line2.getId(), jamsilsaenae.getId(), playgound.getId(), 10, 1);


        addLineStation(line9.getId(), null, bongensa.getId(), 0, 0);
        addLineStation(line9.getId(), bongensa.getId(), playgound.getId(), 10, 1);
        addLineStation(line9.getId(), playgound.getId(), samjun.getId(), 10, 1);
        addLineStation(line9.getId(), samjun.getId(), sukchongobun.getId(), 10, 1);
        addLineStation(line9.getId(), sukchongobun.getId(), sukchon.getId(), 10, 1);

        addLineStation(line8.getId(), null, jamsil.getId(), 0, 0);
        addLineStation(line8.getId(),  jamsil.getId(), sukchon.getId(), 20, 1);


        addLineStation(ktx.getId(),  null, busan.getId(), 100, 100);
        addLineStation(ktx.getId(),  busan.getId(), daegu.getId(), 100, 100);
    }

    @DisplayName("최소시간 기준으로 경로 조회(경로가 여러개)")
    @Test
    void findPathByDurationWhenTwoPath() {
        PathResponse pathByDuration = findPath(jamsil.getName(), samjun.getName(), "duration");
        assertThat(pathByDuration.getStations()).hasSize(4);
        assertThat(pathByDuration.getStations()).extracting(StationResponse::getName)
                .containsExactly("잠실", "잠실새내", "종합운동장", "삼전");
        assertThat(pathByDuration.getTotalDistance()).isEqualTo(30);
        assertThat(pathByDuration.getTotalDuration()).isEqualTo(3);
    }

    @DisplayName("최소시간 기준으로 경로 조회(경로가 한개)")
    @Test
    void findPathByDurationWhenOnePath() {
        PathResponse pathByDuration = findPath(jamsilsaenae.getName(), samjun.getName(), "duration");
        assertThat(pathByDuration.getStations()).hasSize(3);
        assertThat(pathByDuration.getStations()).extracting(StationResponse::getName)
                .containsExactly("잠실새내", "종합운동장", "삼전");
        assertThat(pathByDuration.getTotalDistance()).isEqualTo(20);
        assertThat(pathByDuration.getTotalDuration()).isEqualTo(2);
    }

    @DisplayName("최단경로 기준으로 경로 조회")
    @Test
    void findPath() {
        //when
        PathResponse pathByDistance = findPath(jamsil.getName(), samjun.getName(), "distance");

        //then
        assertThat(pathByDistance.getStations()).hasSize(4);
        assertThat(pathByDistance.getStations()).extracting(StationResponse::getName)
                .containsExactly("잠실", "잠실새내", "종합운동장", "삼전");
        assertThat(pathByDistance.getTotalDistance()).isEqualTo(30);
        assertThat(pathByDistance.getTotalDuration()).isEqualTo(3);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void findPathWithError1() {
        assertThat(findPathWithError("잠실", "잠실", "distance"))
                .isEqualTo("경로가 존재하지 않습니다");
    }

    @DisplayName("연결되어 있지 않는 경")
    @Test
    void findPathWithError2() {
        assertThat(findPathWithError("잠실", "부산", "distance"))
                .isEqualTo("경로가 존재하지 않습니다");
    }
}
