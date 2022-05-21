package wooteco.subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceTest {

    @Test
    @DisplayName("10km 미만이면 기본 요금 적용")
    void under10km(){
        assertThat(Distance.calculateFareBy(5)).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 이면 기본 요금 적용")
    void at10km(){
        assertThat(Distance.calculateFareBy(10)).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과 50km 미만이면 중거리 구간 요금 적용")
    void between10kmAnd50km(){
        assertThat(Distance.calculateFareBy(22)).isEqualTo(1550);
    }

    @Test
    @DisplayName("50km 이면 중거리 구간 요금 적용")
    void at50km(){
        assertThat(Distance.calculateFareBy(50)).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km 초과이면 장거리 요금 적용")
    void over10km(){
        assertThat(Distance.calculateFareBy(79)).isEqualTo(2450);
    }
}
