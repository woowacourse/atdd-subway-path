package wooteco.subway.utils;

import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;

public class DaoUtil {

    private DaoUtil() {
    }

    public static Station saveStation(StationDao stationDao, String name) {
        Long id = stationDao.save(new Station(name));
        return new Station(id, name);
    }
}
