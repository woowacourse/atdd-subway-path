package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.NotNegativeFareException;

class FareTest {

    @Test
    @DisplayName("요금이 음수라면 예외가 발생한다.")
    void createWithNegativeFare() {
        assertThatThrownBy(() -> new Fare(-1000))
                .isInstanceOf(NotNegativeFareException.class)
                .hasMessageContaining("지하철 요금이 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("공제 금액과 할인율이 주어지면 할인된 금액을 반환한다.")
    void discount() {
        Fare fare = new Fare(1000);

        assertThat(fare.discount(100, 0.1)).isEqualTo(new Fare(910));
    }
}
