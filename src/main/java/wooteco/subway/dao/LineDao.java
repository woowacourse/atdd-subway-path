package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.Line;

public interface LineDao {
    Line save(Line line, Fare extraFare);

    List<Line> findAll();

    Line findById(Long id);

    Fare findExtraFareById(Long id);

    int update(Line line, Fare extraFare);

    int delete(Long id);
}
