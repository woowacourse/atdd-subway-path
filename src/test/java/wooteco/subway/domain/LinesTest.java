package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LinesTest {

    @Test
    @DisplayName("가장 요금이 비싼 라인의 요금을 반환한다.")
    void mostExpensiveLineFare() {
        Lines lines = new Lines(Set.of(new Line(1L, "name", "color", 100),
                new Line(2L, "name2", "color2", 200)));

        assertThat(lines.mostExpensiveLineFare()).isEqualTo(200);
    }
}
