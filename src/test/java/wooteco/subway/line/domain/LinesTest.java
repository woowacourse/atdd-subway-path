package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

class LinesTest {

    private Station stationA, stationB, stationC, stationD;
    private Section sectionAB, sectionBC, sectionBD, sectionDC;
    private Lines lines;

    @BeforeEach
    void setUp() {
        stationA = new Station(1L, "stationA");
        stationB = new Station(2L, "stationB");
        stationC = new Station(3L, "stationC");
        stationD = new Station(4L, "stationD");
        lines = new Lines(
            Arrays.asList(createLineA(), createLineB())
        );
    }

    private Line createLineA() {
        sectionAB = new Section(1L, stationA, stationB, 5);
        sectionBC = new Section(2L, stationB, stationC, 3);

        return new Line(
            1L, "lineA", "green lighten-1",
            new Sections(Arrays.asList(sectionAB, sectionBC))
        );
    }

    private Line createLineB() {
        sectionBD = new Section(3L, stationB, stationD, 1);
        sectionDC = new Section(4L, stationD, stationC, 1);

        return new Line(
            2L, "lineB", "black lighten-1",
            new Sections(Arrays.asList(sectionBD, sectionDC))
        );
    }

    @DisplayName("전체 노선에서 모든 역을 무작위 순서로 가져온다.")
    @Test
    void toDistinctStations() {
        // when
        Set<Station> stations = lines.toDistinctStations();

        // then
        assertThat(stations).containsExactlyInAnyOrder(stationA, stationB, stationC, stationD);
    }

    @DisplayName("전체 노선에서 모든 구간을 무작위 순서로 가져온다.")
    @Test
    void toAllSections() {
        // when
        Set<Section> sections = lines.toAllSections();

        // then
        assertThat(sections).containsExactlyInAnyOrder(sectionAB, sectionBC, sectionBD, sectionDC);
    }

    @DisplayName("전체 노선에서 특정 구간을 포함하는 노선을 알아낸다.")
    @Test
    void findLineBySectionContaining() {
        // when
        Line line = lines.findLineBySectionContaining(sectionAB);

        // then
        assertThat(line).usingRecursiveComparison()
            .isEqualTo(createLineA());
    }
}
