package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.Color;

class ColorTest {
    @Test
    @DisplayName("색상이 null이면 예외를 반환한다.")
    void color_null() {
        assertThatThrownBy(() -> new Color(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("색상은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("색상이 빈 값이면 예외를 반환한다.")
    void color_empty() {
        assertThatThrownBy(() -> new Color(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("색상은 필수 입력값입니다.");
    }
}
