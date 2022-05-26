package wooteco.subway.domain.path.graphpath;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class ShortestGraphPath implements SubwayGraphPath<Station, WeightedEdgeWithLineId> {

    private final GraphPath<Station, WeightedEdgeWithLineId> path;

    public ShortestGraphPath(List<Line> lines, Station source, Station target) {
        this.path = new DijkstraShortestPath<>(initGraph(lines)).getPath(source, target);
    }

    private Graph<Station, WeightedEdgeWithLineId> initGraph(List<Line> lines) {
        WeightedMultigraph<Station, WeightedEdgeWithLineId> graph = new WeightedMultigraph<>(
            WeightedEdgeWithLineId.class);

        Map<Long, List<Section>> sections = getSectionsByLineIds(lines);
        for (Long lineId : sections.keySet()) {
            addSectionWithLineId(graph, lineId, sections.get(lineId));
        }
        return graph;
    }

    private Map<Long, List<Section>> getSectionsByLineIds(List<Line> lines) {
        return lines.stream()
            .collect(Collectors.toMap(Line::getId, Line::getSections));
    }

    private void addSectionWithLineId(WeightedMultigraph<Station, WeightedEdgeWithLineId> graph, Long lineId,
        List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.addEdge(section.getUpStation(), section.getDownStation(), new WeightedEdgeWithLineId(lineId,
                section.getDistance()));
        }
    }

    @Override
    public List<Station> getVertexList() {
        return path.getVertexList();
    }

    @Override
    public List<WeightedEdgeWithLineId> getEdgeList() {
        return path.getEdgeList();
    }

    @Override
    public double getWeight() {
        return path.getWeight();
    }
}
