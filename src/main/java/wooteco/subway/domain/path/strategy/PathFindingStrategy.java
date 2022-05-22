package wooteco.subway.domain.path.strategy;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public interface PathFindingStrategy {
    GraphPath<Station, Section> findPathBetween(Graph<Station, Section> graph, Station source, Station target);
}
