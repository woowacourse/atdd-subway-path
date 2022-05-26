package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class LineTest {

    @DisplayName("노선의 이름이 null 또는 빈 값이면 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void notAllowNullOrBlankName(String name) {
        assertThatThrownBy(() -> new Line(name, "초록색", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 빈 값일 수 없습니다.");
    }

    @DisplayName("노선의 색상이 null 또는 빈 값이면 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void notAllowNullOrBlankColor(String color) {
        assertThatThrownBy(() -> new Line("2호선", color, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 색상은 빈 값일 수 없습니다.");
    }

    @DisplayName("노선의 추가 요금이 음수일 경우 예외를 반환한다.")
    @Test
    void notAllowExtraFareLessThan0() {
        assertThatThrownBy(() -> new Line("2호선", "초록색", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가 요금은 음수일 수 없습니다.");
    }

}
