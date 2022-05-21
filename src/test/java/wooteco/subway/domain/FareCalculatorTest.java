package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareCalculatorTest {

    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        Line line1 = new Line(1L, "분당선", "Yellow", 0);
        Line line2 = new Line(2L, "신분당선", "Red", 500);
        Line line3 = new Line(3L, "에버라인", "Green", 700);
        List<Line> lines = List.of(line1, line2, line3);

        Section section1 = new Section(line1.getId(), 1L, 2L, 50);
        Section section2 = new Section(line1.getId(), 2L, 3L, 8);
        Section section3 = new Section(line1.getId(), 3L, 6L, 20);
        Section section4 = new Section(line2.getId(), 2L, 4L, 6);
        Section section5 = new Section(line3.getId(), 5L, 4L, 10);
        Section section6 = new Section(line3.getId(), 4L, 6L, 5);
        List<Section> sections = List.of(section1, section2, section3, section4, section5, section6);

        fareCalculator = new FareCalculator(lines, sections);
    }

    @DisplayName("자연수 이외의 거리 입력이 들어올시 예외가 발생한다.")
    @ParameterizedTest(name = "{displayName} - 입력된 거리: {0}")
    @ValueSource(ints = {0, -1})
    void throwExceptionWhenNotNaturalNumberInput(int distance) {
        assertThatThrownBy(
                () -> fareCalculator.calculateFare(List.of(1L, 2L, 3L), distance, 25)
        ).isInstanceOf(Exception.class);
    }

    @DisplayName("추가요금 없는 노선에서 10km 까지 성인 요금은 1250원이다.")
    @ParameterizedTest(name = "{displayName} - 이동한 거리: {0}km, 성인 기준 요금: 1250원")
    @ValueSource(ints = {8, 10})
    void calculateFareUntil_10km(int distance) {
        assertThat(fareCalculator.calculateFare(List.of(1L, 2L, 3L), distance, 25)).isEqualTo(1_250);
    }

    @DisplayName("추가요금 없는 노선에서 10~50km 까지의 거리는 5km 마다 100원씩 증액한다")
    @ParameterizedTest(name = "{displayName} - 이동한 거리: {0}km, 성인 기준 요금: {1}원")
    @CsvSource(value = {"11 - 1350", "14 - 1350", "50 - 2050"}, delimiterString = " - ")
    void calculateFareBetween_10km_and_50km(int distance, int fare) {
        assertThat(fareCalculator.calculateFare(List.of(1L, 2L, 3L), distance, 25)).isEqualTo(fare);
    }

    @DisplayName("추가요금 없는 노선에서 50km 초과시 8km 마다 100원씩 증액한다")
    @ParameterizedTest(name = "{displayName} - 이동한 거리: {0}km, 성인 기준 요금: {1}원")
    @CsvSource(value = {"51 - 2150", "58 - 2150", "59 - 2250"}, delimiterString = " - ")
    void calculateFareOver_50km(int distance, int fare) {
        assertThat(fareCalculator.calculateFare(List.of(1L, 2L, 3L), distance, 25)).isEqualTo(fare);
    }

    @DisplayName("추가요금이 있는 노선을 경유할 시 추가 요금이 붙는다.")
    @ParameterizedTest(name = "{displayName} - 이동한 거리: {0}km, 성인 기준 요금: {1}원")
    @CsvSource(value = {"11 - 1850", "58 - 2650"}, delimiterString = " - ")
    void calculateFareWithExtraLineFare(int distance, int fare) {
        assertThat(fareCalculator.calculateFare(List.of(1L, 2L, 4L), distance, 25)).isEqualTo(fare);
    }

    @DisplayName("추가요금이 있는 2개 이상의 노선을 경유할 시 가장 높은 추가 요금이 붙는다.")
    @ParameterizedTest(name = "{displayName} - 이동한 거리: {0}km, 성인 기준 요금: {1}원")
    @CsvSource(value = {"11 - 2050", "58 - 2850"}, delimiterString = " - ")
    void calculateFareWithMoreThanTwoExtraLineFare(int distance, int fare) {
        assertThat(fareCalculator.calculateFare(List.of(1L, 2L, 4L, 5L), distance, 25)).isEqualTo(fare);
    }
}
