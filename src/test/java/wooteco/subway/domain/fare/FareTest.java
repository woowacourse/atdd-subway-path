package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("공제 금액과 할인율이 주어지면 할인된 금액을 반환한다.")
    void discount() {
        Fare fare = new Fare(1000);

        assertThat(fare.discount(100, 0.1)).isEqualTo(new Fare(910));
    }
}
