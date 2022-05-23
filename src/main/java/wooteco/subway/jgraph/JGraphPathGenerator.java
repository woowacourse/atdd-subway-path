package wooteco.subway.jgraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathGenerator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;

@Component
public class JGraphPathGenerator implements PathGenerator {

    @Override
    public Path findPath(List<Section> sections, Station from, Station to) {
        try {
            WeightedMultigraph<Station, DistanceWithFareEdge> graph = getGraph(sections);
            GraphPath<Station, DistanceWithFareEdge> graphPath = new DijkstraShortestPath<>(graph).getPath(from, to);
            return Path.of(graphPath.getVertexList(), findExtraFees(graphPath), (int) graphPath.getWeight());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new DomainException(ExceptionMessage.NOT_FOUND_PATH.getContent());
        }
    }

    private WeightedMultigraph<Station, DistanceWithFareEdge> getGraph(List<Section> sections) {
        WeightedMultigraph<Station, DistanceWithFareEdge> graph = new WeightedMultigraph<>(DistanceWithFareEdge.class);
        Set<Station> stations = getDistinctStations(sections);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> setWeightedEdgeFromSection(graph, section));
        return graph;
    }

    private void setWeightedEdgeFromSection(WeightedMultigraph<Station, DistanceWithFareEdge> graph,
                                            Section section) {
        DistanceWithFareEdge edge = new DistanceWithFareEdge(section.getDistance(), section.getExtraFare());
        graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        graph.setEdgeWeight(edge, section.getDistance());
    }

    private Set<Station> getDistinctStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();

        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });
        return stations;
    }

    private List<Long> findExtraFees(GraphPath<Station, DistanceWithFareEdge> graphPath) {
        return graphPath.getEdgeList().stream()
                .map(DistanceWithFareEdge::getFare)
                .collect(Collectors.toList());
    }
}
