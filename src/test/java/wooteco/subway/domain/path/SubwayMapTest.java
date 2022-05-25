package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.domain.fixtures.TestFixtures.강남;
import static wooteco.subway.domain.fixtures.TestFixtures.강남_건대;
import static wooteco.subway.domain.fixtures.TestFixtures.강남_삼성;
import static wooteco.subway.domain.fixtures.TestFixtures.건대;
import static wooteco.subway.domain.fixtures.TestFixtures.건대_성수;
import static wooteco.subway.domain.fixtures.TestFixtures.당고개;
import static wooteco.subway.domain.fixtures.TestFixtures.사당;
import static wooteco.subway.domain.fixtures.TestFixtures.사당_당고개;
import static wooteco.subway.domain.fixtures.TestFixtures.삼성;
import static wooteco.subway.domain.fixtures.TestFixtures.삼성_건대;
import static wooteco.subway.domain.fixtures.TestFixtures.성수;
import static wooteco.subway.domain.fixtures.TestFixtures.성수_강남;
import static wooteco.subway.domain.fixtures.TestFixtures.왕십리;
import static wooteco.subway.domain.fixtures.TestFixtures.왕십리_당고개;
import static wooteco.subway.domain.fixtures.TestFixtures.왕십리_합정;
import static wooteco.subway.domain.fixtures.TestFixtures.잠실;
import static wooteco.subway.domain.fixtures.TestFixtures.잠실_당고개;
import static wooteco.subway.domain.fixtures.TestFixtures.창동;
import static wooteco.subway.domain.fixtures.TestFixtures.창동_당고개;
import static wooteco.subway.domain.fixtures.TestFixtures.합정;
import static wooteco.subway.domain.fixtures.TestFixtures.합정_성수;
import static wooteco.subway.domain.fixtures.TestFixtures.합정_왕십리;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.domain.path.fare.vo.Age;
import wooteco.subway.domain.line.Section;
import wooteco.subway.domain.path.ShortestPath;
import wooteco.subway.domain.path.SubwayMap;
import wooteco.subway.domain.station.Station;

public class SubwayMapTest {

    /*
     * 2호선 :  강남---10---삼성---15---건대---20---성수
     * 분당선 : 강남---------------------------10---성수---10--합정---50---왕십리
     */
    @Test
    @DisplayName("최단 경로를 구한다. 삼성 -> 성수")
    void route() {
        SubwayMap graph = new SubwayMap(List.of(강남_삼성, 삼성_건대, 건대_성수, 왕십리_합정, 합정_성수, 성수_강남));

        ShortestPath shortestPath = graph.getShortestPath(삼성, 성수);

        assertThat(shortestPath.getShortestRoute()).containsExactly(삼성, 강남, 성수);
    }

    /*
     * 2호선 :  강남---10---삼성---15---건대---20---성수
     * 분당선 : 강남---------------------------10---성수---10--합정---50---왕십리
     */
    @Test
    @DisplayName("최단 경로의 거리를 구한다. 삼성 -> 성수")
    void distance() {
        SubwayMap graph = new SubwayMap(List.of(강남_삼성, 삼성_건대, 건대_성수, 왕십리_합정, 합정_성수, 성수_강남));

        ShortestPath shortestPath = graph.getShortestPath(삼성, 성수);

        assertThat(shortestPath.getShortestDistance()).isEqualTo(20);
    }

    @Test
    @DisplayName("추가요금을 포함하여 요금을 계산한다.")
    void extraFare() {
        SubwayMap graph = new SubwayMap(List.of(강남_삼성, 삼성_건대, 건대_성수, 왕십리_합정, 합정_성수, 성수_강남));

        ShortestPath fare = graph.getShortestPath(삼성, 성수);

        assertThat(fare.calculateFare(new Age(20))).isEqualTo(1450 + 1000);
    }

    @Test
    @DisplayName("추가요금이 있는 노선중 가장 높은 노선의 금액으로 기본 금액에 추가한다.")
    void compareExtraFare() {
        SubwayMap graph = new SubwayMap(List.of(성수_강남, 강남_건대));

        ShortestPath fare = graph.getShortestPath(성수, 건대);

        assertThat(fare.calculateFare(new Age(20))).isEqualTo(1450 + 2000);
    }

    @Test
    @DisplayName("경로가 존재하지 않는 경우 예외가 발생한다.")
    void notFoundRoute() {
        List<Section> 구간들 = List.of(강남_삼성, 삼성_건대, 건대_성수, 왕십리_합정, 합정_성수, 성수_강남, 창동_당고개);
        SubwayMap subwayMap = new SubwayMap(구간들);

        assertThatThrownBy(() -> subwayMap.getShortestPath(강남, 창동))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 경로가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 역으로 조회할 시 예외가 발생한다.")
    void notFoundStation() {
        List<Section> 구간들 = List.of(강남_삼성, 삼성_건대, 건대_성수, 왕십리_합정, 합정_성수, 성수_강남);
        SubwayMap subwayMap = new SubwayMap(구간들);

        assertThatThrownBy(() -> subwayMap.getShortestPath(강남, 창동))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @ParameterizedTest
    @MethodSource("getSections")
    @DisplayName("요금을 반환한다.")
    void getFare(Station source, Station target, int fare) {
        SubwayMap subwayMap = new SubwayMap(
                List.of(강남_삼성, 삼성_건대, 건대_성수, 합정_왕십리, 합정_성수, 창동_당고개, 왕십리_당고개, 사당_당고개, 잠실_당고개));

        ShortestPath shortestPath = subwayMap.getShortestPath(source, target);
        assertThat(shortestPath.calculateFare(new Age(20))).isEqualTo(fare);
    }

    private static List<Arguments> getSections() {
        return List.of(
                Arguments.of(강남, 삼성, 1250),                         // 10KM
                Arguments.of(삼성, 건대, 1350),                         // 15KM
                Arguments.of(건대, 성수, 1450),                         // 20KM
                Arguments.of(합정, 왕십리, 2050),                       // 50KM
                Arguments.of(창동, 당고개, 2150),                       // 58KM
                Arguments.of(왕십리, 당고개, 2250),                     // 64KM
                Arguments.of(사당, 당고개, 3650),                       // 178KM
                Arguments.of(잠실, 당고개, 3950)                        // 200KM
        );
    }

}
