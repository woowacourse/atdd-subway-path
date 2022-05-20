package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.PathException;

public class JGraphPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private JGraphPathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder of(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Set<Station> stations = getDistinctStations(sections);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> setWeightedEdgeFromSection(graph, section));
        return new JGraphPathFinder(graph);
    }

    private static void setWeightedEdgeFromSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                                   Section section) {
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
        try {
            return (int) dijkstraShortestPath.getPath(from, to).getWeight();
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new PathException(ExceptionMessage.NOT_FOUND_PATH.getContent());
        }
    }

    @Override
    public List<Station> calculatePath(Station from, Station to) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(from, to).getVertexList();
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new PathException(ExceptionMessage.NOT_FOUND_PATH.getContent());
        }
    }
}
