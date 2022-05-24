package wooteco.subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;

import java.util.List;
import java.util.Set;

public class PathFinder {

    private Sections sections;
    private DijkstraShortestPath<Long, SectionWeightedEdge> dijkstraShortestPath;

    public PathFinder(Sections sections) {
        this.sections = sections;
        this.dijkstraShortestPath = initDijkstraShortestPath();
    }

    public List<Long> findPath(Long from, Long to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public int findDistance(Long from, Long to) {
        return (int) dijkstraShortestPath.getPathWeight(from, to);
    }

    public List<SectionWeightedEdge> findEdges(Long from, Long to) {
        return dijkstraShortestPath.getPath(from, to).getEdgeList();
    }

    private DijkstraShortestPath<Long, SectionWeightedEdge> initDijkstraShortestPath() {
        WeightedMultigraph<Long, SectionWeightedEdge> graph = new WeightedMultigraph<>(SectionWeightedEdge.class);
        Set<Long> stationIds = sections.distinctStationIds();
        fillVertexes(graph, stationIds);
        fillEdges(graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void fillVertexes(WeightedMultigraph<Long, SectionWeightedEdge> graph, Set<Long> stationIds) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private void fillEdges(WeightedMultigraph<Long, SectionWeightedEdge> graph) {
        for (Section section : sections.values()) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();

            SectionWeightedEdge sectionWeightedEdge =
                    new SectionWeightedEdge(section.getLineId(), section.getDistance());
            graph.addEdge(upStationId, downStationId, sectionWeightedEdge);
        }
    }
}
