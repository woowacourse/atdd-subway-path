package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareTest {
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "49:2050", "50:2050", "51:2150"}, delimiter = ':')
    @DisplayName("거리에 따라 요금을 계산한다.")
    void calculateFare(int distance, int expected) {
        Fare fare = new Fare(distance);
        assertThat(fare.getAmount()).isEqualTo(expected);
    }
}
