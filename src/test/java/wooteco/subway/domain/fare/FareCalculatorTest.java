package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.fare.ageStrategy.AgeDiscountPolicy;
import wooteco.subway.domain.fare.ageStrategy.ChildrenDiscountPolicy;
import wooteco.subway.domain.fare.ageStrategy.NormalAgeDiscountPolicy;
import wooteco.subway.domain.fare.ageStrategy.PreferentialDiscountPolicy;
import wooteco.subway.domain.fare.ageStrategy.TeenagerDiscountPolicy;
import wooteco.subway.domain.fare.distanceStrategy.DistanceDiscountPolicy;
import wooteco.subway.domain.fare.distanceStrategy.ExtraDiscountPolicy;
import wooteco.subway.domain.fare.distanceStrategy.FreeDiscountPolicy;
import wooteco.subway.domain.fare.distanceStrategy.NormalDistanceDiscountPolicy;

class FareCalculatorTest {

    @ParameterizedTest(name = "{index} : {0}")
    @MethodSource("parameters")
    @DisplayName("거리, 노선들의 추가요금, 나이에 따라 요금을 계산한다.")
    void calculateFare(
        String testName,
        AgeDiscountPolicy ageDiscountPolicy,
        DistanceDiscountPolicy distanceDiscountPolicy,
        int distance,
        int expected) {

        Lines lines = new Lines(List.of(
            new Line("1호선", "red", 100),
            new Line("2호선", "green", 500),
            new Line("3호선", "orange", 300)
        ));
        FareCalculator fareCalculator = new FareCalculator(ageDiscountPolicy, distanceDiscountPolicy);
        double actual = fareCalculator.calculate(distance, lines);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.of("우대 + 거리 무료 + 노선추가요금500 일때 5km : 0원", new PreferentialDiscountPolicy(), new FreeDiscountPolicy(), 5, 0),
            Arguments.of("우대 + 거리 10~50 + 노선추가요금500 일때 15km : 0원", new PreferentialDiscountPolicy(), new NormalDistanceDiscountPolicy(), 15, 0),
            Arguments.of("우대 + 거리 50초과 + 노선추가요금500 일때 58km : 0원", new PreferentialDiscountPolicy(), new ExtraDiscountPolicy(), 58, 0),
            Arguments.of("어린이 + 거리 무료 + 노선추가요금500 일때 5km : 700원", new ChildrenDiscountPolicy(), new FreeDiscountPolicy(), 5, 700),
            Arguments.of("어린이 + 거리 10~50 + 노선추가요금500 일때 15km : 750원", new ChildrenDiscountPolicy(), new NormalDistanceDiscountPolicy(), 15, 750),
            Arguments.of("어린이 + 거리 50초과 + 노선추가요금500 일때 58km : 1150원", new ChildrenDiscountPolicy(), new ExtraDiscountPolicy(), 58, 1150),
            Arguments.of("청소년 + 거리 무료 + 노선추가요금500 일때 5km : 1120원", new TeenagerDiscountPolicy(), new FreeDiscountPolicy(), 5, 1120),
            Arguments.of("청소년 + 거리 10~50 + 노선추가요금500 일때 15km : 1200원", new TeenagerDiscountPolicy(), new NormalDistanceDiscountPolicy(), 15, 1200),
            Arguments.of("청소년 + 거리 50초과 + 노선추가요금500 일때 58km : 1840원", new TeenagerDiscountPolicy(), new ExtraDiscountPolicy(), 58, 1840),
            Arguments.of("일반 + 거리 무료 + 노선추가요금500 일때 5km : 1750원", new NormalAgeDiscountPolicy(), new FreeDiscountPolicy(), 5, 1750),
            Arguments.of("일반 + 거리 10~50 + 노선추가요금500 일때 15km : 1850원", new NormalAgeDiscountPolicy(), new NormalDistanceDiscountPolicy(), 15, 1850),
            Arguments.of("일반 + 거리 50초과 + 노선추가요금500 일때 58km : 2650원", new NormalAgeDiscountPolicy(), new ExtraDiscountPolicy(), 58, 2650)
        );
    }
}