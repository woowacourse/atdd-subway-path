package wooteco.subway.Infrastructure.line;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LineDao {
    long save(Line line);

    List<Line> findAll();

    Optional<Line> findById(Long id);

    List<Line> findByIdIn(Collection<Long> lineIds);

    boolean existById(Long id);

    boolean existByName(String name);

    boolean existByColor(String color);

    void update(Line line);

    void deleteById(Long id);

    void deleteAll();
}
