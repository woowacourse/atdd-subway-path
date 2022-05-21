package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Line;

public interface LineDao {

    Long save(final Line line);

    List<Line> findAll();

    void deleteById(final Long lineId);

    Line findById(final Long lineId);

    void update(final Long lineId, final Line line);

    boolean existByName(final Line line);

    boolean existByColor(final Line line);

    boolean existByNameExceptSameId(final Long lineId, final Line line);

    boolean existByColorExceptSameId(final Long lineId, final Line line);
}
