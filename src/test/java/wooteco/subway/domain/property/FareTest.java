package wooteco.subway.domain.property;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.exception.NegativeFareException;

class FareTest {

    @Test
    @DisplayName("기본 요금은 1250원이다.")
    public void defaultConstructor() {
        // given & when
        Fare fare = new Fare(0);

        // then
        assertThat(fare.getAmount()).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150"})
    public void calculateFareByDistance(int distance, int expectedFare) {
        assertThat(new Fare(distance).getAmount()).isEqualTo(expectedFare);
    }
}
