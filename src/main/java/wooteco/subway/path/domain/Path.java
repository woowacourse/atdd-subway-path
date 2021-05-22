package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final List<Station> stations;

    public Path(List<Station> stations, List<Section> sections) {
        this.stations = new ArrayList<>(stations);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> calculateShortestPath(Long sourceId, Long targetId) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        Station source = getStation(sourceId);
        Station target = getStation(targetId);

        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private Station getStation(Long id) {
        return this.stations.stream()
                .filter(station -> station.hasSameId(id))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int calculateShortestDistance(Long sourceId, Long targetId) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        Station source = getStation(sourceId);
        Station target = getStation(targetId);

        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }
}
