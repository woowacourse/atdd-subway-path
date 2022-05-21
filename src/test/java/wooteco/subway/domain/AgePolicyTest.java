package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AgePolicyTest {

    @DisplayName("나이에 맞는 AgePolicy가 반환된다.")
    @ParameterizedTest(name = "{displayName} : {0}살은 {1}이다")
    @MethodSource("fromAgeTestSet")
    void fromAge(int age, AgePolicy agePolicy) {
        assertThat(AgePolicy.fromAge(age)).isEqualTo(agePolicy);
    }

    public static Stream<Arguments> fromAgeTestSet() {
        return Stream.of(
                Arguments.of(1, AgePolicy.BABY),
                Arguments.of(5, AgePolicy.BABY),
                Arguments.of(6, AgePolicy.CHILDREN),
                Arguments.of(12, AgePolicy.CHILDREN),
                Arguments.of(13, AgePolicy.TEENAGER),
                Arguments.of(18, AgePolicy.TEENAGER),
                Arguments.of(19, AgePolicy.ADULT)
        );
    }

    @DisplayName("나이에 맞는 할인율이 적용된다.")
    @ParameterizedTest(name = "{displayName} : {0}는 {1}->{2}원으로 할인된다")
    @MethodSource("getDiscountedFareTestSet")
    void getDiscountedFare(AgePolicy agePolicy, int originFare, int discountedFare) {
        assertThat(agePolicy.getDiscountedFare(originFare)).isEqualTo(discountedFare);
    }

    public static Stream<Arguments> getDiscountedFareTestSet() {
        return Stream.of(
                Arguments.of(AgePolicy.BABY, 1350, 0),
                Arguments.of(AgePolicy.CHILDREN, 1350, 500),
                Arguments.of(AgePolicy.TEENAGER, 1350, 800),
                Arguments.of(AgePolicy.ADULT, 1350, 1350)
        );
    }
}
