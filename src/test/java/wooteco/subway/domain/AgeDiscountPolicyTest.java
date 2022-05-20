package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountPolicyTest {

    @DisplayName("나이를 통해 올바른 연령대를 찾는다.")
    @Nested
    class AgesPolicyFindTest {

        @ParameterizedTest(name = "{0} 살은 미취학 아동입니다." )
        @ValueSource(ints = {1, 5})
        void discountPreschoolerAgePolicy(int age) {
            AgeDiscountPolicy agePolicy = AgeDiscountPolicy.findAgePolicy(age);
            assertThat(agePolicy).isEqualTo(AgeDiscountPolicy.PRESCHOOLER);
        }

        @ParameterizedTest(name = "{0} 살은 어린이입니다." )
        @ValueSource(ints = {6, 12})
        void discountChildAgePolicy(int age) {
            AgeDiscountPolicy agePolicy = AgeDiscountPolicy.findAgePolicy(age);
            assertThat(agePolicy).isEqualTo(AgeDiscountPolicy.CHILD);
        }

        @ParameterizedTest(name = "{0} 살은 청소년입니다." )
        @ValueSource(ints = {13, 18})
        void discountTeenagerAgePolicy(int age) {
            AgeDiscountPolicy agePolicy = AgeDiscountPolicy.findAgePolicy(age);
            assertThat(agePolicy).isEqualTo(AgeDiscountPolicy.TEENAGER);
        }

        @ParameterizedTest(name = "{0} 살은 성인입니다." )
        @ValueSource(ints = {19, 50})
        void discountAdultAgePolicy(int age) {
            AgeDiscountPolicy agePolicy = AgeDiscountPolicy.findAgePolicy(age);
            assertThat(agePolicy).isEqualTo(AgeDiscountPolicy.ADULT);
        }
    }

    @DisplayName("연령대별로 정책에 맞게 요금을 할인한다.")
    @Nested
    class AgesFareDiscountTest {

        @ParameterizedTest(name = "미취학 아동은 {0}원인 요금이 할인받아 0원이 됩니다." )
        @ValueSource(ints = {1250, 2150})
        void findPreschoolerFare(int fare) {
            int discountedFare = AgeDiscountPolicy.PRESCHOOLER.fareDiscount(fare);
            assertThat(discountedFare).isEqualTo(0);
        }

        @ParameterizedTest(name = "어린이는 {0}원인 요금이 할인받아 {1}원이 됩니다." )
        @CsvSource(value = {"1250 - 450", "1700 - 675", "2150 - 900"}, delimiterString = " - ")
        void findChildFare(int fare, int expected) {
            int discountedFare = AgeDiscountPolicy.CHILD.fareDiscount(fare);
            assertThat(discountedFare).isEqualTo(expected);
        }

        @ParameterizedTest(name = "청소년은 {0}원인 요금이 할인받아 {1}원이 됩니다." )
        @CsvSource(value = {"1250 - 720", "1700 - 1080", "2150 - 1440"}, delimiterString = " - ")
        void findTeenagerFare(int fare, int expected) {
            int discountedFare = AgeDiscountPolicy.TEENAGER.fareDiscount(fare);
            assertThat(discountedFare).isEqualTo(expected);
        }

        @ParameterizedTest(name = "성인은 {0}원인 요금이 할인받아 {1}원이 됩니다." )
        @CsvSource(value = {"1250 - 1250", "1700 - 1700", "2150 - 2150"}, delimiterString = " - ")
        void findAdultFare(int fare, int expected) {
            int discountedFare = AgeDiscountPolicy.ADULT.fareDiscount(fare);
            assertThat(discountedFare).isEqualTo(expected);
        }
    }
}
