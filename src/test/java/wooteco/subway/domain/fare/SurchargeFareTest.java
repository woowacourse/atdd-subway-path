package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SurchargeFareTest {

    private final FareFactory fareFactory = new FareFactory();

    @Test
    @DisplayName("이동 거리가 10km ~ 50km 사이라면 기본 요금에 5km 마다 100원 추가된 요금이 부과된다.")
    void additionalFare() {
        // given
        int fare = fareFactory.getFare(12).calculateFare(12, 0);
        // then
        assertThat(fare).isEqualTo(1350);
    }

    @Test
    @DisplayName("이동 거리가 10km ~ 50km 사이라면 기본 요금에 5km 마다 100원 추가된 요금이 부과된다.")
    void additionalFare2() {
        // given
        int fare = fareFactory.getFare(50).calculateFare(50, 0);

        // then
        assertThat(fare).isEqualTo(2050);
    }

}
