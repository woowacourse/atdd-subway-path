package wooteco.subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final List<Line> lines;
    private final GraphPath<Station, WeightedEdgeWithLineId> path;

    public Path(List<Line> lines, Station source, Station target) {
        this.lines = List.copyOf(lines);
        this.path = new DijkstraShortestPath<>(initGraph(getSectionsByLineIds())).getPath(source, target);
    }

    public List<Station> getStations() {
        return path.getVertexList();
    }

    private Map<Long, List<Section>> getSectionsByLineIds() {
        return lines.stream()
            .collect(Collectors.toMap(Line::getId, Line::getSections));
    }

    private Graph<Station, WeightedEdgeWithLineId> initGraph(Map<Long, List<Section>> sections) {
        WeightedMultigraph<Station, WeightedEdgeWithLineId> graph = new WeightedMultigraph<>(
            WeightedEdgeWithLineId.class);
        for (Long lineId : sections.keySet()) {
            addSectionWithLineId(graph, lineId, sections.get(lineId));
        }
        return graph;
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

    public int getDistance() {
        return (int)path.getWeight();
    }

    public int getExtraFare() {
        List<WeightedEdgeWithLineId> edgeList = path.getEdgeList();
        return lines.stream()
            .filter(line -> isLineUsed(edgeList, line))
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
    }

    private boolean isLineUsed(List<WeightedEdgeWithLineId> edgeList, Line line) {
        return edgeList.stream()
            .anyMatch(edge -> line.isSameId(edge.getLineId()));
    }
}
