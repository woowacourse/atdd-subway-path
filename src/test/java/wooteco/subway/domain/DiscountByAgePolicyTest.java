package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.DiscountByAgePolicy.ADULT_POLICY;
import static wooteco.subway.domain.DiscountByAgePolicy.BABY_POLICY;
import static wooteco.subway.domain.DiscountByAgePolicy.KIDS_POLICY;
import static wooteco.subway.domain.DiscountByAgePolicy.TEENAGER_POLICY;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DiscountByAgePolicyTest {

    @ParameterizedTest
    @DisplayName("나이가 1 이상 6 미만이면 BABY 이다.")
    @ValueSource(ints = {1, 3, 5})
    void Find_MoreThan1LessThan6_BABY(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(BABY_POLICY);
    }

    @ParameterizedTest
    @DisplayName("나이가 6 이상 13 미만이면 KIDS 이다.")
    @ValueSource(ints = {6, 9, 12})
    void Find_MoreThan6LessThan13_KIDS(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(KIDS_POLICY);
    }

    @ParameterizedTest
    @DisplayName("나이가 13 이상 19 미만이면 TEENAGER 이다.")
    @ValueSource(ints = {13, 16, 18})
    void Find_MoreThan13LessThan19_TEENAGER(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(TEENAGER_POLICY);
    }

    @ParameterizedTest
    @DisplayName("나이가 19 이상이면 ADULT 이다.")
    @ValueSource(ints = {19, 27, 56})
    void Find_MoreThan19_ADULT(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(ADULT_POLICY);
    }

    @Test
    @DisplayName("BABY 이면 요금은 0 원이다.")
    void ApplyDiscount_BABY_ZeroFareReturned() {
        //given
        int fare = 1200;
        int expected = 0;

        //when
        int actual = BABY_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("KIDS 이면 운임에서 350원을 공제한 금액의 50% 할인된다.")
    void ApplyDiscount_KIDS_DiscountFareReturned() {
        //given
        int fare = 1200;
        int expected = 425;

        //when
        int actual = KIDS_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("TEENAGER 이면 운임에서 350원을 공제한 금액의 20% 할인된다.")
    void ApplyDiscount_TEENAGER_DiscountFareReturned() {
        //given
        int fare = 1200;
        int expected = 680;

        //when
        int actual = TEENAGER_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("ADULT 이면 운임에서 할인되지 않는다.")
    void ApplyDiscount_ADULT_OriginFareReturned() {
        //given
        int fare = 1200;
        int expected = 1200;

        //when
        int actual = ADULT_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
