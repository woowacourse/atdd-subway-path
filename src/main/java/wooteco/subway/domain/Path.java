package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final Stations stations;
    private final Sections sections;
    private final DijkstraShortestPath dijkstraShortestPath;

    public Path(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
        this.dijkstraShortestPath = initGraphSetting();
    }

    private DijkstraShortestPath initGraphSetting() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Station> stationsGroup = stations.toList();
        List<Section> sectionsGroup = sections.toList();

        for (Station station : stationsGroup) {
            graph.addVertex(station);
        }

        for (Section section : sectionsGroup) {
            Long upStationId = section.getUpStation().getId();
            Long downStationId = section.getDownStation().getId();

            graph.setEdgeWeight(graph.addEdge(
                stations.findStationById(upStationId),
                stations.findStationById(downStationId)),
                section.getDistance());
        }
        return new DijkstraShortestPath(graph);
    }

    public List<Station> calculateShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double calculateShortestDistance(Station source, Station target) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }
}
