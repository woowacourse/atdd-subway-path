package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;

class LineStationsTest {
    @Test
    void getStationsId() {
        LineStation lineStation = new LineStation(null, 1L, 10, 10);
        LineStation lineStation1 = new LineStation(1L, 2L, 10, 10);
        LineStation lineStation2 = new LineStation(2L, 3L, 10, 10);
        LineStations lineStations = new LineStations(Sets.newHashSet(lineStation, lineStation1, lineStation2));

        assertThat(lineStations.getStationIds().size()).isEqualTo(3);
    }
}