package wooteco.subway.admin.service;

import java.util.List;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathEdge;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NoExistPathTypeException;

@Service
public class PathService {
    private final LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    public PathResponse searchShortestPath(String source, String target,
        String type) {
        Station sourceStation = lineService.findStationByName(source);
        Station targetStation = lineService.findStationByName(target);
        PathType pathType = findPathType(type);

        Path path = createPath(pathType);
        GraphPath<Long, PathEdge> shortestPath = path.searchShortestPath(sourceStation,
            targetStation);
        List<Long> stationIds = shortestPath.getVertexList();
        return new PathResponse(StationResponse.listOf(toStations(stationIds)),
            path.calculateDistance(shortestPath), path.calculateDuration(shortestPath));
    }

    private PathType findPathType(String type) {
        try {
            return PathType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new NoExistPathTypeException("존재하지 않는 경로 타입입니다.");
        }
    }

    private Path createPath(PathType pathType) {
        Path path = new Path();

        List<Station> stations = lineService.showStations();
        path.addVertexes(stations);
        List<Line> lines = lineService.showLines();
        path.setEdges(lines, pathType);

        return path;
    }

    private List<Station> toStations(List<Long> stationIds) {
        return lineService.findAllStationsByIds(stationIds);
    }
}
