package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DefaultFareTest {

    private final FareFactory fareFactory = new FareFactory();

    @Test
    @DisplayName("이동 거리가 10km 이내라면 기본 요금 1,250원이 부과된다.")
    void defaultFare() {
        int fare = fareFactory.getFare(8).calculateFare(8, 0);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("이동 거리가 10km 이내라면 기본 요금 1,250원이 부과된다.")
    void defaultFare_10km() {
        int fare = fareFactory.getFare(10).calculateFare(10, 0);

        // then
        assertThat(fare).isEqualTo(1250);
    }
}
