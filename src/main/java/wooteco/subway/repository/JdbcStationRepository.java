package wooteco.subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;
import wooteco.subway.entity.StationEntity;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long save(Station station) {
        StationEntity stationEntity = StationEntity.from(station);
        return stationDao.save(stationEntity);
    }

    @Override
    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(this::entityToStation)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Station> findById(Long id) {
        try {
            StationEntity stationEntity = stationDao.findById(id);
            Station station = entityToStation(stationEntity);
            return Optional.of(station);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Station> findByName(String name) {
        try {
            StationEntity stationEntity = stationDao.findByName(name);
            Station station = entityToStation(stationEntity);
            return Optional.of(station);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Station entityToStation(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    @Override
    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
