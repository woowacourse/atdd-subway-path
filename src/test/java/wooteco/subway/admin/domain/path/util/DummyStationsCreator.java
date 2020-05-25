package wooteco.subway.admin.domain.path.util;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;

import java.util.Arrays;
import java.util.List;

public class DummyStationsCreator {
    public static Stations createStations() {
        Station jamsil = new Station(1L, "잠실역");
        Station jamsilSaenae = new Station(2L, "잠실새내역");
        Station jonghapundongjang = new Station(3L, "종합운동장역");

        Station samjeon = new Station(4L, "삼전역");
        Station seokchonGoBun = new Station(5L, "석촌고분역");
        Station seokchon = new Station(6L, "석촌역");

        List<Station> stations = Arrays.asList(jamsil, jamsilSaenae, jonghapundongjang,
                samjeon, seokchon, seokchonGoBun);
        return new Stations(stations);
    }
}
