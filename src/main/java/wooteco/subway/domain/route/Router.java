package wooteco.subway.domain.route;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Router {

    public Route findShortestRoute(List<Section> sections, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStations(sections, graph);
        addSections(sections, graph);

        DijkstraShortestPath<Station, Integer> dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, Integer> path = dijkstraShortestPath.getPath(source, target);

        List<Station> shortestPath = path.getVertexList();
        double pathDistance = path.getWeight();

        return new Route(shortestPath, pathDistance);
    }

    private List<Station> extractStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    private void addStations(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : extractStations(sections)) {
            graph.addVertex(station);
        }
    }

    private void addSections(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
