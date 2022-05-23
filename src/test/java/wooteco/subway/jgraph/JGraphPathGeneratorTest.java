package wooteco.subway.jgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DomainException;

class JGraphPathGeneratorTest {

    JGraphPathGenerator pathGenerator;

    Station station1 = new Station(1L, "station1");
    Station station2 = new Station(2L, "station2");
    Station station3 = new Station(3L, "station3");
    Station station4 = new Station(4L, "station4");

    Line line1 = new Line(1L, "1호선", "blue", 100L);
    Line line2 = new Line(2L, "2호선", "red", 200L);
    Line line3 = new Line(3L, "3호선", "green", 300L);
    Line line4 = new Line(4L, "4호선", "white", 400L);

    // given
    Section section1 = new Section(line1, station1, station2, 10);
    Section section2 = new Section(line2, station2, station3, 10);
    Section section3 = new Section(line3, station1, station3, 100);
    List<Section> sections = List.of(section1, section2, section3);
    Section section4 = new Section(line4, station3, station4, 100);
    List<Section> unconnectedSect = List.of(section1, section4);

    @BeforeEach
    void setUp() {
        pathGenerator = new JGraphPathGenerator();
    }

    @Test
    @DisplayName("두 역의 경로 거리를 반환한다.")
    void calculateDistance() {
        // when
        Path path = pathGenerator.findPath(sections, station1, station3);

        // then
        assertThat(path.getDistance()).isEqualTo(20);
    }

    @Test
    @DisplayName("역이 경로에 포함되지 않는 경우 예외를 던진다.")
    void calculateDistance_notFoundPath_exception() {
        assertThatThrownBy(() -> pathGenerator.findPath(sections, station1, station4))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("두 역의 경로가 연결되지 않은 경우 예외를 던진다.")
    void calculateDistance_notConnectedPath_exception() {
        assertThatThrownBy(() -> pathGenerator.findPath(unconnectedSect, station1, station4))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("두 역의 경로 거리를 반환한다.")
    void calculateStationPath() {
        // given
        Path path = pathGenerator.findPath(sections, station1, station3);

        // when
        List<Station> stations = path.getStations();

        // then
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations).containsExactly(station1, station2, station3);
    }

    @Test
    @DisplayName("추가 요금이 드는 구간을 지나는 경우를 경로에 적용할 수 있다.")
    void calculatePath_extraFare() {
        // given
        Path path = pathGenerator.findPath(sections, station1, station3);

        // when
        long extraFare = path.getExtraFare();

        // then
        assertThat(extraFare).isEqualTo(line2.getExtraFare());
    }
}
