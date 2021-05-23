package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path(Sections sections) {
        this.graph = createGraph(sections);
    }

    public List<Station> findPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int findDistance(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(Sections sections) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : sections.getAllStations()) {
            graph.addVertex(station);
        }

        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return graph;
    }
}
