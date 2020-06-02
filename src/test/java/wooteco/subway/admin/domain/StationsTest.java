package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationsTest {

    private Stations stations;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "가");
        station2 = new Station(2L, "나");
        station3 = new Station(3L, "다");
        stations = new Stations(Arrays.asList(station1, station2, station3));
    }

    @DisplayName("역 id들로 역들 추출하기")
    @Test
    void filterStationsByIdsTest() {
        List<Station> filteredStations = stations.filterStationsByIds(Arrays.asList(1L, 3L));
        assertThat(filteredStations).containsExactly(station1, station3);
    }

    @DisplayName("주어진 역id가 포함되어 있지 않은지 테스트")
    @Test
    void isNotContainsTest() {
        assertThat(stations.isNotContains(1L)).isFalse();
        assertThat(stations.isNotContains(4L)).isTrue();
    }
}
