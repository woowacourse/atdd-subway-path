package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @DisplayName("구간의 노선이 null일 경우 예외를 반환한다.")
    @Test
    void notAllowNullLine() {
        Station upStation = new Station("강남역");
        Station downStation = new Station("역삼역");
        assertThatThrownBy(() -> new Section(null, upStation, downStation, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간의 노선은 null일 수 없습니다.");
    }

    @DisplayName("구간의 시점 또는 종점이 null일 경우 예외를 반환한다.")
    @Test
    void notAllowNullStation() {
        assertThatThrownBy(() -> new Section(new Line("2호선", "초록색", 0), null, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간의 시작과 끝은 null일 수 없습니다.");
    }

    @DisplayName("구간의 거리가 1보다 작을 경우 예외를 반환한다.")
    @Test
    void notAllowDistanceLessThan1() {
        Station upStation = new Station("강남역");
        Station downStation = new Station("역삼역");
        assertThatThrownBy(() -> new Section(new Line("2호선", "초록색", 0), upStation, downStation, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간 거리는 1보다 작을 수 없습니다.");
    }

    @DisplayName("구간의 시점과 종점이 같은 역이면 예외를 반환한다.")
    @Test
    void sameEndpoint() {
        Station station = new Station("강남역");
        assertThatThrownBy(() -> new Section(new Line("2호선", "초록색", 0), station, station, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간의 시작과 끝은 같은 역일 수 없습니다.");
    }

}
