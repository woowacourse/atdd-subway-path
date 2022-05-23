package wooteco.subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AgeTest {

    @Test
    @DisplayName("6세 미만이면 요금 0원")
    void babyFare() {
        assertThat(Age.discountBy(5, 1250)).isEqualTo(0);
    }

    @Test
    @DisplayName("6세 이상 13세 미만이면 어린이 할인 적용")
    void childFare() {
        assertThat(Age.discountBy(10, 1250)).isEqualTo(450);
    }

    @Test
    @DisplayName("13세 이상이면 19세 미만이면 청소년 할인 적용")
    void teenagerFare() {
        assertThat(Age.discountBy(15, 1250)).isEqualTo(720);
    }

    @Test
    @DisplayName("19세 이상이면 정상 요금 적용")
    void adultFare() {
        assertThat(Age.discountBy(20, 1250)).isEqualTo(1250);
    }
}
