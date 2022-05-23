package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final WeightedMultigraph<Station, ShortestPathEdge> graph;

    public Path() {
        this.graph = new WeightedMultigraph<>(ShortestPathEdge.class);
    }

    public void addAllStations(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    public void addSection(Station upStation, Station downStation, Long lineId, int distance) {
        graph.addEdge(upStation, downStation, new ShortestPathEdge(lineId, distance));
    }

    public List<Station> getStations(Station upStation, Station downStation) {
        GraphPath<Station, ShortestPathEdge> graphPath = findGraphPath(upStation, downStation);
        return graphPath.getVertexList();
    }

    public int getShortestDistance(Station upStation, Station downStation) {
        GraphPath<Station, ShortestPathEdge> graphPath = findGraphPath(upStation, downStation);
        return (int) graphPath.getWeight();
    }

    public List<Long> getPassedLineIds(Station upStation, Station downStation) {
        GraphPath<Station, ShortestPathEdge> graphPath = findGraphPath(upStation, downStation);

        return graphPath.getEdgeList().stream()
                .map(ShortestPathEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    private GraphPath<Station, ShortestPathEdge> findGraphPath(Station upStation, Station downStation) {
        DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, ShortestPathEdge> path = dijkstraShortestPath.getPath(upStation, downStation);
        validatePathExist(path);

        return path;
    }

    private void validatePathExist(GraphPath<Station, ShortestPathEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }
}
