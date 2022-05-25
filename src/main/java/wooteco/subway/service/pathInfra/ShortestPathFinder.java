package wooteco.subway.service.pathInfra;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPathFinder implements PathFinder {
    private final WeightedMultigraph<Station, ShortestPathEdge> graph;

    public ShortestPathFinder() {
        this.graph = new WeightedMultigraph<>(ShortestPathEdge.class);
    }

    @Override
    public Path findShortestPathByGraph(Station source, Station target) {
        final GraphPath<Station, ShortestPathEdge> graphPath = new DijkstraShortestPath<>(graph).getPath(source, target);
        validatePathExist(graphPath);
        return makePath(graphPath);
    }

    @Override
    public void addVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    @Override
    public void addEdge(Station upStation, Station downStation, Section section) {
        final Long lineId = section.getLineId();
        final int distance = section.getDistance();
        graph.addEdge(upStation, downStation, new ShortestPathEdge(lineId, distance));
    }

    private void validatePathExist(GraphPath<Station, ShortestPathEdge> graphPath) {
        if (graphPath == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }

    private Path makePath(GraphPath<Station, ShortestPathEdge> graphPath) {
        final List<Station> stations = graphPath.getVertexList();
        final List<Long> lineIds = graphPath.getEdgeList()
                .stream()
                .map(ShortestPathEdge::getLineId)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        final int distance = (int) graphPath.getWeight();
        return new Path(stations, lineIds, distance);
    }
}
