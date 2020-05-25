package wooteco.subway.admin.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {

    @DisplayName("입력받은 Id들을 이용해서 일치하는 Station들을 반환한다.")
    @Test
    void findAllById() {
        //given
        Station station1 = new Station(1L, "");
        Station station2 = new Station(2L, "");
        Station station3 = new Station(3L, "");

        Stations stations = new Stations(Lists.newArrayList(station1, station2, station3));

        //when
        List<Station> findStations = stations.findAllById(Lists.newArrayList(1L, 3L));

        //then
        assertThat(findStations).containsExactly(station1, station3);
    }
}