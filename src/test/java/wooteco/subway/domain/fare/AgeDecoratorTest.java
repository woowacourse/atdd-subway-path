package wooteco.subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AgeDecoratorTest {

    private static final int BASIC_FARE = 1250;
    @DisplayName("청소년일 경우 350원을 공제하고 20% 할인을 한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 17, 18})
    void calculateExtraFareTeenager(int age) {
        Decorator ageDecorator = new AgeDecorator(new BaseFare(0), age);
        double fare = ageDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(720);
    }

    @DisplayName("어린이일 경우 350원을 공제하고 50% 할인을 한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 11, 12})
    void calculateExtraFareChildren(int age) {
        Decorator ageDecorator = new AgeDecorator(new BaseFare(0), age);
        double fare = ageDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("일반인일 경우 할인없이 가격 그대로 적용한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 30, 55})
    void calculateExtraFareNormal(int age) {
        Decorator ageDecorator = new AgeDecorator(new BaseFare(0), age);
        double fare = ageDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(BASIC_FARE);
    }

    @DisplayName("6살 미만 아기인 경우 무료이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void calculateExtraFareBaby(int age) {
        Decorator ageDecorator = new AgeDecorator(new BaseFare(0), age);
        double fare = ageDecorator.calculateExtraFare();
        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("0살 이하의 나이인 경우 할인을 찾을수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    void calculateExtraFareFailure(int age) {
        Decorator ageDecorator = new AgeDecorator(new BaseFare(0), age);
        assertThatThrownBy(ageDecorator::calculateExtraFare)
                .isExactlyInstanceOf(NoSuchElementException.class);
    }
}
