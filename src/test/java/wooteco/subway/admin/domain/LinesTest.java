package wooteco.subway.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LinesTest {
    @Test
    @DisplayName("모든 노선에서 LineStation 추출하기 테스트")
    void getAllLineStationTest() {
        Set<LineStation> secondLineStations = new HashSet<>();
        Set<LineStation> bundangLineStations = new HashSet<>();
        secondLineStations.add(new LineStation(null, 1L, 10, 10));
        secondLineStations.add(new LineStation(1L, 2L, 10, 10));
        secondLineStations.add(new LineStation(2L, 3L, 10, 10));
        secondLineStations.add(new LineStation(3L, 4L, 10, 10));
        bundangLineStations.add(new LineStation(null, 5L, 10, 10));
        bundangLineStations.add(new LineStation(5L, 6L, 10, 10));
        bundangLineStations.add(new LineStation(6L, 3L, 10, 10));
        bundangLineStations.add(new LineStation(3L, 7L, 10, 10));

        List<Line> lines = new ArrayList<>();
        lines.add(new Line(1L, "2호선", "bg-gray-300", LocalTime.of(5, 30), LocalTime.of(23, 30), 10, secondLineStations));
        lines.add(new Line(2L, "분당선", "bg-gray-300", LocalTime.of(5, 30), LocalTime.of(23, 30), 10, bundangLineStations));

        Lines lines1 = new Lines(lines);

        List<LineStation> allLineStation = lines1.getAllLineStation();

        assertThat(allLineStation.size()).isEqualTo(8);
    }
}
