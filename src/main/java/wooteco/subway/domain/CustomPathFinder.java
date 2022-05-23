package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.PathException;

public class CustomPathFinder implements PathFinder {
    private final WeightedMultigraph<Station, SectionEdge> graph;

    public CustomPathFinder(WeightedMultigraph<Station, SectionEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder of(List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        Set<Station> stations = getDistinctStations(sections);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> setWeightedEdgeFromSection(graph, section));
        return new CustomPathFinder(graph);
    }

    private static Set<Station> getDistinctStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();

        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });
        return stations;
    }

    private static void setWeightedEdgeFromSection(WeightedMultigraph<Station, SectionEdge> graph,
                                                   Section section) {
        graph.addEdge(section.getUpStation(), section.getDownStation(), new SectionEdge(section));
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

    @Override
    public List<Section> calculateSections(Station from, Station to) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            List<SectionEdge> sectionEdges = dijkstraShortestPath.getPath(from, to).getEdgeList();
            return sectionEdges.stream().map(SectionEdge::getSection).collect(Collectors.toList());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new PathException(ExceptionMessage.NOT_FOUND_PATH.getContent());
        }
    }
}
