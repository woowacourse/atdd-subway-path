package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.domain.pricing.PricingStrategy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DistanceProportionalPricingStrategyTest {

    @Autowired
    @Qualifier("DistanceProportional")
    private PricingStrategy strategy;

    @DisplayName("요금 계산하기")
    @ParameterizedTest(name = "{0} km -> 요금 {1}원 예상")
    @CsvSource(value = {"5, 1250", "10, 1250", "11, 1350", "15, 1350", "16, 1450",
            "50, 2050", "51, 2150", "58, 2150", "59, 2250"})
    void calculateScore2(int distance, int expected) {
        // given
        Line line = new Line(1L, "2호선", "초록색", 0);
        List<Section> sections = List.of(new Section(1L, 1L, 1L, 2L, distance));

        // when
        int result = strategy.calculateFee(new FareCacluateSpecification(sections, List.of(line)));

        // then
        assertThat(result).isEqualTo(expected);
    }
}
