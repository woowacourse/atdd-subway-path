package wooteco.subway.station.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StationsTest {
    private List<Station> stationList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Station 강남역 = new Station(1L, "강남역");
        Station 잠실역 = new Station(2L, "잠실역");
        Station 구의역 = new Station(3L, "구의역");

        stationList.add(강남역);
        stationList.add(잠실역);
        stationList.add(구의역);
    }

    @Test
    @DisplayName("빈 리스트를 전달받아 생성시 예외를 던진다.")
    void testCreateStations() {
        List<Station> empty = new ArrayList<>();

        assertThatThrownBy(() -> new Stations(empty))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("등록된 역이 없습니다.");
    }

    @Test
    @DisplayName("Station ID 리스트를 전달받아 List<Station>을 반환한다.")
    void testFindStationsOnPath() {
        Stations stations = new Stations(stationList);
        List<Long> Ids = stationList.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        assertThat(stations.findStationsOnPath(Ids)).isEqualTo(stationList);
    }
}
