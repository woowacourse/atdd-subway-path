package wooteco.subway.admin.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {
    private List<LineStation> lineStations;

    @BeforeEach
    void setUp() {
        Line line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 0, 0));
        line1.addLineStation(new LineStation(1L, 2L, 50, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));

        Line line2 = new Line(2L, "8호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 10, 50));
        line2.addLineStation(new LineStation(2L, 6L, 10, 10));


        Line line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(null, 9L, 0, 0));
        lineStations = Stream.of(line1, line2, line3)
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @DisplayName("전체 호선에 대한 그래프 생성 확인")
    @Test
    void createDistanceGraph() {
        ShortestPath shortestPath = ShortestPath.of(lineStations, PathType.DURATION);
        assertThat(shortestPath.getTotalPath()).isInstanceOf(DijkstraShortestPath.class);
    }

    @DisplayName("PathType에 따라 Duration, Distance 제대로 찾는지 검증")
    @ParameterizedTest
    @CsvSource({"DISTANCE, 20, 60", "DURATION, 60, 20"})
    void calculateDuartionDistatnceByTypeTest(String pathType, int distance, int duration) {
        ShortestPath shortestPath = ShortestPath.of(lineStations, PathType.of(pathType));
        Long source = 1L;
        Long target = 3L;
        assertThat(shortestPath.getDistance(source, target)).isEqualTo(distance);
        assertThat(shortestPath.getDuration(source, target)).isEqualTo(duration);
    }

    @DisplayName("경로 확인")
    @Test
    void pathTest() {
        ShortestPath shortestPath = ShortestPath.of(lineStations, PathType.DISTANCE);
        Long source = 1L;
        Long target = 3L;

        List<Long> actualPath = shortestPath.getVertexList(source, target);
        List<Long> expected = Arrays.asList(1L, 2L, 3L);

        assertThat(actualPath).hasSize(3);
        assertThat(actualPath).containsAll(expected);
    }

    @DisplayName("(예외) 존재하지 않는 경로")
    @ParameterizedTest
    @CsvSource({"1, 10", "1, 9"})
    void notExistPathTest(Long source, Long target) {
        ShortestPath shortestPath = ShortestPath.of(lineStations, PathType.DISTANCE);
        assertThatThrownBy(() -> shortestPath.getPath(source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 경로입니다.");
    }
}
