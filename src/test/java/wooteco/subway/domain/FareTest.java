package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("1km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원")
    void calculate_1Km() {
        // given
        final Fare fare = new Fare(1, 0, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("1km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원. 6세")
    void calculate_1Km_age_6() {
        // given
        final Fare fare = new Fare(1, 0, 6);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(800);
    }

    @Test
    @DisplayName("1km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원. 13세")
    void calculate_1Km_age_13() {
        // given
        final Fare fare = new Fare(1, 0, 13);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1070);
    }

    @Test
    @DisplayName("1km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원. 19세")
    void calculate_1Km_age_19() {
        // given
        final Fare fare = new Fare(1, 0, 19);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("1km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원 + 추가요금 100원")
    void calculate_1Km_extra_100_Won() {
        // given
        final Fare fare = new Fare(1, 100, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1350);
    }


    @Test
    @DisplayName("10km = 1250원. 기본운임(10㎞ 이내): 기본운임 1,250원")
    void calculate_10Km() {
        // given
        final Fare fare = new Fare(10, 0, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("15km = 10km + 5km = 1350원. 10km~50km: 5km 까지 마다 100원 추가")
    void calculate_15km() {
        // given
        final Fare fare = new Fare(15, 0, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1350);
    }

    @Test
    @DisplayName("15km = 10km + 5km = 1350원. 10km~50km: 5km 까지 마다 100원 추가 + 추가요금 100원")
    void calculate_15km_extra_100() {
        // given
        final Fare fare = new Fare(15, 100, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1450);
    }

    @Test
    @DisplayName("20km = 10km + 5km * 2 = 2150원. 10km~50km: 5km 까지 마다 100원 추가 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_20km() {
        // given
        final Fare fare = new Fare(20, 0, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(1450);
    }

    @Test
    @DisplayName("50km = 10km + 5km * 8 = 2150원. 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_50km() {
        // given
        final Fare fare = new Fare(50, 0, 20);
        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km = 10km + 5km * 8 = 2150원. 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_50km_extra_100() {
        // given
        final Fare fare = new Fare(50, 100, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(2150);
    }

    @Test
    @DisplayName("58km = 10km + 5km * 8 + 8km = 2250원. 50km 초과: 8km 까지 마다 100원 추가")
    void calculate_58km() {
        // given
        final Fare fare = new Fare(58, 0, 20);

        // when
        int result = fare.calculateFare();

        // then
        assertThat(result).isEqualTo(2150);
    }

    @Test
    @DisplayName("거리가 0 미만일 경우 예외를 발생시킨다.")
    void calculate_exception_distance() {
        assertThatThrownBy(() ->
                new Fare(-1, 0, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리 또는 추가요금은 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("추가요금이 0 미만일 경우 예외를 발생시킨다.")
    void calculate_exception_fee() {
        assertThatThrownBy(() ->
                new Fare(10, -1, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리 또는 추가요금은 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("나이가 0 미만일 경우 예외를 발생시킨다.")
    void calculate_exception_age() {
        assertThatThrownBy(() ->
                new Fare(10, 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이는 음수일 수 없습니다.");
    }
}
