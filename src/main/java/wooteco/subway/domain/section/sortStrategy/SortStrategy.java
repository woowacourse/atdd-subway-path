package wooteco.subway.domain.section.sortStrategy;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;

public interface SortStrategy {
    List<Station> sort(List<Section> sections);
}
