package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public Path(Lines lines) {
        this.shortestPath = new DijkstraShortestPath<>(graph(lines));
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> graph(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.allStations()
                .forEach(graph::addVertex);
        lines.allSections()
                .forEach(section ->
                        graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                                section.getDownStation()),
                                section.getDistance()));

        return graph;
    }

    public List<Station> shortestPath(Station sourceStation, Station targetStation) {
        return shortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int distance(Station sourceStation, Station targetStation) {
        return (int) shortestPath.getPath(sourceStation, targetStation).getWeight();
    }
}
