package wooteco.subway.admin.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.PathSearchType;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.exception.LineNotConnectedException;

import java.util.List;
import java.util.Objects;

// 가장 짧은 경로를 생성하는 책임
public class PathMaker {
    private final LineStations lineStations;
    private final Stations stations;

    public PathMaker(LineStations lineStations, Stations stations) {
        this.lineStations = lineStations;
        this.stations = stations;
    }

    public Path computeShortestPath(Long sourceStationId, Long targetStationId, PathSearchType type) {
        StationGraph pathGraph = StationGraph.of(lineStations, type);
        List<Long> shortestPathStationIds = getShortestPathStationIds(pathGraph, sourceStationId, targetStationId);
        Stations shortestPathStations = this.stations.listOf(shortestPathStationIds);

        return Path.of(lineStations, shortestPathStations);
    }

    private List<Long> getShortestPathStationIds(StationGraph pathGraph, Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(pathGraph);
        GraphPath path = dijkstraShortestPath.getPath(sourceStationId, targetStationId);

        validateGraphPathOfNull(path);

        return path.getVertexList();
    }

    private void validateGraphPathOfNull(GraphPath path) {
        if (Objects.isNull(path)) {
            throw new LineNotConnectedException();
        }
    }
}
