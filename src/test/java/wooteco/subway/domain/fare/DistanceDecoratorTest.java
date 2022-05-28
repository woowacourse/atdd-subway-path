package wooteco.subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DistanceDecoratorTest {

    private static final int BASIC_FARE = 1250;

    @DisplayName("10km보다 같거나 작으면 추가 요금이 없이 기본요금 그대로이다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 10})
    void calculateExtraFareUnder10(int distance) {
        Decorator distanceDecorator = new DistanceDecorator(new BaseFare(0), distance);
        double fare = distanceDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(BASIC_FARE);
    }

    @DisplayName("10km 초과 50km 이하이면 5km마다 기본 요금에서 100원이 추가된다.")
    @ParameterizedTest
    @CsvSource({"12, 100", "16, 200"})
    void calculateExtraFareOver10Under50(int distance, int expected) {
        Decorator distanceDecorator = new DistanceDecorator(new BaseFare(0), distance);
        double fare = distanceDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(BASIC_FARE + expected);
    }

    @DisplayName("50km 초과이면 8km마다 기본 요금에서 100원이 추가된다.")
    @ParameterizedTest
    @ValueSource(ints = {58, 57})
    void calculateExtraFareOver50(int distance) {
        Decorator distanceDecorator = new DistanceDecorator(new BaseFare(0), distance);
        double fare = distanceDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(BASIC_FARE + 900);
    }

    @DisplayName("거리가 0km 이하이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    void calculateExtraFareUnder0(int distance) {
        Decorator distanceDecorator = new DistanceDecorator(new BaseFare(0), distance);
        assertThatThrownBy(distanceDecorator::calculateExtraFare)
                .isExactlyInstanceOf(NoSuchElementException.class);
    }
}
