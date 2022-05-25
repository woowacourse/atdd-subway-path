package wooteco.subway.domain.line;

import java.util.List;

public interface LineRepository {
    Line save(Line line, long upStationId, long downStationId, int distance);

    List<Line> findAll();

    Line find(long id);

    void update(Line line);

    void delete(long id);
}
