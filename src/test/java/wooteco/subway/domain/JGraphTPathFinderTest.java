package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.SubwayFixtures.강남에서_역삼_구간;
import static wooteco.subway.SubwayFixtures.강남역;
import static wooteco.subway.SubwayFixtures.대림에서_서초_구간;
import static wooteco.subway.SubwayFixtures.대림역;
import static wooteco.subway.SubwayFixtures.서초에서_강남_구간;
import static wooteco.subway.SubwayFixtures.선릉에서_성담빌딩_구간;
import static wooteco.subway.SubwayFixtures.선릉역;
import static wooteco.subway.SubwayFixtures.성담빌딩;
import static wooteco.subway.SubwayFixtures.압구정역;
import static wooteco.subway.SubwayFixtures.역삼에서_선릉_구간;
import static wooteco.subway.SubwayFixtures.역삼역;
import static wooteco.subway.SubwayFixtures.청담역;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.JGraphTPathFinder;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.exception.notfound.NotFoundPathException;
import wooteco.subway.exception.notfound.NotFoundStationInSectionException;

class JGraphTPathFinderTest {

    private final PathFinder pathFinder = new JGraphTPathFinder();
    private List<Section> sections = List.of(
            역삼에서_선릉_구간, 강남에서_역삼_구간, 서초에서_강남_구간, 대림에서_서초_구간, 선릉에서_성담빌딩_구간
    );

    @DisplayName("역삼-선릉, 강남-역삼, 서초-강남, 대림-서초, 선릉-성담 구간 에서 강남-성담빌딩 경로조회를 시도하면 강남-역삼-선릉-성담빌딩 이 나온다")
    @Test
    void findShortedPath() {
        // given
        final List<Station> expectedStations = List.of(강남역, 역삼역, 선릉역, 성담빌딩);

        // when
        final Path path = pathFinder.searchShortestPath(new Sections(sections), 강남역, 성담빌딩);
        final long distance = path.getDistance();
        final List<Station> stations = path.getStations();

        // then
        assertAll(
                () -> assertThat(distance).isEqualTo(
                        Stream.of(강남에서_역삼_구간, 역삼에서_선릉_구간, 선릉에서_성담빌딩_구간)
                                .mapToLong(Section::getDistance)
                                .sum()),
                () -> assertThat(stations).isEqualTo(expectedStations)
        );
    }

    @DisplayName("역삼-선릉, 강남-역삼, 서초-강남, 대림-서초, 선릉-성담 구간 에서 성담빌딩-강남 경로조회를 시도하면 성담빌딩-선릉-역삼-강남 이 나온다")
    @Test
    void findShortedPathReverse() {
        // given
        final long expectedDistanceSum = Stream.of(강남에서_역삼_구간, 역삼에서_선릉_구간, 선릉에서_성담빌딩_구간)
                .mapToLong(Section::getDistance)
                .sum();
        final List<Station> expectedStations = List.of(성담빌딩, 선릉역, 역삼역, 강남역);

        final Path path = pathFinder.searchShortestPath(new Sections(sections), 성담빌딩, 강남역);

        // when
        final long distance = path.getDistance();
        final List<Station> stations = path.getStations();

        // then
        assertAll(
                () -> assertThat(distance).isEqualTo(expectedDistanceSum),
                () -> assertThat(stations).isEqualTo(expectedStations)
        );
    }

    @DisplayName("경로 조회 요청 시, 출발지 또는 도착지 지하철역이 포함된 구간이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void pathFindingShouldFailIfSourceOrTargetDoesNotExists() {
        // given
        final Station source = 청담역;
        final Station target = 압구정역;

        // when & then
        assertThatThrownBy(() -> pathFinder.searchShortestPath(new Sections(sections), source, target))
                .isInstanceOf(NotFoundStationInSectionException.class);
    }

    @DisplayName("경로 조회 요청 시, 출발지 부터 도착지 까지 연결된 구간이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void pathFindingShouldFailIfSourceToTargetIsNotLinked() {
        // given
        final List<Section> sections = List.of(대림에서_서초_구간, 서초에서_강남_구간, 역삼에서_선릉_구간);
        final Station source = 대림역;
        final Station target = 선릉역;

        // when & then
        assertThatThrownBy(() -> pathFinder.searchShortestPath(new Sections(sections), source, target))
                .isInstanceOf(NotFoundPathException.class);
    }
}
