package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ExtraFareTest {

    @DisplayName("요금은 음수가 될 수 없다.")
    @Test
    void validateFareNotNegative() {
        assertThatThrownBy(() -> new ExtraFare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 음수가 될 수 없습니다.");
    }

    @DisplayName("요금은 0보다 커야 한다.")
    @ParameterizedTest
    @ValueSource(longs = {0, 1, 10})
    void construct(long fare) {
        assertDoesNotThrow(() -> new ExtraFare(fare));
    }
}