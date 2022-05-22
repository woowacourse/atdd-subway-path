package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.DomainException;

class PathFinderTest {

    Station station1 = new Station(1L, "station1");
    Station station2 = new Station(2L, "station2");
    Station station3 = new Station(3L, "station3");
    Station station4 = new Station(4L, "station4");

    // given
    Section section1 = new Section(1L, station1, station2, 10);
    Section section2 = new Section(2L, station2, station3, 10);
    Section section3 = new Section(3L, station1, station3, 100);
    List<Section> sections = List.of(section1, section2, section3);
    Section section4 = new Section(4L, station3, station4, 100);
    List<Section> unconnectedSect = List.of(section1, section4);

    @Test
    @DisplayName("구간들로 경로 객체를 생성한다")
    void createPath() {

        assertThatCode(() -> JGraphPathFinder.of(sections))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("두 역의 경로 거리를 반환한다.")
    void calculateDistance() {
        // given
        PathFinder path = JGraphPathFinder.of(sections);

        // when
        int distance = path.calculateDistance(station1, station3);

        // then
        assertThat(distance).isEqualTo(20);
    }

    @Test
    @DisplayName("역이 경로에 포함되지 않는 경우 예외를 던진다.")
    void calculateDistance_notFoundPath_exception() {
        // given
        PathFinder path = JGraphPathFinder.of(sections);

        // then
        assertThatThrownBy(() -> path.calculateDistance(station1, station4))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("두 역의 경로가 연결되지 않은 경우 예외를 던진다.")
    void calculateDistance_notConnectedPath_exception() {
        // given
        PathFinder path = JGraphPathFinder.of(unconnectedSect);

        // then
        assertThatThrownBy(() -> path.calculateDistance(station1, station4))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("두 역의 경로 거리를 반환한다.")
    void calculateStationPath() {
        // given
        PathFinder pathFinder = JGraphPathFinder.of(sections);

        // when
        List<Station> path = pathFinder.calculatePath(station1, station3);

        // then
        assertThat(path.size()).isEqualTo(3);
        assertThat(path).containsExactly(station1, station2, station3);
    }

}
