package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Station;

public interface StationDao {

    Long save(final Station station);

    List<Station> findAll();

    void deleteById(final Long stationId);

    Station findById(final Long stationId);

    boolean existByName(final Station station);

    boolean existById(final Long stationId);

    List<Station> findByIds(final List<Long> ids);
}
