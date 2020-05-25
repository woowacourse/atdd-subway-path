package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    @Test
    void makeLineStation() {
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 선릉역 = new Station(3L, "선릉역");
        Station 삼성역 = new Station(4L, "삼성역");
        Station 양재시민의숲역 = new Station(5L, "양재시민의숲역");
        Station 청계산입구역 = new Station(6L, "청계산입구역");

        Line line2 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 10, 10));
        line2.addLineStation(new LineStation(1L, 2L, 10, 10));
        line2.addLineStation(new LineStation(2L, 3L, 10, 10));

        Line newLine = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        newLine.addLineStation(new LineStation(null, 4L, 10, 10));
        newLine.addLineStation(new LineStation(4L, 5L, 10, 10));
        newLine.addLineStation(new LineStation(5L, 6L, 10, 10));

        Lines lines = new Lines(Arrays.asList(line2, newLine));

        assertThat(lines.makeLineStation().getLineStations().size()).isEqualTo(4);
    }
}