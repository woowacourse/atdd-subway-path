package wooteco.subway.path.infra;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.exception.path.InvalidPathException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class DijkstraPathFinder implements PathFinder {

    private SubwayGraph subwayGraph;
    private ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm;

    public DijkstraPathFinder(SubwayGraph subwayGraph, ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm) {
        this.subwayGraph = subwayGraph;
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    @Override
    public SubwayPath findPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathException(source, target);
        }
        GraphPath<Station, DefaultWeightedEdge> path = shortestPathAlgorithm.getPath(source, target);

        if (path == null) {
            throw new InvalidPathException(source, target);
        }
        return new SubwayPath(path);
    }

    @Override
    public void updateGraph(List<Line> lines) {
        subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertices(lines);
        subwayGraph.addEdges(lines);
        shortestPathAlgorithm = new DijkstraShortestPath<>(subwayGraph);
        subwayGraph.vertexSet().stream()
                .map(Station::getName)
                .forEach(it -> System.out.println("역 : " + it));
    }
}
