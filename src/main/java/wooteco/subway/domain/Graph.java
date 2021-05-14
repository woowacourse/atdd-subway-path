package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final List<Section> allSections;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final Station sourceStation;
    private final Station targetStation;

    public Graph(List<Section> allSections, Station sourceStation, Station targetStation) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.allSections = allSections;
        initGraph();
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
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

    public List<Station> getShortestPath() {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int getTotalDistance() {
        return (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
    }
}
