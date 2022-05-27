package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
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
            /*
            전체 호선 : 1호선, 2호선, 3호선, 4호선
            1호선 구간 정보 : 1번역 - 2번역 - 3번역 - 5번역
            2호선 구간 정보 : 2번역 - 4번역 - 5번역
            3호선 구간 정보 : 2번역 - 5번역
            4호선 구간 정보 : 3번역 - 6번역
            */
            List<Section> sections = List.of(
                    new Section(1L, STATION1, STATION2, 100),
                    new Section(1L, STATION2, STATION3, 1),
                    new Section(2L, STATION2, STATION4, 100),
                    new Section(2L, STATION4, STATION5, 100),
                    new Section(3L, STATION2, STATION5, 100),
                    new Section(1L, STATION5, STATION3, 100),
                    new Section(4L, STATION3, STATION6, 100));

            Map<Long, Integer> lineExtraFares = Map.of(
                    1L, 10,
                    2L, 15,
                    3L, 10,
                    4L, 70);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            Path actual = pathManager.calculateOptimalPath(STATION2, STATION3);
            Path expected = new Path(1, List.of(STATION2, STATION3), 10);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 인접한_두_역_사이가_최단거리가_아닌_경우_최적의_경로를_계산하여_조회() {
            /*
            전체 호선 : 1호선, 2호선, 3호선, 4호선
            1호선 구간 정보 : 1번역 - 2번역 - 3번역 - 5번역
            2호선 구간 정보 : 2번역 - 4번역 - 5번역
            3호선 구간 정보 : 2번역 - 5번역
            4호선 구간 정보 : 3번역 - 6번역
            */
            List<Section> sections = List.of(
                    new Section(1L, STATION1, STATION2, 1),
                    new Section(1L, STATION2, STATION3, 100),
                    new Section(2L,STATION2, STATION4, 2),
                    new Section(2L,STATION4, STATION5, 3),
                    new Section(3L,STATION2, STATION5, 100),
                    new Section(1L,STATION5, STATION3, 5),
                    new Section(4L,STATION3, STATION6, 6));

            Map<Long, Integer> lineExtraFares = Map.of(
                    1L, 10,
                    2L, 15,
                    3L, 10,
                    4L, 70);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            Path actual = pathManager.calculateOptimalPath(STATION2, STATION3);
            Path expected = new Path(10, List.of(STATION2, STATION4, STATION5, STATION3), 15);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 노선에_구간으로_등록되지_않은_역에_대한_경로를_조회하려는_경우_예외_발생() {
            /*
            전체 호선 : 1호선
            1호선 구간 정보 : 1번역 - 2번역 - 3번역 - 4번역
            */
            Station nonRegisteredStation = new Station(999L, "등록되지 않은 역");
            List<Section> sections = List.of(
                    new Section(1L,STATION1, STATION2, 10),
                    new Section(1L,STATION2, STATION3, 100),
                    new Section(1L,STATION3, STATION4, 20));

            Map<Long, Integer> lineExtraFares = Map.of(1L, 10);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            assertThatThrownBy(() -> pathManager.calculateOptimalPath(STATION1, nonRegisteredStation))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 출발점과_도착점이_동일한_경우_예외_발생() {
            /*
            전체 호선 : 1호선
            1호선 구간 정보 : 1번역 - 2번역
            */
            List<Section> sections = List.of(
                    new Section(1L, STATION1, STATION2, 10));

            Map<Long, Integer> lineExtraFares = Map.of(1L, 10);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            assertThatThrownBy(() -> pathManager.calculateOptimalPath(STATION1, STATION1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 도달할_수_없는_경로를_조회하려는_경우_예외_발생() {
            /*
            전체 호선 : 1호선, 2호선
            1호선 구간 정보 : 1번역 - 2번역
            2호선 구간 정보 : 3번역 - 4번역
            */
            List<Section> sections = List.of(
                    new Section(1L, STATION1, STATION2, 10),
                    new Section(2L,STATION3, STATION4, 20));

            Map<Long, Integer> lineExtraFares = Map.of(
                    1L, 10,
                    2L, 15);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            assertThatThrownBy(() -> pathManager.calculateOptimalPath(STATION1, STATION3))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 최단거리_경로가_2개이상이지만_유일한_최소비용경로가_존재하는_경우() {
            /*
            전체 호선 : 1호선, 2호선, 3호선, 4호선, 5호선, 6호선
            1호선 구간 정보 : 2번역 - 4번역
            2호선 구간 정보 : 4번역 - 5번역
            3호선 구간 정보 : 3번역 - 5번역
            4호선 구간 정보 : 1번역 - 2번역 - 5번역
            5호선 구간 정보 : 2번역 - 3번역 - 6번역
            6호선 구간 정보 : 2번역 - 4번역
            */
            List<Section> sections = List.of(
                    new Section(4L, STATION1, STATION2, 1),
                    new Section(5L, STATION2, STATION3, 100),
                    new Section(1L, STATION2, STATION4, 2),
                    new Section(2L, STATION4, STATION5, 3),
                    new Section(4L, STATION2, STATION5, 100),
                    new Section(3L, STATION5, STATION3, 5),
                    new Section(6L, STATION2, STATION4, 2),
                    new Section(5L, STATION3, STATION6, 6));

            Map<Long, Integer> lineExtraFares = Map.of(
                    1L, 10,
                    2L, 15,
                    3L, 10,
                    4L, 70,
                    5L, 100,
                    6L, 30);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            Path actual = pathManager.calculateOptimalPath(STATION2, STATION3);
            Path expected = new Path(10, List.of(STATION2, STATION4, STATION5, STATION3), 15);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 최소비용_경로가_2개이상이지만_유일한_최단경로가_존재하는_경우() {
            /*
            전체 호선 : 1호선, 3호선
            1호선 구간 정보 : 1번역 - 2번역 - 4번역
            3호선 구간 정보 : 1번역 - 3번역 - 4번역
            */
            List<Section> sections = List.of(
                    new Section(1L, STATION1, STATION2, 100),
                    new Section(1L, STATION2, STATION4, 100),
                    new Section(3L, STATION1, STATION3, 1),
                    new Section(3L, STATION3, STATION4, 1));

            Map<Long, Integer> lineExtraFares = Map.of(
                    1L, 10,
                    3L, 10);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            Path actual = pathManager.calculateOptimalPath(STATION1, STATION4);
            Path expected = new Path(2, List.of(STATION1, STATION3, STATION4), 10);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 도중에_노선이_바뀔_때_추가요금이_최대값으로만_바뀌는지_테스트() {
            /*
            전체 호선 : 1호선, 2호선, 3호선
            1호선 구간 정보 : 1번역 - 2번역 - 3번역
            2호선 구간 정보 : 3번역 - 4번역
            3호선 구간 정보 : 4번역 - 5번역 - 6번역
            */
            List<Section> sections = List.of(
                    new Section(1L, STATION1, STATION2, 1),
                    new Section(1L, STATION2, STATION3, 1),
                    new Section(2L, STATION3, STATION4, 1),
                    new Section(3L, STATION4, STATION5, 1),
                    new Section(3L, STATION5, STATION6, 1));

            Map<Long, Integer> lineExtraFares = Map.of(
                    1L, 10,
                    2L, 70,
                    3L, 30);
            PathManager pathManager = PathManager.of(GraphGenerator.toAdjacentPath(sections, lineExtraFares));

            Path actual = pathManager.calculateOptimalPath(STATION1, STATION6);
            Path expected = new Path(5, List.of(STATION1, STATION2, STATION3, STATION4, STATION5, STATION6), 70);

            assertThat(actual).isEqualTo(expected);
        }
    }
}
