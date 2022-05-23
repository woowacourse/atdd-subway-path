package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.StationEntity;

@Repository
public class JdbcStationRepository implements StationRepository{

    private final StationDao stationDao;

    public JdbcStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long save(Station station) {
        return stationDao.save(station);
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll().stream()
            .map(this::toStation)
            .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        return stationDao.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return stationDao.existsByName(name);
    }

    @Override
    public Station findById(Long id) {
        StationEntity station = stationDao.findById(id);
        return toStation(station);
    }

    @Override
    public List<Station> findById(List<Long> ids) {
        return stationDao.findById(ids).stream()
            .map(this::toStation)
            .collect(Collectors.toList());
    }

    private Station toStation(StationEntity station) {
        return new Station(station.getId(), station.getName());
    }
}
