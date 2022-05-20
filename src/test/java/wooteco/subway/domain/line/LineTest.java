package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("추가요금이 음수가 들어올 경우 예외 발생")
    void createLineExceptionByNegativeExtraFare() {
        assertThatThrownBy(() -> new Line(1L, "2호선", "green", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가요금은 음수가 들어올 수 없습니다.");
    }
}
