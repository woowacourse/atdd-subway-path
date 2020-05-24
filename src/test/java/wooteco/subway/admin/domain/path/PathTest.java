package wooteco.subway.admin.domain.path;

import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.admin.domain.path.util.DummyLineStationsCreator.createLineStations;

class PathTest {

    @Test
    void of() {
        // given
        LineStations lineStations = createLineStations();

        Station jamsil = new Station(1L, "잠실역");
        Station jamsilSaenae = new Station(2L, "잠실새내역");
        Station jonghapundongjang = new Station(3L, "종합운동장역");
        Station samjeon = new Station(4L, "삼전역");
        List<Station> shortestPathStations = Arrays.asList(jamsil, jamsilSaenae, jonghapundongjang, samjeon);

        // when
        Path path = Path.of(lineStations, new Stations(shortestPathStations));
        // then
        assertThat(path.getStations().getStations()).hasSize(4);
        assertThat(path.getDistance()).isEqualTo(30L);
        assertThat(path.getDuration()).isEqualTo(30L);
    }
}