package wooteco.subway.domain;

import lombok.Getter;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Path {
    private final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(List<Station> stations, List<Section> sections){
        dijkstraShortestPath = new DijkstraShortestPath<>(generateGraph(stations, sections));
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> generateGraph(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(String.valueOf(station.getId()));
        }

        for (Section section : sections) {
            String upStation = String.valueOf(section.getUpStationId());
            String downStation = String.valueOf(section.getDownStationId());
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
        return graph;
    }

    public int calculateShortestDistance(Long source, Long target) {
        return (int) dijkstraShortestPath.getPathWeight(String.valueOf(source), String.valueOf(target));
    }

    public List<Long> getShortestPath(Long source, Long target) {
        return dijkstraShortestPath
                .getPath(String.valueOf(source), String.valueOf(target))
                .getVertexList()
                .stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
