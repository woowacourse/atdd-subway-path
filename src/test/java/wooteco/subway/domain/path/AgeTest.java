package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeTest {

    @DisplayName("대상 나이보다 낮은지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,2,true", "2,2,false", "2,1,false"})
    void isLowerThan(long source, long target, boolean expected) {
        Age age = new Age(source);
        assertThat(age.isLowerThan(target)).isEqualTo(expected);
    }

    @DisplayName("대상 나이보다 크거나 같은지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,2,false", "2,2,true", "2,1,true"})
    void isSameOrHigherThan(long source, long target, boolean expected) {
        Age age = new Age(source);
        assertThat(age.isSameOrHigherThan(target)).isEqualTo(expected);
    }
}