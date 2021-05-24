package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;
import wooteco.subway.exception.path.InvalidPathException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.ShortestPathStrategy;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Component
public class PathFinder {

    private SubwayGraph subwayGraph;

    public SubwayPath findPath(List<Line> lines, Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathException(source, target);
        }
        if (subwayGraph == null) {
            subwayGraph = initSubwayGraph(lines);
        }

        ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm = ShortestPathStrategy.DIJKSTRA.match(subwayGraph);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPathAlgorithm.getPath(source, target);
        if (path == null) {
            throw new InvalidPathException(source, target);
        }
        return new SubwayPath(path);
    }

    private SubwayGraph initSubwayGraph(List<Line> lines) {
        SubwayGraph graph = new SubwayGraph(DefaultWeightedEdge.class);
        graph.addVertices(lines);
        graph.addEdges(lines);
        return graph;
    }

    public void updateGraph(List<Line> lines) {
        subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertices(lines);
        subwayGraph.addEdges(lines);
    }
}
