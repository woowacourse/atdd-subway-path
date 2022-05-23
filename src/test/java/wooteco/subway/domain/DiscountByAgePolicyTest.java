package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DiscountByAgePolicyTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    @DisplayName("나이가 1 이상 6 미만이면 BABY 이다.")
    void Find_MoreThan1LessThan6_BABY(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(DiscountByAgePolicy.BABY_POLICY);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 9, 12})
    @DisplayName("나이가 6 이상 13 미만이면 KIDS 이다.")
    void Find_MoreThan6LessThan13_KIDS(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(DiscountByAgePolicy.KIDS_POLICY);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 16, 18})
    @DisplayName("나이가 13 이상 19 미만이면 TEENAGER 이다.")
    void Find_MoreThan13LessThan19_TEENAGER(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(DiscountByAgePolicy.TEENAGER_POLICY);
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 27, 56})
    @DisplayName("나이가 19 이상이면 ADULT 이다.")
    void Find_MoreThan19_ADULT(int age) {
        //when
        DiscountByAgePolicy actual = DiscountByAgePolicy.find(age);

        //then
        assertThat(actual).isEqualTo(DiscountByAgePolicy.ADULT_POLICY);
    }
}
