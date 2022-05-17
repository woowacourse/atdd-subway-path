package wooteco.subway.dao.line;

import java.util.List;
import wooteco.subway.domain.line.Line;

public interface LineDao {

    long save(Line line);

    boolean existLineById(Long id);

    List<Line> findAll();

    Line findById(Long id);

    void update(Line line);

    void delete(Long id);
}
