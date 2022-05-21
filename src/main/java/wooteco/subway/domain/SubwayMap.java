package wooteco.subway.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayMap {

    private final DijkstraShortestPath<Station, SectionWeightedEdge> graph;

    public SubwayMap(final List<Section> sections) {
        graph = initGraph(sections);
    }

    public Path calculatePath(final Station source, final Station target) {
        checkReachable(source, target);
        List<Station> stations = calculatePassingStations(source, target);
        int distance = calculateDistance(source, target);

        HashSet<Long> passingLineIds = new HashSet<>();
        GraphPath<Station, SectionWeightedEdge> path = graph.getPath(source, target);
        for (final SectionWeightedEdge sectionWeightedEdge : path.getEdgeList()) {
            passingLineIds.add(sectionWeightedEdge.getIncludedLineId());
        }

        return new Path(stations, distance, passingLineIds);
    }

    private List<Station> calculatePassingStations(final Station source, final Station target) {
        if (source.equals(target)) {
            return Collections.emptyList();
        }
        return graph.getPath(source, target).getVertexList();
    }

    private int calculateDistance(final Station source, final Station target) {
        return (int) graph.getPath(source, target).getWeight();
    }

    private void checkReachable(final Station source, final Station target) {
        GraphPath<Station, SectionWeightedEdge> path = this.graph.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("이동 가능한 경로가 존재하지 않습니다");
        }
    }

    private DijkstraShortestPath<Station, SectionWeightedEdge> initGraph(List<Section> sections) {
        WeightedMultigraph<Station, SectionWeightedEdge> graph
                = new WeightedMultigraph<>(SectionWeightedEdge.class);
        addSections(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    private void addSections(final WeightedMultigraph<Station, SectionWeightedEdge> graph,
                           final List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            SectionWeightedEdge edge = new SectionWeightedEdge(section.getLineId());
            graph.addEdge(upStation, downStation, edge);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    private static class SectionWeightedEdge extends DefaultWeightedEdge {
        private final Long includedLineId;

        public SectionWeightedEdge(final Long includedLineId) {
            this.includedLineId = includedLineId;
        }

        public Long getIncludedLineId() {
            return includedLineId;
        }
    }
}
