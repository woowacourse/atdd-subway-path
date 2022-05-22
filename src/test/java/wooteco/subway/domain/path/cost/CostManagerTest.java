package wooteco.subway.domain.path.cost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CostManagerTest {

    @ParameterizedTest
    @CsvSource(value = {"19 : 1250", "6 : 450", "12 : 450", "13 : 720", "18 : 720"}, delimiter = ':')
    void 구간이_3개이고_운행거리가_1번째_구간에_속할_때(int age, int fare) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(9, 0, age);
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"19 : 0", "6 : 0", "12 : 0", "13 : 0", "18 : 0"}, delimiter = ':')
    void 구간이_3개이고_운행거리가_0일_때(int age, int fare) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(0, 0, age);
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"19 : 2050", "6 : 850", "12 : 850", "13 : 1360", "18 : 1360"}, delimiter = ':')
    void 구간이_3개이고_운행거리가_2번째_구간의_경계값에_속할_때(int age, int fare) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(49, 0, age);
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"19 : 1650", "6 : 650", "12 : 650", "13 : 1040", "18 : 1040"}, delimiter = ':')
    void 구간이_3개이고_운행거리가_2번째_구간에_속할_때(int age, int fare) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(27, 0, age);
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"19 : 2150", "6 : 900", "12 : 900", "13 : 1440", "18 : 1440"}, delimiter = ':')
    void 구간이_3개이고_운행거리가_3번째_구간의_경계값에_속할_때(int age, int fare) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(51, 0, age);
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"19 : 2250", "6 : 950", "12 : 950", "13 : 1520", "18 : 1520"}, delimiter = ':')
    void 구간이_3개이고_운행거리가_3번째_구간에_속할_때(int age, int fare) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        int result = costManager.calculateFare(60, 0, age);
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest
    @ValueSource(ints = {5,201})
    void 어린이보다_나이가_적거나_200세보다_나이가_많으면_예외발생(int age) {
        CostManager costManager = new CostManager(List.of(new CostSection(50, 8, 100),
                new CostSection(10, 5, 100)));
        assertThatThrownBy(() -> costManager.calculateFare(60, 0, age))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
