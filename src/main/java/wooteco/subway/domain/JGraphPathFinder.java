package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class JGraphPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public JGraphPathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static JGraphPathFinder of(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Set<Station> stations = getDistinctStations(sections);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> setWeightedEdgeFromSection(graph, section));
        return new JGraphPathFinder(graph);
    }

    private static void setWeightedEdgeFromSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
    }

    private static Set<Station> getDistinctStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();

        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });
        return stations;
    }

    @Override
    public int calculateDistance(Station from, Station to) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return (int) dijkstraShortestPath.getPath(from, to).getWeight();
    }
}
