package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.domain.FeeException;

public class FareTest {

    private final FareFactory fareFactory = new FareFactory();

    @Test
    @DisplayName("거리가 0 이하인 경우 예외가 발생한다.")
    void fee_underMinimum_exception() {
        // given
        assertThatThrownBy(() -> fareFactory.getFare(0))
                .isInstanceOf(FeeException.class);
    }
}
