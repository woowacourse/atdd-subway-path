package wooteco.subway.domain.age;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.age.FareByAgePolicy.ADULT_POLICY;
import static wooteco.subway.domain.age.FareByAgePolicy.BABY_POLICY;
import static wooteco.subway.domain.age.FareByAgePolicy.KIDS_POLICY;
import static wooteco.subway.domain.age.FareByAgePolicy.TEENAGER_POLICY;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Fare;

public class FareByAgePolicyTest {

    @Test
    @DisplayName("BABY 이면 BABY_POLICY 를 찾는다.")
    void Find_MoreThan1LessThan6_BABY() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.from(AgeType.BABY);

        //then
        assertThat(actual).isEqualTo(BABY_POLICY);
    }

    @Test
    @DisplayName("KIDS 이면 KIDS_POLICY 를 찾는다.")
    void Find_MoreThan6LessThan13_KIDS() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.from(AgeType.KIDS);

        //then
        assertThat(actual).isEqualTo(KIDS_POLICY);
    }

    @Test
    @DisplayName("TEENAGER 이면 TEENAGER_POLICY 를 찾는다.")
    void Find_MoreThan13LessThan19_TEENAGER() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.from(AgeType.TEENAGER);

        //then
        assertThat(actual).isEqualTo(TEENAGER_POLICY);
    }

    @Test
    @DisplayName("ADULT 이면 ADULT_POLICY 를 찾는다.")
    void Find_MoreThan19_ADULT() {
        //when
        FareByAgePolicy actual = FareByAgePolicy.from(AgeType.ADULT);

        //then
        assertThat(actual).isEqualTo(ADULT_POLICY);
    }

    @Test
    @DisplayName("BABY 이면 요금은 0 원이다.")
    void ApplyDiscount_BABY_ZeroFareReturned() {
        //given
        Fare fare = new Fare(1200);
        Fare expected = new Fare(0);

        //when
        Fare actual = BABY_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("KIDS 이면 운임에서 350원을 공제한 금액의 50% 할인된다.")
    void ApplyDiscount_KIDS_DiscountFareReturned() {
        //given
        Fare fare = new Fare(1200);
        Fare expected = new Fare(425);

        //when
        Fare actual = KIDS_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("TEENAGER 이면 운임에서 350원을 공제한 금액의 20% 할인된다.")
    void ApplyDiscount_TEENAGER_DiscountFareReturned() {
        //given
        Fare fare = new Fare(1200);
        Fare expected = new Fare(680);

        //when
        Fare actual = TEENAGER_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("ADULT 이면 운임에서 할인되지 않는다.")
    void ApplyDiscount_ADULT_OriginFareReturned() {
        //given
        Fare fare = new Fare(1200);
        Fare expected = new Fare(1200);

        //when
        Fare actual = ADULT_POLICY.applyDiscount(fare);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
