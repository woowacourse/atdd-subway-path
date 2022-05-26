package wooteco.subway.infrastructure.jgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixture.강남_역삼;
import static wooteco.subway.TestFixture.강남역_ID;
import static wooteco.subway.TestFixture.삼성역_ID;
import static wooteco.subway.TestFixture.선릉_삼성;
import static wooteco.subway.TestFixture.선릉역_ID;
import static wooteco.subway.TestFixture.역삼_선릉;
import static wooteco.subway.TestFixture.역삼역_ID;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.graph.SubwayGraph;

class SubwayJGraphTest {

    private static final long LINE_ID = 1;
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    private static final long LINE_EXTRA_FARE = 0L;

    private SubwayGraph subwayGraph;

    @BeforeEach
    void setUp() {
        this.subwayGraph = new SubwayJGraph();
    }

    @DisplayName("구간 정보와 출발지 도착지를 이용해 최단 경로 및 거리를 계산한다")
    @Test
    void calculateShortestRoute() {
        Path shortestRoute = subwayGraph.calculateShortestPath(
                List.of(new Line(LINE_ID, List.of(강남_역삼, 역삼_선릉), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE)),
                강남역_ID, 선릉역_ID);

        assertAll(
                () -> assertThat(shortestRoute.getPath()).containsExactly(강남역_ID, 역삼역_ID, 선릉역_ID),
                () -> assertThat(shortestRoute.getDistance()).isEqualTo(강남_역삼.getDistance() + 역삼_선릉.getDistance())
        );
    }

    @DisplayName("도달할 수 없는 경로를 조회하다")
    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class CalculateImpossiblePathTest {

        @DisplayName("존재하는 노선 중 어느 하나도 출발지 혹은 도착지를 포함하고 있지 않는 경우")
        @ParameterizedTest
        @MethodSource("provideForCalculateWithNonConnectedStation")
        void calculateWithNonConnectedStation(List<Line> lines, long source, long target) {
            assertThatThrownBy(() -> subwayGraph.calculateShortestPath(lines, source, target))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("출발지 또는 도착지에 연결된 구간이 존재하지 않습니다.");
        }

        private Stream<Arguments> provideForCalculateWithNonConnectedStation() {
            return Stream.of(
                    Arguments.of(List.of(
                            new Line(LINE_ID, List.of(역삼_선릉), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE),
                            new Line(LINE_ID, List.of(선릉_삼성), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE)),
                            강남역_ID, 삼성역_ID),
                    Arguments.of(List.of(
                            new Line(LINE_ID, List.of(강남_역삼), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE),
                            new Line(LINE_ID, List.of(역삼_선릉), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE)),
                            강남역_ID, 삼성역_ID));
        }

        @DisplayName("출발지부터 도착지까지의 경로가 끊어져 있는 경우")
        @Test
        void calculateWithNonConnectedSections() {
            List<Line> lines = List.of(
                    new Line(LINE_ID, List.of(강남_역삼), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE),
                    new Line(LINE_ID, List.of(선릉_삼성), LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE));
            assertThatThrownBy(() -> subwayGraph.calculateShortestPath(lines, 강남역_ID, 삼성역_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("출발지부터 도착지까지 이어지는 경로가 존재하지 않습니다.");
        }
    }
}