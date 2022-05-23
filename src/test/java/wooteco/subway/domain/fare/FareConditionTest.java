package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.SubwayException;

class FareConditionTest {

    @DisplayName("나이가 0보다 적을 경우 예외가 발생한다.")
    @Test
    void validateAge() {
        assertThatThrownBy(() -> new FareCondition(10, -1, 100))
                .isInstanceOf(SubwayException.class)
                .hasMessage("[ERROR] 나이는 양수로 입력하세요");
    }

}
