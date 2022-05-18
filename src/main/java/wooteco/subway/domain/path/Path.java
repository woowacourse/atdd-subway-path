package wooteco.subway.domain.path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;

public class Path {
    private final Graph<Long, DefaultWeightedEdge> graph;
    private final PathFindingStrategy pathFindingStrategy;

    public Path(List<Station> stations, List<Section> sections, PathFindingStrategy pathFindingStrategy){
        this.graph = generateGraph(stations, sections);
        this.pathFindingStrategy = pathFindingStrategy;
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> generateGraph(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        for (Section section : sections) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), section.getDistance());
        }
        
        return graph;
    }

    public int calculateShortestDistance(Long source, Long target) {
        return pathFindingStrategy.calculateShortestDistance(graph, source, target);
    }

    public List<Long> getShortestPath(Long source, Long target) {
        return pathFindingStrategy.getShortestPath(graph, source, target);
    }
}
