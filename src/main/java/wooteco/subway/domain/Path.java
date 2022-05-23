package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public void addAllStations(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance);
    }

    public List<Station> getStations(Station upStation, Station downStation){
        GraphPath<Station, DefaultWeightedEdge> graphPath = findGraphPath(upStation, downStation);
        return graphPath.getVertexList();
    }

    public int getShortestDistance(Station upStation, Station downStation){
        GraphPath<Station, DefaultWeightedEdge> graphPath = findGraphPath(upStation, downStation);
        return (int) graphPath.getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> findGraphPath(Station upStation, Station downStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(upStation, downStation);
        validatePathExist(path);

        return path;
    }

    private void validatePathExist(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }
}
