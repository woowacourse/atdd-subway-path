package wooteco.subway.domain.fare;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AgeFarePolicyTest {

    /*
    청소년(13세 이상~19세 미만): 운임에서 350원을 공제한 금액의 20%할인
    어린이(6세 이상~13세 미만): 운임에서 350원을 공제한 금액의 50%할인
    유아(6세 미만): 무료
    노인(65 이상): 무료
     */
    @DisplayName("연령 요금 정책에 맞게 요금을 환산해준다.")
    @ParameterizedTest
    @MethodSource("provideFareAndAge")
    void apply(Fare fare, Age age, Fare expected) {
        Assertions.assertThat(AgeFarePolicy.getFare(fare, age)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideFareAndAge() {
        return Stream.of(
                Arguments.of(new Fare(1350), new Age(5), new Fare(0)),
                Arguments.of(new Fare(1350), new Age(6), new Fare(500)),
                Arguments.of(new Fare(1350), new Age(12), new Fare(500)),
                Arguments.of(new Fare(1350), new Age(13), new Fare(800)),
                Arguments.of(new Fare(1350), new Age(18), new Fare(800)),
                Arguments.of(new Fare(1350), new Age(19), new Fare(1350)),
                Arguments.of(new Fare(1350), new Age(64), new Fare(1350)),
                Arguments.of(new Fare(1350), new Age(65), new Fare(0))
        );
    }
}
