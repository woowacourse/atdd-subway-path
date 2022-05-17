package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathManagerTest {

    private final Station STATION1 = new Station(1L, "역1");
    private final Station STATION2 = new Station(2L, "역2");
    private final Station STATION3 = new Station(3L, "역3");
    private final Station STATION4 = new Station(4L, "역4");
    private final Station STATION5 = new Station(5L, "역5");
    private final Station STATION6 = new Station(6L, "역6");

    @DisplayName("calculateOptimalPath 메서드는 두 지하철역 사이의 최단거리와 그 경로 정보를 계산하여_반환")
    @Nested
    class CalculateOptimalPathTest {

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
            PathManager pathManager = PathManager.of(sections);

            Path actual = pathManager.calculateOptimalPath(STATION2, STATION3);
            Path expected = new Path(1, List.of(STATION2, STATION3));

            assertThat(actual).isEqualTo(expected);
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
            PathManager pathManager = PathManager.of(sections);

            Path actual = pathManager.calculateOptimalPath(STATION2, STATION3);
            Path expected = new Path(10, List.of(STATION2, STATION4, STATION5, STATION3));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 노선에_구간으로_등록되지_않은_역에_대한_경로를_조회하려는_경우_예외_발생() {
            Station nonRegisteredStation = new Station(999L, "등록되지 않은 역");
            List<Section> sections = List.of(new Section(STATION1, STATION2, 10),
                    new Section(STATION2, STATION3, 100),
                    new Section(STATION3, STATION4, 20));
            PathManager pathManager = PathManager.of(sections);

            assertThatThrownBy(() -> pathManager.calculateOptimalPath(STATION1, nonRegisteredStation))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 출발점과_도착점이_동일한_경우_예외_발생() {
            List<Section> sections = List.of(new Section(STATION1, STATION2, 10));
            PathManager pathManager = PathManager.of(sections);

            assertThatThrownBy(() -> pathManager.calculateOptimalPath(STATION1, STATION1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 도달할_수_없는_경로를_조회하려는_경우_예외_발생() {
            List<Section> sections = List.of(new Section(STATION1, STATION2, 10),
                    new Section(STATION3, STATION4, 20));
            PathManager pathManager = PathManager.of(sections);

            assertThatThrownBy(() -> pathManager.calculateOptimalPath(STATION1, STATION3))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
