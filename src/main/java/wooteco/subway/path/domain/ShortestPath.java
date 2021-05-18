package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;

import java.util.List;

public class ShortestPath {
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public ShortestPath(List<Line> lines) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(initSubwayMap(lines));
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> initSubwayMap(List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            List<Long> stations = line.getStationIds();
            stations.forEach(subwayMap::addVertex);
            Sections sections = line.getSections();
            connectStation(subwayMap, sections);
        }
        return subwayMap;
    }

    private void connectStation(WeightedMultigraph<Long, DefaultWeightedEdge> subwayMap, Sections sections) {
        for (Section section : sections.getSections()) {
            subwayMap.setEdgeWeight(subwayMap.addEdge(section.upStationId(), section.downStationId()),
                    section.getDistance());
        }
    }

    public List<Long> getPath(Long sourceId, Long targetId) {
        return dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
    }

    public int distance(Long sourceId, Long targetId) {
        return (int) dijkstraShortestPath.getPath(sourceId, targetId).getWeight();
    }
}
