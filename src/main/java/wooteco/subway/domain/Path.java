package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(Sections sections) {
        dijkstraShortestPath = getDijkstraShortestPath(sections);
    }

    public int getShortestDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public List<Station> getShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath(Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.getStations()) {
            graph.addVertex(station);
        }
        sections.forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );

        return new DijkstraShortestPath<>(graph);
    }
}
