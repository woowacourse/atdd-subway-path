package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.utils.DijkstraShortestPathStation;

public class PathTest {

    @DisplayName("최단 거리를 구한다.")
    @Test
    void getShortestDistance() {
        // given
        Station station1 = new Station(1L, "name1");
        Station station2 = new Station(2L, "name2");
        Station station3 = new Station(3L, "name3");
        Section section1 = new Section(station1, station2, 10);
        Section section2 = new Section(station2, station3, 10);
        Section section3 = new Section(station1, station3, 10);
        Sections sections = new Sections(List.of(section1, section2, section3));

        // when
        Path path = DijkstraShortestPathStation.getPath(
                sections,
                station1,
                station2
        );

        // then
        assertThat(path.getDistance()).isEqualTo(10);
    }

    @DisplayName("최단 경로를 구한다.")
    @Test
    void getShortestPath() {
        // given
        Station station1 = new Station(1L, "name1");
        Station station2 = new Station(2L, "name2");
        Station station3 = new Station(3L, "name3");
        Section section1 = new Section(station1, station2, 10);
        Section section2 = new Section(station2, station3, 10);
        Section section3 = new Section(station1, station3, 10);
        Sections sections = new Sections(List.of(section1, section2, section3));

        // when
        Path path = DijkstraShortestPathStation.getPath(
                sections,
                station1,
                station2
        );

        // then
        assertThat(path.getStations())
                .hasSize(2)
                .containsExactly(station1, station3);
    }
}
