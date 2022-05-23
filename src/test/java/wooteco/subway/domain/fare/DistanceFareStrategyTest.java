package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DistanceFareStrategyTest {
    @DisplayName("거리에 따른 요금을 계산한다.")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"9,1250", "12,1350", "16,1450", "58,2150"})
    void calculate(String distance, String resultFare) {
        DistanceFareStrategy distanceFareStrategy = new DistanceFareStrategy();
        assertThat(distanceFareStrategy.calculate(Integer.parseInt(distance))).isEqualTo(Integer.parseInt(resultFare));
    }
}
