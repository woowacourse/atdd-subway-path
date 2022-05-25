package wooteco.subway.domain.section;

import java.util.List;
import wooteco.subway.domain.station.Station;

public interface SortStrategy {

    List<Station> sort(List<Section> sections, List<Station> stations);
}
