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
        Section sectionAB = new Section(1L, stationA, stationB, 5);
        Section sectionBC = new Section(2L, stationB, stationC, 3);

        return new Line(
            1L, "lineA", "green lighten-1",
            new Sections(Arrays.asList(sectionAB, sectionBC))
        );
    }

    private Line createLineB() {
        Section sectionBD = new Section(3L, stationB, stationD, 1);
        Section sectionDC = new Section(4L, stationD, stationC, 1);

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
}
