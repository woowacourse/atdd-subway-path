package wooteco.subway.station.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {

    private Stations stations;

    @BeforeEach
    void setUp() {
        stations = new Stations(Arrays.asList(new Station(1L, "1번역"),
                new Station(2L, "2번역"),
                new Station(3L, "3번역")));
    }

    @Test
    void getStations() {
        Station firstStation = stations.getStationById(1L);
        Station lastStation = stations.getStationById(3L);

        assertThat(firstStation.getName()).isEqualTo("1번역");
        assertThat(lastStation.getName()).isEqualTo("3번역");
    }
}
