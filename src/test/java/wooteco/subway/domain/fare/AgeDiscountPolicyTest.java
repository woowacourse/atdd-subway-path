package wooteco.subway.domain.fare;

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

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {6, 12})
    @DisplayName("어린이 할인 정책을 조회한다.")
    void childrenDiscountPolicy(int age) {
        AgeDiscountPolicy policy = AgeDiscountPolicy.find(age);

        assertThat(policy).isEqualTo(AgeDiscountPolicy.CHILDREN);
    }

    @ParameterizedTest(name = "일반 요금 : {0}, 어린이 할인 요금 : {1}")
    @CsvSource({"1250, 450", "1350, 500", "1450, 550", "1650, 650", "2750, 1200", "3650, 1650"})
    @DisplayName("청소년 요금을 계산한다.")
    void childrenFare(int fare, int result) {
        assertThat(AgeDiscountPolicy.CHILDREN.calculate(fare)).isEqualTo(result);
    }

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {1, 5, 65})
    @DisplayName("무임 요금 정책을 조회한다.")
    void freeDiscountPolicy(int age) {
        AgeDiscountPolicy policy = AgeDiscountPolicy.find(age);

        assertThat(policy).isEqualTo(AgeDiscountPolicy.FREE);
    }
}
