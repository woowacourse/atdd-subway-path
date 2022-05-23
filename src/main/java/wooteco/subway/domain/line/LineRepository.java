package wooteco.subway.domain.line;

import java.util.List;

public interface LineRepository {
    List<Line> findAll();

    Line save(Line line, long upStationId, long downStationId, int distance);

    Line find(long id);

    void update(Line line);

    void delete(long id);
}
