package wooteco.subway.domain.path;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;

@Component
public class DijkstraPathCalculator implements PathCalculator {

    @Override
    public Path calculatePath(Station source, Station target, List<Line> inputLines) {
        Lines lines = new Lines(inputLines);
        Set<Station> stations = lines.extractStations();
        WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        lines.addEdge(graph);

        DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        List<Station> vertexes = calculateShortestPath(source, target, dijkstraShortestPath);
        List<Long> lineIds = calculateShortestPathLines(source, target, dijkstraShortestPath);
        double distance = calculateShortestDistance(source, target, dijkstraShortestPath);
        return new Path(vertexes, lineIds, distance);
    }

    private List<Station> calculateShortestPath(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private List<Long> calculateShortestPathLines(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        List<ShortestPathEdge> edges = dijkstraShortestPath.getPath(source, target).getEdgeList();
        return edges.stream()
                .map(ShortestPathEdge::getLineId)
                .collect(Collectors.toList());
    }

    private double calculateShortestDistance(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }
}
