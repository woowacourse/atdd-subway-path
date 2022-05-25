package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @DisplayName("100 미만의 추가 금액으로 Line을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_exception_extraFareLowerThanMinValue() {
        assertThatThrownBy(() -> new Line("2호선", "green", 99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최소 추가 금액은 100원입니다.");
    }
}
