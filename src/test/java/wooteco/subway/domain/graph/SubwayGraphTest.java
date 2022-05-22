package wooteco.subway.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.TestFixture.강남_역삼_선릉_삼성;
import static wooteco.subway.domain.TestFixture.강남_선릉_거리;
import static wooteco.subway.domain.TestFixture.강남_역삼;
import static wooteco.subway.domain.TestFixture.강남역;
import static wooteco.subway.domain.TestFixture.광교역;
import static wooteco.subway.domain.TestFixture.삼성역;
import static wooteco.subway.domain.TestFixture.선릉_삼성;
import static wooteco.subway.domain.TestFixture.선릉역;
import static wooteco.subway.domain.TestFixture.역삼역;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.domain.station.Station;

class SubwayGraphTest {

    private SubwayGraph subwayGraph;

    @BeforeEach
    void setUp() {
        this.subwayGraph = new SubwayGraph(강남_역삼_선릉_삼성);
    }

    @DisplayName("구간 정보와 출발지 도착지를 이용해 최단 경로 및 거리를 계산한다")
    @Test
    void calculateShortestRoute() {
        Route shortestRoute = subwayGraph.calculateShortestRoute(강남역, 선릉역);
        List<Station> actualRoute = shortestRoute.getRoute();
        int actualDistance = shortestRoute.getDistance();

        assertAll(
                () -> assertThat(actualRoute).containsExactly(강남역, 역삼역, 선릉역),
                () -> assertThat(actualDistance).isEqualTo(강남_선릉_거리)
        );
    }

    @DisplayName("구간 정보 중 출발지 혹은 도착지가 존재하지 않습니다.")
    @ParameterizedTest
    @MethodSource("provideForValidateSourceAndTargetBothExist")
    void validateSourceAndTargetBothExist(Station source, Station target) {
        assertThatThrownBy(() -> subwayGraph.calculateShortestRoute(source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지 또는 도착지에 대한 구간 정보가 없습니다");
    }

    private static Stream<Arguments> provideForValidateSourceAndTargetBothExist() {
        return Stream.of(
                Arguments.of(광교역, 강남역),
                Arguments.of(강남역, 광교역));
    }

    @Test
    @DisplayName("출발지부터 도착지까지 연결된 경로가 없을 경우 반환값이 존재하지 않는다")
    void ifRouteDoesNotExistResultShouldBeNull() {
        SubwayGraph subwayGraph = new SubwayGraph(List.of(강남_역삼, 선릉_삼성));
        assertThatThrownBy(() -> subwayGraph.calculateShortestRoute(강남역, 삼성역))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("출발지부터 도착지까지 구간이 연결되어 있지 않습니다.");
    }
}
