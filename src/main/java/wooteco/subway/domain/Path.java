package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path(List<Section> sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(sections);
        addEdge(sections);
    }

    public List<Station> createShortestPath(Station upStation, Station downStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            return dijkstraShortestPath.getPath(upStation, downStation).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("최단 경로를 요청하신 역이 구간에 존재하지 않습니다.");
        }
    }

    public int calculateDistance(Station upStation, Station downStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) (dijkstraShortestPath.getPath(upStation, downStation).getWeight());
    }

    private void addVertex(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
        }
    }

    private void addEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                    section.getDownStation()), section.getDistance());
        }
    }
}
