package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:0", "5:950", "13:1520", "19:2250"},
            delimiter = ':')
    void calculateFare(int age, int expected) {
        Path path = new Path(List.of(1L, 2L, 3L, 4L), 46, Set.of(1L, 2L));

        assertThat(path.calculateFare(age, 200)).isEqualTo(expected);
    }
}
