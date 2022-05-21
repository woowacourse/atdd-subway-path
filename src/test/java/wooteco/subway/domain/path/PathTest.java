package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    private final Station STATION1 = new Station(1L, "역1");
    private final Station STATION2 = new Station(2L, "역2");
    private final Station STATION3 = new Station(3L, "역3");
    private final Station STATION4 = new Station(4L, "역4");
    private final Station STATION5 = new Station(5L, "역5");
    private final Station STATION6 = new Station(6L, "역6");

    @Test
    void 인접한_두_역_사이가_최단거리인_경우_해당_경로를_그대로_조회() {
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 100),
                new Section(STATION2, STATION3, 1),
                new Section(STATION2, STATION4, 100),
                new Section(STATION4, STATION5, 100),
                new Section(STATION2, STATION5, 100),
                new Section(STATION5, STATION3, 100),
                new Section(STATION3, STATION6, 100));

        Path path = Path.of(STATION2, STATION3, sections);

        assertThat(path.toStations()).containsExactly(STATION2, STATION3);
        assertThat(path.calculateDistance()).isEqualTo(1);
    }

    @Test
    void 인접한_두_역_사이가_최단거리가_아닌_경우_최적의_경로를_계산하여_조회() {
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 1),
                new Section(STATION2, STATION3, 100),
                new Section(STATION2, STATION4, 2),
                new Section(STATION4, STATION5, 3),
                new Section(STATION2, STATION5, 100),
                new Section(STATION5, STATION3, 5),
                new Section(STATION3, STATION6, 6));

        Path path = Path.of(STATION2, STATION3, sections);

        assertThat(path.toStations()).containsExactly(STATION2, STATION4, STATION5, STATION3);
        assertThat(path.calculateDistance()).isEqualTo(10);
    }

    @Test
    void 출발점과_도착점이_반대가_되면_지하철역들의_순서만_반대로_반환() {
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 1),
                new Section(STATION2, STATION3, 100),
                new Section(STATION2, STATION4, 2),
                new Section(STATION4, STATION5, 3),
                new Section(STATION2, STATION5, 100),
                new Section(STATION5, STATION3, 5),
                new Section(STATION3, STATION6, 6));

        Path path1 = Path.of(STATION2, STATION3, sections);
        Path path2 = Path.of(STATION3, STATION2, sections);

        assertThat(path1.toStations()).containsExactly(STATION2, STATION4, STATION5, STATION3);
        assertThat(path2.toStations()).containsExactly(STATION3, STATION5, STATION4, STATION2);
        assertThat(path1.calculateDistance()).isEqualTo(path2.calculateDistance());
    }

    @Test
    void 노선에_구간으로_등록되지_않은_역에_대한_경로를_조회하려는_경우_예외_발생() {
        Station nonRegisteredStation = new Station(999L, "등록되지 않은 역");
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 10),
                new Section(STATION2, STATION3, 100),
                new Section(STATION3, STATION4, 20));

        assertThatThrownBy(() -> Path.of(STATION1, nonRegisteredStation, sections))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출발점과_도착점이_동일한_경우_예외_발생() {
        List<Section> sections = List.of(new Section(STATION1, STATION2, 10));

        assertThatThrownBy(() -> Path.of(STATION1, STATION1, sections))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 도달할_수_없는_경로를_조회하려는_경우_예외_발생() {
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 10),
                new Section(STATION3, STATION4, 20));

        assertThatThrownBy(() -> Path.of(STATION1, STATION3, sections))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
