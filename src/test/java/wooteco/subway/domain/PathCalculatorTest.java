package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.factory.SectionFactory.AB3;
import static wooteco.subway.domain.factory.SectionFactory.BC3;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.factory.SectionFactory;
import wooteco.subway.domain.factory.StationFactory;

@DisplayName("PathCalculator 는")
class PathCalculatorTest {

    private static final Station STATION_A = StationFactory.from(StationFactory.A);
    private static final Station STATION_B = StationFactory.from(StationFactory.B);
    private static final Station STATION_C = StationFactory.from(StationFactory.C);

    private PathCalculator pathCalculator;

    @BeforeEach
    void setUp() {
        final Sections sections = new Sections(List.of(SectionFactory.from(AB3), SectionFactory.from(BC3)));
        final Line line = new Line(1L, "신분당선", "bg-red-600", sections, 1L);
        pathCalculator = new PathCalculator(List.of(line));
    }

    @DisplayName("지하철 최단 경로 목록을 조회한다.")
    @Test
    void findShortestPath() {
        assertThat(pathCalculator.findShortestPath(STATION_A, STATION_C))
                .isEqualTo(List.of(STATION_A, STATION_B, STATION_C));
    }

    @DisplayName("지하철 최단 경로 거리를 구한다.")
    @Test
    void findShortestDistance() {
        assertThat(pathCalculator.findShortestDistance(STATION_A, STATION_C))
                .isEqualTo(6);
    }
}
