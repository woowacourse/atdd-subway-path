package wooteco.subway.domain.path;

import java.util.Comparator;
import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.SectionNotFoundException;

public class Path {

    private final Station startStation;
    private final Station endStation;
    private final ShortestPathAlgorithm<Station, ShortestPathEdge> pathAlgorithm;

    public Path(Station startStation, Station endStation,
            ShortestPathAlgorithm<Station, ShortestPathEdge> pathAlgorithm) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.pathAlgorithm = pathAlgorithm;
    }

    public List<ShortestPathEdge> findEdges() {
        try {
            return pathAlgorithm.getPath(startStation, endStation).getEdgeList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public int calculateMinDistance() {
        try {
            return (int) pathAlgorithm.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public List<Station> findShortestStations() {
        try {
            return pathAlgorithm.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public int findMaxExtraLineFare(final List<Line> lines) {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(o -> o))
                .orElse(0);
    }
}
