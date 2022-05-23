package wooteco.subway.repository;

import java.util.List;
import wooteco.subway.domain.Line;

public interface LineRepository {

    Long save(Line line);

    List<Line> findAll();

    boolean deleteById(Long id);

    Line findById(Long id);

    boolean updateById(Line line);

    boolean existsByName(String name);
}
