package wooteco.subway.admin.domain.line;

import wooteco.subway.admin.domain.line.path.EdgeWeightStrategy;
import wooteco.subway.admin.domain.line.path.SubwayGraph;

import java.util.Set;

public class LineStations {
    private final Set<LineStation> lineStations;

    public LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public SubwayGraph toGraph(EdgeWeightStrategy edgeWeightStrategy) {
        return SubwayGraph.of(lineStations, edgeWeightStrategy);
    }
}
