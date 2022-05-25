package wooteco.subway.infrastructure.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NoSuchStationException;
import wooteco.subway.infrastructure.jdbc.dao.StationDao;
import wooteco.subway.infrastructure.jdbc.dao.entity.EntityAssembler;

@Repository
public class StationJdbcRepository implements StationRepository {

    private final StationDao stationDao;

    public StationJdbcRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(Station station) {
        long stationId = stationDao.save(EntityAssembler.stationEntity(station));
        return getById(stationId);
    }

    @Override
    public List<Station> getAll() {
        return stationDao.findAll()
                .stream()
                .map(EntityAssembler::station)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Station getById(long stationId) {
        return stationDao.findById(stationId)
                .map(EntityAssembler::station)
                .orElseThrow(() -> new NoSuchStationException(stationId));
    }

    @Override
    public void remove(long stationId) {
        stationDao.remove(stationId);
    }

    @Override
    public boolean existsById(long stationId) {
        return stationDao.existsById(stationId);
    }

    @Override
    public boolean existsByName(String name) {
        return stationDao.existsByName(name);
    }
}
