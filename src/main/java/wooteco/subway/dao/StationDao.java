package wooteco.subway.dao;

import wooteco.subway.domain.Station;

import java.util.List;
import java.util.Optional;

public interface StationDao {
    Station save(Station station) throws IllegalArgumentException;

    List<Station> findAll();

    void deleteById(Long id);

    Optional<Station> findById(Long id);

    boolean existByName(String name);
}
