package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.strategy.fare.age.AdultStrategy;
import wooteco.subway.domain.strategy.fare.age.ChildStrategy;
import wooteco.subway.domain.strategy.fare.age.InfantStrategy;
import wooteco.subway.domain.strategy.fare.age.TeenAgeStrategy;

class FareAgeStrategyTest {

    @Test
    @DisplayName("영유아는 전체 금액을 할인받는다.")
    void calculateInfant() {
        // given
        InfantStrategy strategy = new InfantStrategy();

        // when
        int discountFare = strategy.discountAge(1350);

        // then
        assertThat(discountFare).isEqualTo(1350);
    }

    @Test
    @DisplayName("유아는 전체 금액에서 350원을 공제한 금액의 50%를 할인받는다.")
    void calculateChild() {
        // given
        ChildStrategy strategy = new ChildStrategy();

        // when
        int discountFare = strategy.discountAge(1350);

        // then
        assertThat(discountFare).isEqualTo(500);
    }

    @Test
    @DisplayName("청소년은 전체 금액에서 350원을 공제한 금액의 20%를 할인받는다.")
    void calculateTeenage() {
        // given
        TeenAgeStrategy strategy = new TeenAgeStrategy();

        // when
        int discountFare = strategy.discountAge(1350);

        // then
        assertThat(discountFare).isEqualTo(200);
    }

    @Test
    @DisplayName("성인은 할인 받는 금액이 없다.")
    void calculateAdult() {
        // given
        AdultStrategy strategy = new AdultStrategy();

        // when
        int discountFare = strategy.discountAge(1350);

        // then
        assertThat(discountFare).isEqualTo(0);
    }
}
