package wooteco.subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class StationsTest {

    private static final Stations STATIONS = new Stations(List.of(
            new Station(1L, "선릉역"),
            new Station(2L, "잠실역"),
            new Station(3L, "역삼역"),
            new Station(4L, "강남역"),
            new Station(5L, "교대역")
    ));

    @Test
    void findStationById() {
        assertThat(STATIONS.findStationById(1L))
                .usingRecursiveComparison()
                .isEqualTo(new Station(1L, "선릉역"));
    }

    @Test
    void filter() {
        List<Long> stationIds = List.of(1L, 3L, 4L);

        assertThat(STATIONS.filter(stationIds).getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Station(1L, "선릉역"),
                        new Station(3L, "역삼역"),
                        new Station(4L, "강남역")
                ));
    }
}
