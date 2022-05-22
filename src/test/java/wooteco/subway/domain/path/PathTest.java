package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;

public class PathTest {

    @DisplayName("거쳐간 노선 중 가장 추가요금이 비싼 노선의 추가요금을 반환한다.")
    @Test
    void findMostExpensiveExtraFare() {
        Line line = new Line("2호선", "초록색", 0);
        Line expensiveLine = new Line("신분당선", "빨간색", 500);

        Path path = new Path(List.of(), List.of(line, expensiveLine), 1);

        assertThat(path.getMostExpensiveExtraFare()).isEqualTo(500);
    }

}
