package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

    @Test
    @DisplayName("최단 경로 정보 반환")
    void getGraph() {
        var stations = List.of(
                new Station(1L, "테스트1역"),
                new Station(2L, "테스트2역"),
                new Station(3L, "테스트3역")
        );

        var sections = List.of(
                new Section(1L, 2L, 2),
                new Section(1L, 3L, 2),
                new Section(2L, 3L, 100)
        );

        var path = new Path(stations, sections);

        var graphPath = path.getPath(2L, 3L);

        assertAll(
                () -> assertThat(graphPath.getVertexList().size()).isEqualTo(3),
                () -> assertThat(graphPath.getWeight()).isEqualTo(4)
        );
    }
}
