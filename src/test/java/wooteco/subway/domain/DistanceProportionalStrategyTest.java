package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DistanceProportionalStrategyTest {

    @DisplayName("요금 계산하기")
    @ParameterizedTest(name = "{0} km -> 요금 {1}원 예상")
    @CsvSource(value = {"5,1250", "44,1950", "60,2250"})
    void calculateScore2(int distance, int expected) {
        // given
        List<Section> sections = List.of(new Section(1L, 1L, 1L, 2L, distance));
        FeeStrategy strategy = new DistanceProportionalStrategy();

        // when
        int result = strategy.calculateFee(sections);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
