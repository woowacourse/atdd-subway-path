package wooteco.subway.Infrastructure;

import wooteco.subway.domain.Station;

import java.util.*;
import java.util.stream.Collectors;

public interface StationDao {
    long save(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);

    List<Station> findByIdIn(Collection<Long> sortedStations);

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
