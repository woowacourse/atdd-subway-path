package wooteco.subway.domain.line;

import java.util.List;

public interface LineRepository {

    Line save(Line line);

    List<Line> getAll();

    Line getById(long lineId);

    Line update(Line line);

    void updateSections(Line line);

    void remove(long lineId);

    boolean existsByName(String name);

    boolean existsByColor(String color);

    boolean existsSectionByStationId(long stationId);
}
