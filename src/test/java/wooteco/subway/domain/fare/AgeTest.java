package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeTest {

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 적절한 Age를 반환한다. - 어린이")
    @ValueSource(ints = {6, 12})
    void from_child(int age) {
        assertThat(Age.from(age)).isEqualTo(Age.CHILD);
    }

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 적절한 Age를 반환한다. - 청소년")
    @ValueSource(ints = {13, 18})
    void from_adolescent(int age) {
        assertThat(Age.from(age)).isEqualTo(Age.ADOLESCENT);
    }

    @Test
    @DisplayName("나이 입력에 대해 적절한 Age를 반환한다. - 일반")
    void from_others() {
        int age = 19;
        assertThat(Age.from(age)).isEqualTo(Age.OTHERS);
    }
}