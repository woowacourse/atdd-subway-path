package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.PositiveDigitException;

class LineTest {

    @DisplayName("추가 요금이 0보다 작은 경우 예외를 발생시킨다.")
    @Test
    void NegativeExtraFareException() {
        assertThatThrownBy(() -> new Line("2호선", "green", -1))
                .isInstanceOf(PositiveDigitException.class)
                .hasMessage("추가 요금이 0보다 작습니다.");
    }

}