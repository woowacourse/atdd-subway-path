package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class AgeFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"5, INFANT", "12, CHILD", "18, TEENAGER", "19, ADULT"})
    @DisplayName("나이로 연령타입을 얻을 수 있다.")
    void from(final int age, final AgeFarePolicy expected) {
        AgeFarePolicy ageFarePolicy = AgeFarePolicy.from(age);
        assertThat(ageFarePolicy).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("나이가 0이하이면 에러가 발생한다.")
    void fromThrowException(final int age) {
        assertThatThrownBy(() -> AgeFarePolicy.from(age))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"INFANT, 0", "CHILD, 450", "TEENAGER, 720", "ADULT, 1250"})
    @DisplayName("나이에 따라서 할인된 요금을 계산할 수 있다.")
    void calculateFare(final AgeFarePolicy ageFarePolicy, final int expected) {
        assertThat(ageFarePolicy.calculateFare(1250)).isEqualTo(expected);
    }
}
