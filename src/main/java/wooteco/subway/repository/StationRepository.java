package wooteco.subway.repository;

import java.util.List;
import java.util.Optional;
import wooteco.subway.domain.station.Station;

public interface StationRepository {

    Station save(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);

    void deleteById(Long id);

    boolean existsByName(String name);

    boolean existsById(Long id);
}
