package wooteco.subway.dao.line;

import java.util.List;
import java.util.Set;
import wooteco.subway.domain.line.Line;

public interface LineDao {

    Long save(Line line);

    boolean existLineById(Long id);

    List<Line> findAll();

    Line findById(Long id);

    List<Line> findByIds(Set<Long> ids);

    void update(Line line);

    void delete(Long id);
}
