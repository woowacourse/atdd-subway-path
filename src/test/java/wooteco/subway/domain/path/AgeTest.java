package wooteco.subway.domain.path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.Age;

public class AgeTest {

    @Test
    @DisplayName("나이가 양수가 아니면 예외를 발생시킨다.")
    void validateNegativeNumber() {
        Assertions.assertThatThrownBy(() -> new Age(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이는 양수여야 합니다.");
    }
}
