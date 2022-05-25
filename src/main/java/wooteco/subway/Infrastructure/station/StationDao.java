package wooteco.subway.Infrastructure.station;

import wooteco.subway.domain.station.Station;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface StationDao {
    long save(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);

    List<Station> findByIdIn(Collection<Long> sortedStationIds);

    boolean existById(Long id);

    boolean existByName(String name);

    void deleteById(Long id);

    void deleteAll();

    default LinkedList<Station> sort(Collection<Long> sortedIds, Collection<Station> stations) {
        return sortedIds.stream()
                .flatMap(sortedId -> stations.stream()
                        .filter(station -> station.getId().equals(sortedId)))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
