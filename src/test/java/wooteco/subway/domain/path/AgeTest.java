package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AgeTest {

    @DisplayName("음수 값으로 나이를 생성하면 예외가 발생한다.")
    @Test
    void construct_negative() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Age(-1))
                .withMessageContaining("음수");
    }

    @ParameterizedTest(name = "나이가 {0}살 이상이면 {1}를 반환한다")
    @CsvSource({"12, true", "13, true", "14, false"})
    void isSameOrBiggerThan(int comparing, boolean expected) {
        Age age = new Age(13);
        assertThat(age.isSameOrBiggerThan(comparing)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "나이가 {0}살 미만이면 {1}를 반환한다")
    @CsvSource({"12, false", "13, false", "14, true"})
    void isSmallerThan(int comparing, boolean expected) {
        Age age = new Age(13);
        assertThat(age.isSmallerThan(comparing)).isEqualTo(expected);
    }
}
