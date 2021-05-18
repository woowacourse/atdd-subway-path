package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final List<Section> allSections;

    public Graph(List<Section> allSections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.allSections = allSections;
        initGraph();
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void initGraph() {
        Set<Station> uniqueStations = getUniqueStations();
        initVertices(uniqueStations);
        initEdges();
    }

    private Set<Station> getUniqueStations() {
        Set<Station> uniqueStations = new HashSet<>();
        for (Section section : allSections) {
            uniqueStations.add(section.getUpStation());
            uniqueStations.add(section.getDownStation());
        }
        return uniqueStations;
    }

    private void initVertices(Set<Station> uniqueStations) {
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
