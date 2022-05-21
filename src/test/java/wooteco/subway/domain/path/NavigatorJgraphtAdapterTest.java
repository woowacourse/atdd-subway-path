package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.Navigator;
import wooteco.subway.domain.path.NavigatorJgraphtAdapter;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class NavigatorJgraphtAdapterTest {

    private final Station STATION1 = new Station(1L, "역1");
    private final Station STATION2 = new Station(2L, "역2");
    private final Station STATION3 = new Station(3L, "역3");
    private final Station STATION4 = new Station(4L, "역4");
    private final Station STATION5 = new Station(5L, "역5");
    private final Station STATION6 = new Station(6L, "역6");

    @Test
    void 인접한_두_역_사이가_최단거리인_경우_해당_경로를_그대로_조회() {
        Section closestSection = new Section(STATION2, STATION3, 1);
        List<Section> sections = List.of(closestSection,
                new Section(STATION1, STATION2, 100),
                new Section(STATION2, STATION4, 100),
                new Section(STATION4, STATION5, 100),
                new Section(STATION2, STATION5, 100),
                new Section(STATION5, STATION3, 100),
                new Section(STATION3, STATION6, 100));

        Navigator<Section> navigator = NavigatorJgraphtAdapter.of(STATION2, STATION3, sections);
        List<Section> actual = navigator.calculateShortestPath();
        List<Section> expected = List.of(closestSection);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 인접한_두_역_사이가_최단거리가_아닌_경우_최적의_경로를_계산하여_조회() {
        Section firstSection = new Section(STATION2, STATION4, 2);
        Section secondSection = new Section(STATION4, STATION5, 3);
        Section thirdSection = new Section(STATION5, STATION3, 5);
        List<Section> sections = List.of(
                firstSection, secondSection, thirdSection,
                new Section(STATION1, STATION2, 1),
                new Section(STATION2, STATION3, 100),
                new Section(STATION2, STATION5, 100),
                new Section(STATION3, STATION6, 6));

        Navigator<Section> navigator = NavigatorJgraphtAdapter.of(STATION2, STATION3, sections);
        List<Section> actual = navigator.calculateShortestPath();
        List<Section> expected = List.of(firstSection, secondSection, thirdSection);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 출발점과_도착점이_반대가_되면_지하철역들의_순서만_반대로_반환() {
        Section firstSection = new Section(STATION2, STATION4, 2);
        Section secondSection = new Section(STATION4, STATION5, 3);
        Section thirdSection = new Section(STATION5, STATION3, 5);
        List<Section> sections = List.of(
                firstSection, secondSection, thirdSection,
                new Section(STATION1, STATION2, 1),
                new Section(STATION2, STATION3, 100),
                new Section(STATION2, STATION5, 100),
                new Section(STATION3, STATION6, 6));

        Navigator<Section> navigator1 = NavigatorJgraphtAdapter.of(STATION2, STATION3, sections);
        Navigator<Section> navigator2 = NavigatorJgraphtAdapter.of(STATION3, STATION2, sections);
        List<Section> actual1 = navigator1.calculateShortestPath();
        List<Section> actual2 = navigator2.calculateShortestPath();
        List<Section> expected1 = List.of(firstSection, secondSection, thirdSection);
        List<Section> expected2 = List.of(thirdSection, secondSection, firstSection );

        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    void 도달할_수_없는_경로를_조회하려는_경우_예외_발생() {
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 10),
                new Section(STATION3, STATION4, 20));

        assertThatThrownBy(() -> NavigatorJgraphtAdapter.of(STATION1, STATION3, sections))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
