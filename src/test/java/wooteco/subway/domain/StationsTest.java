package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StationsTest {

    @DisplayName("인자로 전달된 StationId 순으로 역들이 정렬되어 반환되는 지 확인한다.")
    @Test
    void arrangeStationsByIds() {
        Stations stations = new Stations(List.of(
                new Station(1L, "강남역"),
                new Station(2L, "역삼역"),
                new Station(3L, "선릉역")));
        List<Station> arrangedStations = stations.arrangeStationsByIds(List.of(3L, 2L, 1L));

        assertThat(arrangedStations).containsExactly(
                new Station(3L, "선릉역"),
                new Station(2L, "역삼역"),
                new Station(1L, "강남역"));
    }
}
