package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {
    @DisplayName("거리가 0이하라면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void constructor_throwsExceptionIfValueIsLessThanOne(int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0이하가 될 수 없습니다.");
    }
}
