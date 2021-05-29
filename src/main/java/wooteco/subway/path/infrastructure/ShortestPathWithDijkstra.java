package wooteco.subway.path.infrastructure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.badrequest.InvalidPathException;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShortestPathWithDijkstra implements ShortestPath {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public ShortestPathWithDijkstra(List<Section> sections) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        resetGraph(sections);
    }

    @Override
    public void resetGraph(List<Section> sections) {
        removeAllVertexes();
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void removeAllVertexes() {
        Set<Station> vertices = new HashSet<>(graph.vertexSet());
        graph.removeAllVertices(vertices);
    }

    @Override
    public Path getPath(Station source, Station target) {
        validatePath(source, target);
        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
            return new Path(path.getVertexList(), (int) path.getWeight());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidPathException("해당 경로를 찾을 수 없습니다.");
        }
    }

    private void validatePath(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidPathException("출발역과 도착역을 다르게 해서 경로를 조회해주세요.");
        }
    }
}
