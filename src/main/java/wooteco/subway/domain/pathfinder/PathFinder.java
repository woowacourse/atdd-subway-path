package wooteco.subway.domain.pathfinder;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public interface PathFinder {

    int calculateDistance(Station from, Station to);

    List<Station> calculatePath(Station from, Station to);

    List<Section> calculateSections(Station from, Station to);
}
