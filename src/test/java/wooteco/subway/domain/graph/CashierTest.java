package wooteco.subway.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CashierTest {

    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250"})
    void calculateFare(int distance, long expected) {
        // given
        Cashier cashier = new Cashier();

        // when
        Long actual = cashier.calculateFare(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
