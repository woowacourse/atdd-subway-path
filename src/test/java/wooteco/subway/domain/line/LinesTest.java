package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {

    @DisplayName("노선 중 추가 요금이 가장 비싼 요금을 조회한다.")
    @Test
    void 가장_비싼_요금_조회() {
        List<Line> value = List.of(new Line(1L, "1호선", "blue", 100),
                new Line(2L, "2호선", "green", 200),
                new Line(3L, "3호선", "yellow", 900),
                new Line(4L, "4호선", "red", 0),
                new Line(5L, "5호선", "white", 0)
        );

        Lines lines = new Lines(value);

        assertThat(lines.getMaxExtraFare()).isEqualTo(900);
    }
}
