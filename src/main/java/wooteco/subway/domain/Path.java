package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int getShortestDistance(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = getDijkstraShortestPath();

        return (int)dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public List<Station> getShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = getDijkstraShortestPath();

        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private DijkstraShortestPath getDijkstraShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.getStations()) {
            graph.addVertex(station);
        }
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistance()));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath;
    }
}
