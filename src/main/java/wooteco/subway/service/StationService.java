package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public long save(Station station) {
        validateName(station);
        return stationDao.save(station);
    }

    private void validateName(Station station) {
        if (stationDao.existStationByName(station.getName())) {
            throw new SubwayException("지하철역 이름이 중복됩니다.");
        }
    }

    public Stations findAll() {
        return new Stations(stationDao.findAll());
    }

    public void delete(Long id) {
        validateExistStation(id);
        stationDao.delete(id);
    }

    private void validateExistStation(Long id) {
        if (!stationDao.existStationById(id)) {
            throw new DataNotExistException("존재하지 않는 지하철역입니다.");
        }
    }
}
