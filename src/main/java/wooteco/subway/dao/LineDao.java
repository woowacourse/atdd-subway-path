package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Line;

public interface LineDao {

    Line save(Line line);

    boolean existByName(String name);

    List<Line> findAll();

    Line findById(Long id);

    int update(Line line);

    int delete(Long id);

    void deleteByExistName(String name);
}
