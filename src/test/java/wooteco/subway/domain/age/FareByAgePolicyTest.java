package wooteco.subway.domain.age;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.age.FareByAgePolicy.ADULT_POLICY;
import static wooteco.subway.domain.age.FareByAgePolicy.BABY_POLICY;
import static wooteco.subway.domain.age.FareByAgePolicy.KIDS_POLICY;
import static wooteco.subway.domain.age.FareByAgePolicy.TEENAGER_POLICY;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareByAgePolicyTest {

    @Test
    @DisplayName("BABY 이면 BABY_POLICY 를 찾는다.")
    void Find_MoreThan1LessThan6_BABY() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.find(Age.BABY);

        //then
        assertThat(actual).isEqualTo(BABY_POLICY);
    }

    @Test
    @DisplayName("KIDS 이면 KIDS_POLICY 를 찾는다.")
    void Find_MoreThan6LessThan13_KIDS() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.find(Age.KIDS);

        //then
        assertThat(actual).isEqualTo(KIDS_POLICY);
    }

    @Test
    @DisplayName("TEENAGER 이면 TEENAGER_POLICY 를 찾는다.")
    void Find_MoreThan13LessThan19_TEENAGER() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.find(Age.TEENAGER);

        //then
        assertThat(actual).isEqualTo(TEENAGER_POLICY);
    }

    @Test
    @DisplayName("ADULT 이면 ADULT_POLICY 를 찾는다.")
    void Find_MoreThan19_ADULT() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.find(Age.ADULT);

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
