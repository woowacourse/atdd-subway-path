package wooteco.subway.domain.fare.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeTest {

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {0, 151})
    @DisplayName("나이는 0이하 151이상으로 생성될 경우 예외를 발생한다.")
    void outOfRangeAge(int value) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Age(value))
                .withMessage("나이는 1이상 150이하여야 합니다. value : " + value);
    }

    @Test
    @DisplayName("나이가 같으면 동등한 객체다.")
    void equalAge() {
        assertThat(new Age(20)).isEqualTo(new Age(20));
    }

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {5, 65})
    @DisplayName("유아 또는 노인인지 확인한다.")
    void isOldOrBaby(int age) {
        assertThat(new Age(age).isOldOrBaby()).isTrue();
    }

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {6, 12})
    @DisplayName("어린이인지 확인한다.")
    void isChildren(int age) {
        assertThat(new Age(age).isChildren()).isTrue();
    }

    @ParameterizedTest(name = "나이 : {0}")
    @ValueSource(ints = {13, 18})
    @DisplayName("청소년인지 확인한다.")
    void isTeenager(int age) {
        assertThat(new Age(age).isTeenager()).isTrue();
    }
}
