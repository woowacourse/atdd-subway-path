package wooteco.subway.dao;

import wooteco.subway.domain.Station;

import java.util.List;

public interface StationDao {

    Station save(Station station);

    boolean isExistById(Long id);

    boolean isExistByName(String name);

    Station findById(long id);

    List<Station> findByIds(List<Long> stationId);

    List<Station> findAll();

    int delete(long id);
}
