package wooteco.subway.path.infrastructure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.section.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ShortestPath {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public ShortestPath(List<Section> sections) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        setVertexes(sections);
        setEdges(sections);
    }

    private void setVertexes(List<Section> sections) {
        getStationIds(sections).forEach(graph::addVertex);
    }

    private List<Station> getStationIds(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(toList());
    }

    private void setEdges(List<Section> sections) {
        sections.forEach(
                section -> graph.setEdgeWeight(
                        graph.addEdge(
                                section.getUpStation(),
                                section.getDownStation()
                        ),
                        section.getDistance()
                )
        );
    }

    public Statistics statistics(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);

        return new Statistics(path.getVertexList(), path.getWeight());
    }

    public static class Statistics {

        private final List<Station> shortestPath;
        private final int distance;

        public Statistics(List<Station> shortestPath, double distance) {
            this.shortestPath = shortestPath;
            this.distance = (int) distance;
        }

        public List<Station> getShortestPath() {
            return shortestPath;
        }

        public int getDistance() {
            return distance;
        }

    }

}
