package wooteco.subway.domain.pricing.implement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.pricing.PricingStrategy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AllPricingStrategyTest {

    @Autowired
    @Qualifier("AllPricingStrategy")
    private PricingStrategy strategy;

    @DisplayName("모든 요금계산 전략을 이용해 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource("providePathAndLines")
    void calculateScore(List<Section> path, List<Line> lines, int expectFee) {
        // given
        FareCacluateSpecification specification = new FareCacluateSpecification(path, lines);

        // when
        if (strategy == null) {
            System.out.println("null@@@@@");
        }
        Fare fee = strategy.calculateFare(specification);

        // then
        assertThat(fee.getValue()).isEqualTo(expectFee);
    }

    private static Stream<Arguments> providePathAndLines() {
        return Stream.of(
                Arguments.of(
                        List.of(new Section(1L, 1L, 2L, 10)
                                , new Section(2L, 2L, 3L, 10)
                                , new Section(3L, 3L, 4L, 10)),
                        List.of(new Line(1L, "1호선", "파란색", new Fare(100))
                                , new Line(2L, "2호선", "초록색", new Fare(200))
                                , new Line(3L, "3호선", "빨강색", new Fare(300))),
                        1650 + 300
                ),
                Arguments.of(
                        List.of(new Section(1L, 1L, 2L, 1)
                                , new Section(2L, 2L, 3L, 1)
                                , new Section(1L, 3L, 4L, 1)),
                        List.of(new Line(1L, "1호선", "파란색", new Fare(100))
                                , new Line(2L, "2호선", "초록색", new Fare(200))),
                        1250 + 200
                ),
                Arguments.of(
                        List.of(new Section(1L, 1L, 2L, 25)
                                , new Section(2L, 2L, 3L, 25)
                                , new Section(1L, 3L, 4L, 24)),
                        List.of(new Line(1L, "1호선", "파란색", new Fare(100))
                                , new Line(2L, "2호선", "초록색", new Fare(200))),
                        2350 + 200
                )
        );
    }
}
