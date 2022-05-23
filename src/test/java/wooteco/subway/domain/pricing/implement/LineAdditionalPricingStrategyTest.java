package wooteco.subway.domain.pricing.implement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.pricing.PricingStrategy;

class LineAdditionalPricingStrategyTest {

    @DisplayName("경로에 포함된 역의 추가 가격 중 최대값을 반환한다.")
    @ParameterizedTest
    @MethodSource("providePathAndLines")
    void calculateFee(List<Section> path, List<Line> lines, int expectFee) {
        // given
        PricingStrategy strategy = new LineAdditionalPricingStrategy();
        FareCacluateSpecification specification = new FareCacluateSpecification(path, lines);

        // when
        int fee = strategy.calculateFee(specification);

        // then
        assertThat(fee).isEqualTo(expectFee);
    }

    private static Stream<Arguments> providePathAndLines() {
        return Stream.of(
                Arguments.of(
                        List.of(new Section(1L, 1L, 2L, 10)
                        , new Section(2L, 2L, 3L, 10)
                        , new Section(3L, 3L, 4L, 10)),
                        List.of(new Line(1L, "1호선", "파란색", 100)
                        , new Line(2L, "2호선", "초록색", 200)
                        , new Line(3L, "3호선", "빨강색", 300)),
                        300
                ),
                Arguments.of(
                        List.of(new Section(1L, 1L, 2L, 10)
                                , new Section(2L, 2L, 3L, 10)
                                , new Section(1L, 3L, 4L, 10)),
                        List.of(new Line(1L, "1호선", "파란색", 100)
                                , new Line(2L, "2호선", "초록색", 200)),
                        200
                )
        );
    }

}
