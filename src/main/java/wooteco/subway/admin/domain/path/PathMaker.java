package wooteco.subway.admin.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.PathSearchType;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.exception.CanNotCreateGraphException;
import wooteco.subway.admin.exception.LineNotConnectedException;

import java.util.List;

// 가장 짧은 경로를 생성하는 책임
public class PathMaker {
    private final LineStations lineStations;
    private final Stations stations;

    public PathMaker(LineStations lineStations, Stations stations) {
        this.lineStations = lineStations;
        this.stations = stations;
    }

    public Path computeShortestPath(Long sourceStationId, Long targetStationId, PathSearchType type) {
        StationGraph pathGraph = getPathGraph(type, lineStations);
        List<Long> shortestPathStationIds = getShortestPathStationIds(pathGraph, sourceStationId, targetStationId);
        Stations shortestPathStations = this.stations.listOf(shortestPathStationIds);

        return Path.of(lineStations, shortestPathStations);
    }

    private List<Long> getShortestPathStationIds(StationGraph pathGraph, Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(pathGraph);
        try {
            return dijkstraShortestPath.getPath(sourceStationId, targetStationId).getVertexList();
        } catch (NullPointerException e) {
            throw new LineNotConnectedException();
        }
    }

    private StationGraph getPathGraph(PathSearchType type, LineStations lineStations) {
        try {
            return StationGraph.of(lineStations, type);
        } catch (IllegalArgumentException e) {
            throw new CanNotCreateGraphException();
        }
    }
}
