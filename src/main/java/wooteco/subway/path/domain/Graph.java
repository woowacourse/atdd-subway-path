package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Graph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public Graph(List<Section> sections) {
        initPath(sections);
    }

    private void initPath(List<Section> sections) {
        for (Section section : sections) {
            initEdge(section);
        }
    }

    private void initEdge(Section section) {
        addVertex(section.getUpStation());
        addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    private void addVertex(Station station) {
        if (!graph.containsVertex(station)) {
            graph.addVertex(station);
        }
    }

    public Path shortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return new Path(dijkstraShortestPath.getPath(source, target).getVertexList(), dijkstraShortestPath.getPathWeight(source, target));
    }
}
