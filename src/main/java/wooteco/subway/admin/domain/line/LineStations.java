package wooteco.subway.admin.domain.line;

import java.util.Set;

import wooteco.subway.admin.domain.line.path.EdgeWeightStrategy;
import wooteco.subway.admin.domain.line.path.SubwayGraph;
import wooteco.subway.admin.domain.line.path.SubwayMap;

public class LineStations {
    private final Set<LineStation> lineStations;

    public LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public SubwayMap toGraph(EdgeWeightStrategy edgeWeightStrategy) {
        return SubwayGraph.of(lineStations, edgeWeightStrategy);
    }
}
