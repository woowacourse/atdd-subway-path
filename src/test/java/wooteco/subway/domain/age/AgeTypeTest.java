package wooteco.subway.domain.age;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.domain.age.AgeType.ADULT;
import static wooteco.subway.domain.age.AgeType.BABY;
import static wooteco.subway.domain.age.AgeType.KIDS;
import static wooteco.subway.domain.age.AgeType.TEENAGER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.IllegalInputException;

public class AgeTypeTest {

    @ParameterizedTest
    @DisplayName("나이가 1 이상 6 미만이면 BABY 이다.")
    @ValueSource(ints = {1, 3, 5})
    void Find_MoreThan1LessThan6_BABY(int age) {
        //when
        AgeType actual = AgeType.from(age);

        //then
        assertThat(actual).isEqualTo(BABY);
    }

    @ParameterizedTest
    @DisplayName("나이가 6 이상 13 미만이면 KIDS 이다.")
    @ValueSource(ints = {6, 9, 12})
    void Find_MoreThan6LessThan13_KIDS(int age) {
        //when
        AgeType actual = AgeType.from(age);

        //then
        assertThat(actual).isEqualTo(KIDS);
    }

    @ParameterizedTest
    @DisplayName("나이가 13 이상 19 미만이면 TEENAGER 이다.")
    @ValueSource(ints = {13, 16, 18})
    void Find_MoreThan13LessThan19_TEENAGER(int age) {
        //when
        AgeType actual = AgeType.from(age);

        //then
        assertThat(actual).isEqualTo(TEENAGER);
    }

    @ParameterizedTest
    @DisplayName("나이가 19 이상이면 ADULT 이다.")
    @ValueSource(ints = {19, 27, 56})
    void Find_MoreThan19_ADULT(int age) {
        //when
        AgeType actual = AgeType.from(age);

        //then
        assertThat(actual).isEqualTo(ADULT);
    }

    @ParameterizedTest
    @DisplayName("해당하는 나이 유형이 없는 경우 예외를 던진다.")
    @ValueSource(ints = {-70, -1})
    void Find_InvalidAge_ExceptionThrown(int age) {
        //when, then
        assertThatThrownBy(() -> AgeType.from(age))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("해당하는 나이 유형을 찾을 수 없습니다.");
    }
}
