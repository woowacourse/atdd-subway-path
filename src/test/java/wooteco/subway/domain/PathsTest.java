package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathsTest {

    @DisplayName("최단 경로와 경로에 따른 거리를 구한다.")
    @Test
    void calculateShortestPath() {
        Station Jamsil = new Station(1L, "잠실역");
        Station Seolleung = new Station(2L, "선릉역");
        Station Gangnam = new Station(3L, "강남역");
        Station Konkuk = new Station(4L, "건대역");
        Station Sagajeong = new Station(5L, "사가정역");

        Section first = new Section(1L, 1L, Jamsil, Seolleung, 3);
        Section second = new Section(2L, 2L, Seolleung, Gangnam, 3);
        Section third = new Section(3L, 2L, Gangnam, Konkuk, 4);
        Section fourth = new Section(4L, 3L, Konkuk, Sagajeong, 5);
        Section fifth = new Section(5L, 4L, Sagajeong, Jamsil, 7);
        Sections sections = new Sections(List.of(first, second, third, fourth, fifth));

        SubwayGraph subwayGraph = new SubwayGraph(sections);
        Paths paths = subwayGraph.createPathsResult(Jamsil, Konkuk);

        assertThat(paths.getStations()).isEqualTo(List.of(Jamsil, Seolleung, Gangnam, Konkuk));
        assertThat(paths.getDistance()).isEqualTo(10);
    }
}
