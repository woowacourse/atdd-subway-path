package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AgeGroupTest {

    @DisplayName("각 연령대를 확인한다.")
    @ParameterizedTest(name ="age = {0}")
    @MethodSource("checkAgeGroupArgument")
    public void checkAgeGroup(int age, AgeGroup ageGroup) {
        assertEquals(AgeGroup.from(age), ageGroup);
    }

    public static Stream<Arguments> checkAgeGroupArgument() {
        return Stream.of(
                Arguments.of(6, AgeGroup.CHILDREN),
                Arguments.of(12, AgeGroup.CHILDREN),
                Arguments.of(13, AgeGroup.TEENAGER),
                Arguments.of(18, AgeGroup.TEENAGER),
                Arguments.of(19, AgeGroup.ADULT),
                Arguments.of(20, AgeGroup.ADULT),
                Arguments.of(100, AgeGroup.ADULT)
        );
    }

    @DisplayName("나이가 음수이면 에러가 발생한다.")
    @Test
    public void checkNegativeAge() {
        // then
        assertThatThrownBy(() -> AgeGroup.from(-1)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일치하는 연령대가 존재하지 않습니다.");

    }

    @DisplayName("각 연령대 별 요금을 확인한다.")
    @ParameterizedTest(name = "기본요금 = 2350, ageGroup = {0}, 최종 요금 = {1}")
    @MethodSource("checkFareArgument")
    public void checkFare(AgeGroup ageGroup, int resultFare) {
        //given
        int fare = 2350;

        // then
        assertEquals(ageGroup.getDiscountedValue(fare), resultFare);
    }


    public static Stream<Arguments> checkFareArgument() {
        return Stream.of(
                Arguments.of(AgeGroup.BABY, 0),
                Arguments.of(AgeGroup.CHILDREN, 1350),
                Arguments.of(AgeGroup.TEENAGER, 1950),
                Arguments.of(AgeGroup.ADULT, 2350)
        );
    }
}
