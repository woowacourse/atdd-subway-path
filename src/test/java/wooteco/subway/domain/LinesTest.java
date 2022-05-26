package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LinesTest {

    @DisplayName("중복된 노선들이 들어오면 예외를 반환한다.")
    @Test
    void notAllowDuplicateStations() {
        Line line = new Line("2호선", "초록색", 0);

        assertThatThrownBy(() -> new Lines(List.of(line, line)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 목록에 중복된 노선이 있습니다.");
    }

    @DisplayName("생성자에 null이 들어오면 예외를 반환한다.")
    @Test
    void notAllowNullValues() {
        assertThatThrownBy(() -> new Lines(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 목록은 null일 수 없습니다.");
    }

    @DisplayName("역 목록에 null이 포함되면 예외를 반환한다.")
    @Test
    void notAllowNullStation() {
        List<Line> values = new ArrayList<>();
        values.add(null);

        assertThatThrownBy(() -> new Lines(values))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 목록은 null을 포함할 수 없습니다.");
    }

    @DisplayName("노선들 중 가장 추가요금이 비싼 노선의 추가요금을 반환한다.")
    @Test
    void findMostExpensiveExtraFare() {
        Line line = new Line("2호선", "초록색", 0);
        Line expensiveLine = new Line("신분당선", "빨간색", 500);

        Lines lines = new Lines(List.of(line, expensiveLine));

        assertThat(lines.getMostExpensiveExtraFare()).isEqualTo(500);
    }

}
