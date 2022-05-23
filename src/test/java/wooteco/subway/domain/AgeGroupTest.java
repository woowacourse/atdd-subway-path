package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgeGroupTest {

    @DisplayName("각 연령대를 확인한다.")
    @Test
    public void checkChildren() {
        // given
        AgeGroup ageGroup1 = AgeGroup.from(6);
        AgeGroup ageGroup2 = AgeGroup.from(12);
        AgeGroup ageGroup3 = AgeGroup.from(13);
        AgeGroup ageGroup4 = AgeGroup.from(18);
        AgeGroup ageGroup5 = AgeGroup.from(19);
        AgeGroup ageGroup6 = AgeGroup.from(20);
        AgeGroup ageGroup7 = AgeGroup.from(100);

        // when & then
        assertAll(
                () -> assertEquals(ageGroup1, AgeGroup.CHILDREN),
                () -> assertEquals(ageGroup2, AgeGroup.CHILDREN),
                () -> assertEquals(ageGroup3, AgeGroup.TEENAGER),
                () -> assertEquals(ageGroup4, AgeGroup.TEENAGER),
                () -> assertEquals(ageGroup5, AgeGroup.ADULT),
                () -> assertEquals(ageGroup6, AgeGroup.ADULT),
                () -> assertEquals(ageGroup7, AgeGroup.ADULT)
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
    @Test
    public void checkFare() {
        //given
        int fare = 2350;
        final AgeGroup baby = AgeGroup.BABY;
        final AgeGroup children = AgeGroup.CHILDREN;
        final AgeGroup teenager = AgeGroup.TEENAGER;
        final AgeGroup adult = AgeGroup.ADULT;

        // then
        assertAll(
                () -> assertEquals(baby.getDiscountedValue(fare), 0),
                () -> assertEquals(children.getDiscountedValue(fare), 1350),
                () -> assertEquals(teenager.getDiscountedValue(fare), 1950),
                () -> assertEquals(adult.getDiscountedValue(fare), 2350)
        );
    }

}
