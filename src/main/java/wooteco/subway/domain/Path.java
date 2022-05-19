package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public Path(final List<Section> sections) {
        path = initPath(sections);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initPath(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> findRoute(final Station source, final Station target) {
        checkRouteExist(source, target);
        return path.getPath(source, target).getVertexList();
    }

    public int calculateDistance(final Station source, final Station target) {
        checkRouteExist(source, target);
        return (int) path.getPath(source, target).getWeight();
    }

    private void checkRouteExist(final Station source, final Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = this.path.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("이동 가능한 경로가 존재하지 않습니다");
        }
    }

    private void initGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                           final List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }
}
