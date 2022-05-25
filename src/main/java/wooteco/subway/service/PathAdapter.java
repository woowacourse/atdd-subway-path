package wooteco.subway.service;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

public class PathAdapter implements PathFinder {

    private final Path path;
    private final GraphPath<Long, DefaultWeightedEdge> shortestPath;

    public PathAdapter(Path path) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph(path.getStationIds(), path.getSections());
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(path.getSource(), path.getTarget());
        validateExistPath(shortestPath);
        this.path = path;
        this.shortestPath = shortestPath;
    }

    private static void validateExistPath(GraphPath<Long, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 수 없습니다");
        }
    }

    private static WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Long> stationId, Sections sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(stationId, graph);
        addEdges(sections, graph);
        return graph;
    }

    private static void addVertexes(List<Long> stationId, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Long id : stationId) {
            graph.addVertex(id);
        }
    }

    private static void addEdges(Sections sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }

    @Override
    public List<Section> getShortestPathSections() {
        return path.getShortestPathSections(shortestPath.getVertexList());
    }

    @Override
    public int getTotalDistance() {
        return (int) shortestPath.getWeight();
    }

    @Override
    public List<Long> getShortestPath() {
        return shortestPath.getVertexList();
    }
}
