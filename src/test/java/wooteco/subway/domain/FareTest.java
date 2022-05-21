package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("노선의 추가요금이 없을 때의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"8,2,1250", "10,4,1350", "30,10,1850", "50,8,2150"})
    void calculateFareByDistance(int firstDistance, int secondDistance, int expectedFare) {
        final Line line = new Line("2호선", "bg-green-600", 0);
        final Section section1 = new Section(new Station("왕십리"), new Station("선릉역"), firstDistance, line);
        final Section section2 = new Section(new Station("선릉역"), new Station("잠실역"), secondDistance, line);
        final Sections sections = new Sections(List.of(section1, section2));

        assertThat(Fare.calculate(sections)).isEqualTo(expectedFare);
    }

    @DisplayName("노선의 추가요금이 있을 떄의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"6,2,2150", "10,4,2250", "30,10,2750", "50,8,3050"})
    void calculateFareByLineAndDistance(int firstDistance, int secondDistance, int expectedFare) {
        final Line line1 = new Line("수인분당선", "bg-yellow-600", 900);
        final Line line2 = new Line("2호선", "bg-green-600", 0);
        final Section section1 = new Section(new Station("왕십리"), new Station("선릉역"), firstDistance, line1);
        final Section section2 = new Section(new Station("선릉역"), new Station("잠실역"), secondDistance, line2);
        final Sections sections = new Sections(List.of(section1, section2));

        assertThat(Fare.calculate(sections)).isEqualTo(expectedFare);
    }
}
