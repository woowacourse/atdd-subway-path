package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("9km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원")
    void calculate1() {
        // given
        Fare fare = new Fare();

        // when
        int result = fare.calculateFare(9);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("12km = 10km + 2km = 1350원. 10km~50km: 5km 까지 마다 100원 추가")
    void calculate2() {
        // given
        Fare fare = new Fare();

        // when
        int result = fare.calculateFare(12);

        // then
        assertThat(result).isEqualTo(1350);
    }

    @Test
    @DisplayName("58km = 10km + 40km + 8km = 2150원. 50km 초과: 8km 까지 마다 100원 추가")
    void calculate3() {
        // given
        Fare fare = new Fare();

        // when
        int result = fare.calculateFare(58);

        // then
        assertThat(result).isEqualTo(2150);
    }
}
