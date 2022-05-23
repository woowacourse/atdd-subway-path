package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.graphpath.ShortestGraphPath;
import wooteco.subway.domain.path.graphpath.SubwayGraphPath;
import wooteco.subway.domain.path.graphpath.WeightedEdgeWithLineId;

public class Path {

    private final List<Line> lines;
    private final SubwayGraphPath<Station, WeightedEdgeWithLineId> path;

    public Path(List<Line> lines, Station source, Station target) {
        this.lines = List.copyOf(lines);
        this.path = new ShortestGraphPath(lines, source, target);
    }

    public List<Station> getStations() {
        return path.getVertexList();
    }

    public int getDistance() {
        return (int)path.getWeight();
    }

    public int getExtraFare() {
        List<WeightedEdgeWithLineId> edgeList = path.getEdgeList();
        return lines.stream()
            .filter(line -> isLineUsed(edgeList, line))
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
    }

    private boolean isLineUsed(List<WeightedEdgeWithLineId> edgeList, Line line) {
        return edgeList.stream()
            .anyMatch(edge -> line.isSameId(edge.getLineId()));
    }
}
