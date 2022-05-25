package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("계산 전략을 순서대로 적용해서 요금 객체 생성")
    void calculateOf() {
        // given
        Fare fare = new Fare(0L).chargeOf(x -> x + 1);
        // when
        Long value = fare.getValue();
        // then
        assertThat(value).isEqualTo(1);
    }
}
