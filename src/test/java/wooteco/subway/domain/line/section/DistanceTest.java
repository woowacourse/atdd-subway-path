package wooteco.subway.domain.line.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("지하철노선 거리")
class DistanceTest {

    @DisplayName("거리는 양수여야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-5, 0})
    void validateDistancePositive(int distance) {
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는 양수여야 합니다.");
    }

    @DisplayName("거리를 비교한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,2,false", "2,2,false", "2,1,true"})
    void isLongerThan(long source, long target, boolean expected) {
        Distance sourceDistance = new Distance(source);
        Distance targetDistance = new Distance(target);

        boolean actual = sourceDistance.isLongerThan(targetDistance);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("거리의 차를 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,2,1", "2,1,1"})
    void subtract(long source, long target, long expected) {
        Distance sourceDistance = new Distance(source);
        Distance targetDistance = new Distance(target);

        long actual =sourceDistance.subtract(targetDistance).getDistance();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("거리의 합을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,2,3", "2,1,3"})
    void sum(long source, long target, long expected) {
        Distance sourceDistance = new Distance(source);
        Distance targetDistance = new Distance(target);

        long actual =sourceDistance.sum(targetDistance).getDistance();
        assertThat(actual).isEqualTo(expected);
    }
}
