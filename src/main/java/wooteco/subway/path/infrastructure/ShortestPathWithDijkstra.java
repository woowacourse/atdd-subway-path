package wooteco.subway.path.infrastructure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.exception.PathNotFoundException;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.station.domain.Station;

import java.util.HashSet;
import java.util.Set;

@Component
public class ShortestPathWithDijkstra implements ShortestPath {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public ShortestPathWithDijkstra(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void resetGraph(Sections sections) {
        removeAllVertexes();
        for (Section section : sections.getSections()) {
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
        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
            return new Path(path.getVertexList(), (int) path.getWeight());
        } catch (IllegalArgumentException e) {
            throw new PathNotFoundException();
        }
    }
}
