package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {

    @DisplayName("lineIds를 이용하여 lines중 가장 비싼 추가 요금을 찾는다.")
    @Test
    void findMaxExtraFare() {
        Lines lines = new Lines(List.of(new Line(1L, "1호선", "blue", 2),
                new Line(2L, "2호선", "green", 1),
                new Line(3L, "3호선", "orange", 4),
                new Line(4L, "4호선", "black", 3)));

        List<Long> lineIds = List.of(1L, 2L, 3L, 4L);

        int maxExtraFare = lines.findMaxExtraFare(lineIds);

        assertThat(maxExtraFare).isEqualTo(4);
    }

}