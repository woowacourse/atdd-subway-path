package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LineStationsTest {

    @Test
    void getInformation() {
        LineStations lineStations = new LineStations(Arrays.asList(
                new LineStation(null, 1L, 10, 10),
                new LineStation(1L, 2L, 5, 7),
                new LineStation(2L, 3L, 4, 6),
                new LineStation(3L, 4L, 4, 6)
        ));

        assertThat(lineStations.getInformation(Arrays.asList(1L, 2L, 3L), PathType.DISTANCE)).isEqualTo(13);
        assertThat(lineStations.getInformation(Arrays.asList(2L, 3L, 4L), PathType.DURATION)).isEqualTo(8);
    }
}