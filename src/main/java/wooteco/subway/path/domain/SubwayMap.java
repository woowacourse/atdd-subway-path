package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;

import java.util.List;

public class SubwayMap {
    private final WeightedGraph<Long, DefaultWeightedEdge> subwayMap
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> map
            = new DijkstraShortestPath<>(subwayMap);

    public SubwayMap(List<Line> lines) {
        initSubwayMap(lines);
    }

    private void initSubwayMap(List<Line> lines) {
        for (Line line : lines) {
            addAllStationIds(line);
            Sections sections = line.getSections();
            addAllSectionInfo(sections);
        }
    }

    private void addAllStationIds(Line line) {
        line.getAllStationIds().forEach(subwayMap::addVertex);
    }

    private void addAllSectionInfo(Sections sections) {
        sections.getSections()
                .forEach(section -> subwayMap.setEdgeWeight(connectStation(section), section.getDistance()));
    }

    private DefaultWeightedEdge connectStation(Section section) {
        Long upStationId = section.upStationId();
        Long downStationId = section.downStationId();
        return subwayMap.addEdge(upStationId, downStationId);
    }

    public int getShortestDistance(Long source, Long target) {
        return (int) map.getPathWeight(source, target);
    }

    public List<Long> getShortestPathIds(Long source, Long target) {
        return map.getPath(source, target).getVertexList();
    }
}
