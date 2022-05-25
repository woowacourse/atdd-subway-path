package wooteco.subway.domain.line;

import java.util.List;

import wooteco.subway.domain.section.Section;

public interface LineRepository {
    Line save(Line line, long upStationId, long downStationId, int distance);

    List<Line> findAll();

    Line find(long id);

    void update(Line line);

    void delete(long id);

    void addSection(Line line, Section section);

    void deleteSection(Line line, Section section);
}
