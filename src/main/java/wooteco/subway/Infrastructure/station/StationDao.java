package wooteco.subway.Infrastructure.station;

import wooteco.subway.domain.station.Station;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class StationDao {
    abstract public long save(Station station);

    abstract public List<Station> findAll();

    abstract public Optional<Station> findById(Long id);

    abstract public List<Station> findByIdIn(Collection<Long> sortedStationIds);

    abstract public boolean existById(Long id);

    abstract public boolean existByName(String name);

    abstract public void deleteById(Long id);

    abstract public void deleteAll();

    protected LinkedList<Station> sort(Collection<Long> sortedIds, Collection<Station> stations) {
        return sortedIds.stream()
                .flatMap(sortedId -> stations.stream()
                        .filter(station -> station.getId().equals(sortedId)))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
