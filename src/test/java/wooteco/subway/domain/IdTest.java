package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("식별자")
class IdTest {

    @DisplayName("식별자는 양수여야 한다.")
    @ParameterizedTest
    @ValueSource(longs = {-5, 0})
    void validateIdPositive(long id) {
        assertThatThrownBy(() -> new Id(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("식별자는 양수여야 합니다.");
    }

    @DisplayName("식별자가 지정되지 않으면 임시값을 할당한다.")
    @Test
    void createWithoutId() {
        long temporaryId = Id.temporary().getId();
        assertThat(temporaryId).isEqualTo(0L);
    }

    @DisplayName("식별자끼리 비교한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,1,true", "1,2,false"})
    void equalsWithSameType(long id1, long id2, boolean expected) {
        Id thisId = new Id(id1);
        Id otherId = new Id(id2);

        boolean actual = thisId.equals(otherId);
        assertThat(actual).isEqualTo(expected);
    }
}
