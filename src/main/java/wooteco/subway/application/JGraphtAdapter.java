package wooteco.subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Graph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class JGraphtAdapter implements Graph {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public JGraphtAdapter(List<Station> stations, List<Section> sections) {
        this.shortestPath = createShortestPath(createStationsMap(stations), sections);
    }

    private Map<Long, Station> createStationsMap(List<Station> stations) {
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, value -> value));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createShortestPath(
        Map<Long, Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations.values()) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            Station upStation = stations.get(section.getUpStationId());
            Station downStation = stations.get(section.getDownStationId());
            DefaultWeightedEdge edge = graph.addEdge(upStation, downStation);
            graph.setEdgeWeight(edge, section.getDistance());
        }

        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public Path search(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if (path == null) {
            return new Path(List.of(), 0);
        }
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

}
