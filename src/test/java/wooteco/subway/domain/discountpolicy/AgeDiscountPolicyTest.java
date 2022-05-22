package wooteco.subway.domain.discountpolicy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.factory.SectionFactory;
import wooteco.subway.domain.factory.StationFactory;

@DisplayName("AgeDiscountPolicy 는")
class AgeDiscountPolicyTest {

    @DisplayName("나이가 주어 졌을 떄 해당 나이에 대한 금액 할인을 적용해야 한다.")
    @ParameterizedTest(name = "{index} {displayName} age={0} expectedFare={1}")
    @CsvSource(value = {"5, 0", "6, 625", "13, 1000", "19, 1250"})
    void discountFare(final int age, final int expectedFare) {
        final Fare fare = new Fare(AgeDiscountFactory.from(age));
        final Sections sections = new Sections(List.of(SectionFactory.from(SectionFactory.AB3),
                SectionFactory.from(SectionFactory.BC3)));
        final List<Station> stations = List.of(StationFactory.from(StationFactory.A),
                StationFactory.from(StationFactory.B), StationFactory.from(StationFactory.C));
        final Line line = new Line(1L, "lien", "bg-red-600", sections, 1L, 0);
        final Line line2 = new Line(1L, "lien", "bg-red-600", sections, 1L, 0);
        final List<Line> lines = List.of(line, line2);

        final int actualFare = fare.calculate(10, stations, lines);

        assertThat(actualFare).isEqualTo(expectedFare);
    }
}
