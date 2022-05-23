package wooteco.subway.domain.property.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.Distance;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource({"10,1250", "12,1350", "16,1450", "50,2050", "58,2150"})
    @DisplayName("거리에 따른 운임을 계산한다.")
    public void calculateFareByDistance(int distance, int expectedFare) {
        // given
        Fare fare = new Fare();
        FarePolicy policy = new DistanceFarePolicy(new Distance(distance));

        // when
        final Fare surcharged = policy.apply(new Fare());

        // then
        assertThat(surcharged.getAmount()).isEqualTo(expectedFare);
    }
}