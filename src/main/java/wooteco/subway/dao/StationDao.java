package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;
import wooteco.subway.domain.Station;

public interface StationDao {

    Station save(Station station);

    Optional<Station> findById(Long id);

    List<Station> findAll();

    boolean hasStation(String name);

    void deleteById(Long id);
}
