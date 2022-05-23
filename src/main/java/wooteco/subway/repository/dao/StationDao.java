package wooteco.subway.repository.dao;

import java.util.List;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.entity.StationEntity;

public interface StationDao {

    Long save(Station station);

    List<StationEntity> findAll();

    boolean deleteById(Long id);

    boolean existsByName(String name);

    StationEntity findById(Long id);

    List<StationEntity> findById(List<Long> ids);
}
