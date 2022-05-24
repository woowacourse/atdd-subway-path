package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.NegativeFareException;

class FareTest {
    @DisplayName("금액이 음수라면 예외가 발생한다.")
    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void constructor_throwsExceptionIfValueIsNegative(int value) {
        // when & then
        assertThatThrownBy(() -> new Fare(value))
                .isInstanceOf(NegativeFareException.class);
    }
}
