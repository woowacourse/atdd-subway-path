package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.discountpolicy.AgeDiscountFactory;
import wooteco.subway.domain.factory.SectionFactory;
import wooteco.subway.domain.factory.StationFactory;

@DisplayName("Fare 는 ")
class FareTest {

    @DisplayName("최단거리를 입력하면 요금을 계산한다.")
    @ParameterizedTest(name = "{index} {displayName} distance={0} expectedFare={1}")
    @CsvSource(value = {"1, 1250", "10, 1250"})
    void calculateFareWithDistanceShortThan10km(final int distance, final int expectedFare) {
        Fare fare = new Fare(AgeDiscountFactory.from(50));

        assertThat(fare.calculate(distance, Collections.emptyList(), Collections.emptyList()))
                .isEqualTo(expectedFare);
    }

    @DisplayName("10km 에서 50km 사이일 경우 5km 마다 100원이 추가된 요금을 계산한다.")
    @ParameterizedTest(name = "{index} {displayName} distance={0} expectedFare={1}")
    @CsvSource(value = {"11, 1350", "50, 2050"})
    void calculateFareWithDistanceBetween10kmAnd50km(final int distance, final int expectedFare) {
        Fare fare = new Fare(AgeDiscountFactory.from(50));

        assertThat(fare.calculate(distance, Collections.emptyList(), Collections.emptyList()))
                .isEqualTo(expectedFare);
    }

    @DisplayName("50km 를 초과할 경우 8km 마다 100원이 추가된 요금을 계산한다.")
    @Test
    void calculateFareWithDistanceOver50() {
        Fare fare = new Fare(AgeDiscountFactory.from(50));

        assertThat(fare.calculate(51, Collections.emptyList(), Collections.emptyList()))
                .isEqualTo(2150);
    }

    @DisplayName("노선에 추가 요금이 있으면 추가 요금을 합해 요금을 계산한다.")
    @Test
    void calculateFareWithExtraLineFare() {
        final Fare fare = new Fare(AgeDiscountFactory.from(50));
        final Sections sections = new Sections(List.of(SectionFactory.from(SectionFactory.AB3),
                SectionFactory.from(SectionFactory.BC3)));
        final List<Station> stations = List.of(StationFactory.from(StationFactory.A),
                StationFactory.from(StationFactory.B), StationFactory.from(StationFactory.C));
        final Line line = new Line(1L, "lien", "bg-red-600", sections, 1L, 100);
        final Line line2 = new Line(1L, "lien", "bg-red-600", sections, 1L, 1000);
        final List<Line> lines = List.of(line, line2);

        assertThat(fare.calculate(9, stations, lines)).isEqualTo(2250);
    }
}
