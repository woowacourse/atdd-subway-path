package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgePolicyTest {

    @Test
    @DisplayName("아기 요금을 계산한다.")
    void calculate_baby() {
        AgePolicy agePolicy = new AgePolicy(3);
        assertThat(agePolicy.calculate(1250)).isEqualTo(0);
    }

    @Test
    @DisplayName("어린이 요금을 계산한다.")
    void calculate_child() {
        AgePolicy agePolicy_6 = new AgePolicy(12);
        AgePolicy agePolicy_12 = new AgePolicy(12);

        assertThat(agePolicy_6.calculate(1250)).isEqualTo(450);
        assertThat(agePolicy_12.calculate(1250)).isEqualTo(450);
    }

    @Test
    @DisplayName("청소년 요금을 계산한다.")
    void calculate_teenager() {
        AgePolicy agePolicy_18 = new AgePolicy(18);
        AgePolicy agePolicy_19 = new AgePolicy(19);

        assertThat(agePolicy_18.calculate(1250)).isEqualTo(720);
        assertThat(agePolicy_19.calculate(1250)).isEqualTo(1250);
    }
}
