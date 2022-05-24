package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class PathTest {

    @DisplayName("지나간 역 목록을 null로 생성하려고 하면 예외를 반환한다.")
    @Test
    void notAllowNullStations() {
        assertThatThrownBy(() -> new Path(null, List.of(), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 역들은 null일 수 없습니다.");
    }

    @DisplayName("지나간 역 목록이 null을 포함하면 예외를 반환한다.")
    @Test
    void notAllowContainNullStation() {
        List<Station> containsNull = new ArrayList<>();
        containsNull.add(null);
        assertThatThrownBy(() -> new Path(containsNull, List.of(), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 역들은 null일 수 없습니다.");
    }

    @DisplayName("지나간 노선 목록을 null로 생성하려고 하면 예외를 반환한다.")
    @Test
    void notAllowNullLines() {
        assertThatThrownBy(() -> new Path(List.of(), null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 노선들은 null일 수 없습니다.");
    }

    @DisplayName("지나간 역 목록이 null을 포함하면 예외를 반환한다.")
    @Test
    void notAllowContainNullLine() {
        List<Line> containsNull = new ArrayList<>();
        containsNull.add(null);
        assertThatThrownBy(() -> new Path(List.of(), containsNull, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 노선들은 null일 수 없습니다.");
    }

    @DisplayName("경로의 거리가 1보다 작을 경우 예외를 반환한다.")
    @Test
    void notAllowDistanceLessThan1() {
        assertThatThrownBy(() -> new Path(List.of(), List.of(), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로 거리는 1보다 작을 수 없습니다.");
    }

    @DisplayName("거쳐간 노선 중 가장 추가요금이 비싼 노선의 추가요금을 반환한다.")
    @Test
    void findMostExpensiveExtraFare() {
        Line line = new Line("2호선", "초록색", 0);
        Line expensiveLine = new Line("신분당선", "빨간색", 500);

        Path path = new Path(List.of(), List.of(line, expensiveLine), 1);

        assertThat(path.getMostExpensiveExtraFare()).isEqualTo(500);
    }

}
