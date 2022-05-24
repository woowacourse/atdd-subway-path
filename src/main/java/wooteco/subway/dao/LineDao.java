package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;

public interface LineDao {
    Line save(Line line, Fare fare);

    List<Line> findAll();

    Line findById(Long id);

    Fare findFareById(Long id);

    int update(Line line);

    int delete(Long id);
}
