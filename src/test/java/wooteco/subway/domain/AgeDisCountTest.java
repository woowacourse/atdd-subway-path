package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.AgeDisCount.BABY;
import static wooteco.subway.domain.AgeDisCount.CHILDREN;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDisCountTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 5})
    @DisplayName("0살부터 5살까지는 BABY다")
    void ageDiscountBaby(final int age) {
        assertThat(AgeDisCount.from(age)).isEqualTo(BABY);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    @DisplayName("6살부터 12살까지는 CHILDREN이다")
    void ageDiscountChildren(final int age) {
        assertThat(AgeDisCount.from(age)).isEqualTo(CHILDREN);
    }
}
