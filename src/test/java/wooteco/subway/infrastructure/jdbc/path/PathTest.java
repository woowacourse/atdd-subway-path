package wooteco.subway.infrastructure.jdbc.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.Path;

class PathTest {

    @DisplayName("거리에 따른 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250"})
    void calculateFare(long distance, long expected) {
        Path path = new Path(Collections.emptyList(), distance);

        Fare actual = path.calculateFare();
        assertThat(actual.getFare()).isEqualTo(expected);
    }
}
