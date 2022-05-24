package wooteco.subway.domain.distance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KilometerTest {

    @DisplayName("거리가 음수이면 예외를 발생시킨다.")
    @Test
    void validateNegativeNumber() {
        assertThatThrownBy(() -> Kilometer.from(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수여야 합니다.");
    }
}
