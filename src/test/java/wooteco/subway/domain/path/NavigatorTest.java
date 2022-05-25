package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class NavigatorTest {

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 역삼역 = new Station(2L, "역삼역");
    private static final Station 잠실역 = new Station(3L, "잠실역");
    private static final Station 선릉역 = new Station(4L, "선릉역");
    private static final Station 양재역 = new Station(5L, "양재역");
    private static final Station 청계산입구역 = new Station(6L, "청계산입구역");

    @Test
    void 인접한_두_역_사이가_최단거리인_경우_해당_경로를_그대로_조회() {
        Section 역삼_잠실_가까운_구간 = new Section(역삼역, 잠실역, 1);
        List<Section> sections = List.of(역삼_잠실_가까운_구간,
                new Section(강남역, 역삼역, 100),
                new Section(역삼역, 선릉역, 100),
                new Section(선릉역, 양재역, 100),
                new Section(역삼역, 양재역, 100),
                new Section(양재역, 잠실역, 100),
                new Section(잠실역, 청계산입구역, 100));

        Navigator navigator = new Navigator(sections);
        List<Section> actual = navigator.calculateShortestPath(역삼역, 잠실역);
        List<Section> expected = List.of(역삼_잠실_가까운_구간);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 인접한_두_역_사이가_최단거리가_아닌_경우_최적의_경로를_계산하여_조회() {
        Section 역삼_선릉 = new Section(역삼역, 선릉역, 2);
        Section 선릉_양재 = new Section(선릉역, 양재역, 3);
        Section 양재_잠실 = new Section(양재역, 잠실역, 5);
        List<Section> sections = List.of(역삼_선릉, 선릉_양재, 양재_잠실,
                new Section(강남역, 역삼역, 1),
                new Section(역삼역, 잠실역, 100),
                new Section(역삼역, 양재역, 100),
                new Section(잠실역, 청계산입구역, 6));

        Navigator navigator = new Navigator(sections);
        List<Section> actual = navigator.calculateShortestPath(역삼역, 잠실역);
        List<Section> expected = List.of(역삼_선릉, 선릉_양재, 양재_잠실);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 출발점과_도착점이_반대가_되면_지하철역들의_순서만_반대로_반환() {
        Section 역삼_선릉 = new Section(역삼역, 선릉역, 2);
        Section 선릉_양재 = new Section(선릉역, 양재역, 3);
        Section 양재_잠실 = new Section(양재역, 잠실역, 5);
        List<Section> sections = List.of(역삼_선릉, 선릉_양재, 양재_잠실,
                new Section(강남역, 역삼역, 1),
                new Section(역삼역, 잠실역, 100),
                new Section(역삼역, 양재역, 100),
                new Section(잠실역, 청계산입구역, 6));

        Navigator navigator = new Navigator(sections);
        List<Section> actual = navigator.calculateShortestPath(역삼역, 잠실역);
        List<Section> actualReversed = navigator.calculateShortestPath(잠실역, 역삼역);
        List<Section> expected = List.of(역삼_선릉, 선릉_양재, 양재_잠실);
        List<Section> expectedReversed = List.of(양재_잠실, 선릉_양재, 역삼_선릉);

        assertThat(actual).isEqualTo(expected);
        assertThat(actualReversed).isEqualTo(expectedReversed);
    }

    @Test
    void 도달할_수_없는_경로를_조회하려는_경우_예외_발생() {
        List<Section> sections = List.of(
                new Section(강남역, 역삼역, 10), new Section(잠실역, 선릉역, 20));

        Navigator navigator = new Navigator(sections);
        assertThatThrownBy(() -> navigator.calculateShortestPath(강남역, 잠실역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
