package wooteco.subway.domain.farestrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeStrategyTest {

    @ParameterizedTest
    @DisplayName("나이에 따른 계산")
    @CsvSource({"1350,19,1350", "1350,13,800", "1350,6,500", "1350,1,0"})
    void calculate(long fare, int age, long expect) {
        AgeStrategy strategy = new AgeStrategy(age);
        long actual = strategy.calculate(fare);
        assertThat(actual).isEqualTo(expect);
    }
}
