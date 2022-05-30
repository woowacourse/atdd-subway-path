package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExtraSurchargeTest {
    private final FareFactory fareFactory = new FareFactory();

    @Test
    @DisplayName("50km을 초과한 거리라면 50km까지 계산된 요금에 8km 마다 100원 추가된 요금이 부과된다.")
    void extraAdditionalFare() {
        // given
        int fare = fareFactory.getFare(58).calculateFare(58, 0);
        // then
        assertThat(fare).isEqualTo(2150);
    }
}
