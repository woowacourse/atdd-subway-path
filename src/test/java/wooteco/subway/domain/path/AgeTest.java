package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgeTest {

    @DisplayName("음수 값으로 나이를 생성하면 예외가 발생한다.")
    @Test
    void construct_negative() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Age(-1))
                .withMessageContaining("음수");
    }
}
