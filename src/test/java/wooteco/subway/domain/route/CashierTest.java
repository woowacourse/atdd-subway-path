package wooteco.subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    class TestSection implements Comparable<TestSection> {
        Integer up;
        Integer down;

        public TestSection(int up, int down) {
            this.up = up;
            this.down = down;
        }

        @Override
        public int compareTo(TestSection o) {
            if (Objects.equals(this.down, o.up)) {
                return -1;
            }

            if (Objects.equals(this.up, o.down)) {
                return 1;
            }

            return 0;
        }
    }

    @Test
    @DisplayName("g")
    void g() {
        // given
        List<TestSection> testSections = new ArrayList<>();
        final TestSection first = new TestSection(1, 2);
        final TestSection second = new TestSection(2, 3);
        final TestSection third = new TestSection(3, 4);
        final TestSection last = new TestSection(4, 5);
        testSections.add(first);
        testSections.add(third);
        testSections.add(last);
        testSections.add(second);

        // when
        Collections.sort(testSections);

        // then
        assertThat(testSections).containsExactly(first, second, third, last);
    }
}
