package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicateNameException;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station createStation(final Station station) {
        validateDuplicateName(station);
        return stationDao.save(station);
    }

    public List<Station> findAllStations() {
        return stationDao.findAll();
    }

    public void delete(final Long id) {
        final int affectedRows = stationDao.deleteById(id);

        if (affectedRows == 0) {
            throw new DataNotFoundException("존재하지 않는 노선 id 입니다.");
        }
    }

    private void validateDuplicateName(final Station station) {
        if (stationDao.existByName(station.getName())) {
            throw new DuplicateNameException("이미 존재하는 지하철 역입니다.");
        }
    }
}
