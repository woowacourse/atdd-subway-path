package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.policy.distance.BasePolicy;

class FareTest {

    @Test
    @DisplayName("10키로 이하시 기본요금을 계산한다.")
    void getFare_under10km() {
        assertThat(getFare(0)).isEqualTo(1250);
        assertThat(getFare(9)).isEqualTo(1250);
        assertThat(getFare(10)).isEqualTo(1250);
    }

    private int getFare(int distance) {
        return new Fare(new ArrayList<>(), getBaseFare(distance)).getFare();
    }

    private BasePolicy getBaseFare(int distance) {
        return PolicyFactory.createBase(distance);
    }

    @Test
    @DisplayName("50키로 이하시 기본요금을 계산한다.")
    void getFare_over10km() {
        assertThat(getFare(12)).isEqualTo(1350);
        assertThat(getFare(50)).isEqualTo(2050);
    }

    @Test
    @DisplayName("50키로 초과시 기본요금을 계산한다.")
    void getFare_over50km() {
        assertThat(getFare(51)).isEqualTo(2150);
        assertThat(getFare(58)).isEqualTo(2150);
        assertThat(getFare(65)).isEqualTo(2250);
        assertThat(getFare(66)).isEqualTo(2250);
        assertThat(getFare(67)).isEqualTo(2350);
    }

    @Test
    @DisplayName("연령별 요금을 계산한다.")
    void getFare_addAge() {
        assertThat(new Fare(List.of(PolicyFactory.createAgeDiscount(3)), getBaseFare(9)).getFare()).isEqualTo(0);
        assertThat(new Fare(List.of(PolicyFactory.createAgeDiscount(12)), getBaseFare(9)).getFare()).isEqualTo(450);
        assertThat(new Fare(List.of(PolicyFactory.createAgeDiscount(15)), getBaseFare(9)).getFare()).isEqualTo(720);
        assertThat(new Fare(List.of(PolicyFactory.createAgeDiscount(22)), getBaseFare(9)).getFare()).isEqualTo(1250);
    }
}
