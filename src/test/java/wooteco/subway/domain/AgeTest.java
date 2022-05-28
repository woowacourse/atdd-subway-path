package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeTest {

    @DisplayName("1 보다 적은 값으로 Age를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void a() {
        int invalidAgeValue = 0;

        assertThatThrownBy(() -> new Age(invalidAgeValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이는 1살 보다 어릴 수 없습니다.");
    }
}
