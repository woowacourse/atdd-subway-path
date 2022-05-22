package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDiscountPolicyTest {

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {13, 18})
    @DisplayName("청소년 할인 정책을 조회한다.")
    void teenagerDiscountPolicy(int age) {
        AgeDiscountPolicy policy = AgeDiscountPolicy.find(age);

        assertThat(policy).isEqualTo(AgeDiscountPolicy.TEENAGER);
    }

    @ParameterizedTest(name = "일반 요금 : {0}, 청소년 할인 요금 : {1}")
    @CsvSource({"1250, 720", "1350, 800", "1450, 880", "1650, 1040", "2750, 1920", "3650, 2640"})
    @DisplayName("청소년 요금을 계산한다.")
    void teenagerFare(int fare, int result) {
        assertThat(AgeDiscountPolicy.TEENAGER.calculate(fare)).isEqualTo(result);
    }
}
