package wooteco.subway.domain.property;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.InvalidRequestException;

class AgeTest {
    @Test
    @DisplayName("나이는 음수일 수 없다.")
    public void throwsExceptionWithNotPositiveValue() {
        // given & when
        int value = -1;
        // then
        assertThatExceptionOfType(InvalidRequestException.class)
            .isThrownBy(() -> new Age(value));
    }

    @Test
    @DisplayName("13세는 청소년이다.")
    public void isTeenagerWith13() {
        // given
        Age age = new Age(13);
        // when
        final boolean isTeenager = age.isTeenager();
        // then
        assertThat(isTeenager).isTrue();
    }

    @Test
    @DisplayName("18세는 청소년이다.")
    public void isTeenagerWith18() {
        // given
        Age age = new Age(18);
        // when
        final boolean isTeenager = age.isTeenager();
        // then
        assertThat(isTeenager).isTrue();
    }

    @Test
    @DisplayName("12세는 어린이이다.")
    public void isChildWith12() {
        // given
        Age age = new Age(12);
        // when
        final boolean isChild = age.isChild();
        // then
        assertThat(isChild).isTrue();
    }

    @Test
    @DisplayName("6세는 어린이이다.")
    public void isChildWith6() {
        // given
        Age age = new Age(6);
        // when
        final boolean isChild = age.isChild();
        // then
        assertThat(isChild).isTrue();
    }
}