package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    Station station1 = new Station(1L, "station1");
    Station station2 = new Station(2L, "station2");
    Station station3 = new Station(3L, "station3");

    // given
    Section section1 = new Section(1L, station1, station2, 10);
    Section section2 = new Section(2L, station2, station3, 10);
    Section section3 = new Section(3L, station1, station3, 100);

    List<Section> sections = List.of(section1, section2, section3);

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
}
