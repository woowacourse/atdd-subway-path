package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.fare.Discounter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Discounter 도메인 객체 관련 테스트")
class DiscounterTest {

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    @DisplayName("나이가 6 세 이상 13 세 미만일 경우 350 원을 공제한 금액에서 50% 를 할인한다.")
    void discountChild(int age) {
        assertThat(Discounter.discount(1350, age)).isEqualTo(500);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    @DisplayName("나이가 13 세 이상 19 세 미만일 경우 350 원을 공제한 금액에서 20% 를 할인한다.")
    void discountTeenager(int age) {
        assertThat(Discounter.discount(1350, age)).isEqualTo(800);
    }
}
