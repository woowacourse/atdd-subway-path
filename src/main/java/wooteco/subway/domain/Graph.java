package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph {
    private final List<Section> allSections;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Graph(List<Section> allSections) {
        this.allSections = allSections;
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        initGraph();
    }

    private void initGraph() {
        List<Station> uniqueStations = getUniqueStations();
        initVertices(uniqueStations);
        initEdges();
    }

    private List<Station> getUniqueStations() {
        Set<Station> uniqueStations = new HashSet<>();
        for (Section section : allSections) {
            uniqueStations.add(section.getUpStation());
            uniqueStations.add(section.getDownStation());
        }
        return new ArrayList<>(uniqueStations);
    }

    private void initVertices(List<Station> uniqueStations) {
        for (Station uniqueStation : uniqueStations) {
            graph.addVertex(uniqueStation);
        }
    }

    private void initEdges() {
        for (Section section : allSections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> getShortestPath(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int getTotalDistance(Station sourceStation, Station targetStation) {
        return (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
    }
}
