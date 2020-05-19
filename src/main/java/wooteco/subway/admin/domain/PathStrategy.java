package wooteco.subway.admin.domain;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

public interface PathStrategy {

    GraphPath<Long, LineStationEdge> getPath(Long sourceId, Long targetId,
            Graph<Long, LineStationEdge> graph);
}
