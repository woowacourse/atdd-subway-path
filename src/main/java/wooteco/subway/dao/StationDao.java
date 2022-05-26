package wooteco.subway.dao;

import wooteco.subway.domain.Station;

import java.util.List;

public interface StationDao {

    Station save(Station station);

    Station findById(Long id);

    List<Station> findAll();

    List<Station> findByIdIn(List<Long> ids);

    void deleteById(Long id);
}
