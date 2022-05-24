package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {
    @Test
    @DisplayName("노선 이름이 빈 값일 때 예외를 발생시킨다.")
    void invalidLineName() {
        assertThatThrownBy(() -> new Line("", "green"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값일 수 없습니다.");
    }

    @Test
    @DisplayName("노선 색상이 빈 값일 때 예외를 발생시킨다.")
    void invalidLineColor() {
        assertThatThrownBy(() -> new Line("2호선", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("색상은 빈 값일 수 없습니다.");
    }
}
