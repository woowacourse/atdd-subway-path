package wooteco.subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.dao.entity.StationEntity;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(Station station) {
        final Long savedId = stationDao.save(new StationEntity(station.getName()));

        return new Station(savedId, station.getName());
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll()
                .stream()
                .map(this::toStation)
                .collect(Collectors.toList());
    }

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    @Override
    public Optional<Station> findById(Long id) {
        final Optional<StationEntity> stationEntity = stationDao.findById(id);

        if (stationEntity.isEmpty()) {
            return Optional.empty();
        }

        final StationEntity existEntity = stationEntity.get();
        return Optional.of(toStation(existEntity));
    }

    @Override
    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return stationDao.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return stationDao.existsById(id);
    }
}
