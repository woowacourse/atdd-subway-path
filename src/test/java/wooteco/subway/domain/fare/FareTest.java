package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {
    private final Fare fare = new Fare();

    @Test
    @DisplayName("10키로 이하시 기본요금을 계산한다.")
    void getFare_under10km() {
        assertThat(fare.getFare(0)).isEqualTo(1250);
        assertThat(fare.getFare(9)).isEqualTo(1250);
        assertThat(fare.getFare(10)).isEqualTo(1250);
    }

    @Test
    @DisplayName("50키로 이하시 기본요금을 계산한다.")
    void getFare_over10km() {
        assertThat(fare.getFare(12)).isEqualTo(1350);
        assertThat(fare.getFare(50)).isEqualTo(2050);
    }

    @Test
    @DisplayName("50키로 초과시 기본요금을 계산한다.")
    void getFare_over50km() {
        assertThat(fare.getFare(51)).isEqualTo(2150);
        assertThat(fare.getFare(58)).isEqualTo(2150);
        assertThat(fare.getFare(65)).isEqualTo(2250);
        assertThat(fare.getFare(66)).isEqualTo(2250);
        assertThat(fare.getFare(67)).isEqualTo(2350);
    }

    @Test
    @DisplayName("연령별 요금을 계산한다.")
    void getFare_addAge() {
        assertThat(new Fare(List.of(new AgePolicy(3))).getFare(9)).isEqualTo(0);
        assertThat(new Fare(List.of(new AgePolicy(12))).getFare(9)).isEqualTo(450);
        assertThat(new Fare(List.of(new AgePolicy(15))).getFare(9)).isEqualTo(720);
        assertThat(new Fare(List.of(new AgePolicy(22))).getFare(9)).isEqualTo(1250);
    }
}
