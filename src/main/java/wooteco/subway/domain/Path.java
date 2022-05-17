package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;

    public Path(Sections sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertex(sections);
        addEdge(sections);
    }

    public List<String> createShortestPath(Station upStation, Station downStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<String> shortestPath = dijkstraShortestPath.getPath(upStation.getName(),
                downStation.getName()).getVertexList();

        return shortestPath;
    }

    public int calculateDistance(Station upStation, Station downStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return (int)(dijkstraShortestPath.getPath(upStation.getName(), downStation.getName()).getWeight());
    }

    private void addVertex(Sections sections) {
        for (Station station : sections.getStations()) {
            graph.addVertex(station.getName());
        }
    }

    private void addEdge(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(),
                    section.getDownStation().getName()), section.getDistance());
        }
    }
}
