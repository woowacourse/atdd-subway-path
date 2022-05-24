package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareFactory;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "50:2050", "51:2150", "58:2150", "59:2250"}, delimiter = ':')
    @DisplayName("거리에 따른 요금을 계산한다.")
    void getFare(int distance, int expectFare) {
        Fare fare = makeFare(distance, 0, 20);
        assertThat(fare.getFare()).isEqualTo(expectFare);
    }

    @Test
    @DisplayName("추가 요금을 계산한다.")
    void calculateExtraFare() {
        //given
        Fare fare = makeFare(10, 300, 20);
        //when
        //then
        assertThat(fare.getFare()).isEqualTo(1550);
    }

    private Fare makeFare(int distance, int maxExtraFare, int age) {
        FareFactory fareFactory = new FareFactory();
        return fareFactory.makeFare(distance, maxExtraFare, age);
    }
}
