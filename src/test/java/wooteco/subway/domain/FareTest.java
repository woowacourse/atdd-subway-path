package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("한 노선만 이동하는 경우, 해당 노선의 추가 요금을 적용한 경우의 요금을 계산한다.")
    @Test
    void calculateAdditionalFare() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections sections = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, sections);
        List<Line> lines = List.of(일호선);
        List<Station> stations = List.of(잠실, 홍대, 신촌);

        Path path = new Path(sections.findShortestPath(잠실, 신촌));

        int fare = Fare.chargeFare(path, lines, stations);

        assertThat(fare).isEqualTo(2250);
    }
}
