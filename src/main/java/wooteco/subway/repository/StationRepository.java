package wooteco.subway.repository;

import java.util.List;
import java.util.Optional;
import wooteco.subway.domain.station.Station;

public interface StationRepository {
    Long save(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);

    Optional<Station> findByName(String name);

    void deleteById(Long id);
}
