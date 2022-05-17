package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Station;

public interface StationDao {

    Long save(Station station);

    List<Station> findAll();

    boolean deleteById(Long id);

    boolean existsByName(String name);

    Station findById(Long id);

    List<Station> findById(List<Long> ids);
}
