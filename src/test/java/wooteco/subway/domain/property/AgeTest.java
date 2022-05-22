package wooteco.subway.domain.property;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AgeTest {
    @Test
    @DisplayName("나이를 입력하지 않은 경우 예외 발생")
    void throwsExceptionWithEmptyAge() {
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> new Age(null))
                .withMessageContaining("나이는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("1 이상의 나이를 입력하지 않은 경우 예외 발생")
    void throwsExceptionWithNotPositive() {
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> new Age(0))
                .withMessageContaining("나이는 0 이하가 될 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource({"12,false", "13,true", "18,true", "10,false"})
    @DisplayName("청소년 여부 반환")
    void isTeenager(int inputAge, boolean expected) {
        Age age = new Age(inputAge);
        assertThat(age.isTeenager()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"5,false", "6,true", "12,true", "13,false"})
    @DisplayName("어린이 여부 반환")
    void isChildren(int inputAge, boolean expected) {
        Age age = new Age(inputAge);
        assertThat(age.isChildren()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"6,false", "5,true"})
    @DisplayName("영유아 여부 반환")
    void isBaby(int inputAge, boolean expected) {
        Age age = new Age(inputAge);
        assertThat(age.isBaby()).isEqualTo(expected);
    }
}
