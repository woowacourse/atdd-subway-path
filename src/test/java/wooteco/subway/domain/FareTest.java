package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("1km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원")
    void calculate_1Km() {
        // when
        int result = Fare.calculateFare(1);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원")
    void calculate_10Km() {
        // when
        int result = Fare.calculateFare(10);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("15km = 10km + 5km = 1350원. 10km~50km: 5km 까지 마다 100원 추가")
    void calculate_15km() {
        // when
        int result = Fare.calculateFare(15);

        // then
        assertThat(result).isEqualTo(1350);
    }

    @Test
    @DisplayName("20km = 10km + 5km * 2 = 2150원. 10km~50km: 5km 까지 마다 100원 추가 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_20km() {
        // when
        int result = Fare.calculateFare(20);

        // then
        assertThat(result).isEqualTo(1450);
    }

    @Test
    @DisplayName("50km = 10km + 5km * 8 = 2150원. 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_50km() {
        // when
        int result = Fare.calculateFare(50);

        // then
        assertThat(result).isEqualTo(2050);
    }

    @Test
    @DisplayName("58km = 10km + 5km * 8 + 8km = 2250원. 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_58km() {
        // when
        int result = Fare.calculateFare(58);

        // then
        assertThat(result).isEqualTo(2150);
    }

    @Test
    @DisplayName("거리가 0 미만일 경우 예외를 발생시킨다.")
    void calculate_exception() {
        assertThatThrownBy(() ->
                Fare.calculateFare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 음수일 수 없습니다.");
    }
}
