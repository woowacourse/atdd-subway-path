package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationsTest {

    @Test
    @DisplayName("입력 받은 StationId 리스트에 따라 정렬된 Station 리스트를 반환한다.")
    void sortedStationsById() {
        // given
        Station station1 = new Station(1L, "강남역");
        Station station2 = new Station(2L, "역삼역");
        Station station3 = new Station(3L, "선릉역");
        List<Station> inputStations = List.of(station1, station2, station3);
        Stations stations = new Stations(inputStations);
        List<Long> stationIds = List.of(3L, 2L, 1L);

        // when
        List<Station> sortedStations = stations.sortedStationsById(stationIds);

        // then
        assertAll(
            () -> assertThat(sortedStations.get(0)).isEqualTo(station3),
            () -> assertThat(sortedStations.get(1)).isEqualTo(station2),
            () -> assertThat(sortedStations.get(2)).isEqualTo(station1)
        );
    }
}
