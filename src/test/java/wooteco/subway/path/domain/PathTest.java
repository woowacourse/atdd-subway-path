package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

class PathTest {

    private List<PathEdge> pathEdges;
    private Station stationA, stationB, stationC;

    @BeforeEach
    void setUp() {
        stationA = new Station(1L, "stationA");
        stationB = new Station(2L, "stationB");
        stationC = new Station(3L, "stationC");
        Section sectionAB = new Section(1L, stationA, stationB, 5);
        Section sectionBC = new Section(2L, stationB, stationC, 3);
        Sections sections = new Sections(Arrays.asList(sectionAB, sectionBC));
        Line line = new Line(1L, "line", "green lighten-1", sections);

        pathEdges = Arrays.asList(
            new PathEdge(sectionAB, line),
            new PathEdge(sectionBC, line)
        );
    }

    @DisplayName("구간이 없는 경로는 생성에 실패한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void creationFailed(List<PathEdge> pathEdges) {
        // when, then
        assertThatThrownBy(() -> new Path(pathEdges))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("구간이 없는 경로는 생성할 수 없습니다");
    }

    @DisplayName("구간에 속해있는 역 정보를 가져온다.")
    @Test
    void toStations() {
        // when
        List<Station> stations = new Path(pathEdges).toStations();

        // then
        assertThat(stations).containsExactly(stationA, stationB, stationC);
    }

    @DisplayName("구간의 거리를 가져온다.")
    @Test
    void toDistance() {
        // when
        int distance = new Path(pathEdges).toDistance();

        // then
        assertThat(distance).isEqualTo(8);
    }
}