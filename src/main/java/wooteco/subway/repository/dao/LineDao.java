package wooteco.subway.repository.dao;

import java.util.List;
import java.util.Optional;
import wooteco.subway.domain.Line;
import wooteco.subway.repository.entity.LineEntity;

public interface LineDao {

    Long save(Line line);

    List<LineEntity> findAll();

    boolean deleteById(Long id);

    Optional<LineEntity> findById(Long id);

    boolean updateById(Line line);

    boolean existsByName(String name);
}
