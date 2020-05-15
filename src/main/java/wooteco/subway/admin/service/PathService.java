package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Graph;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.GraphResponse;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.PathType;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundStationException;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(PathRequest request) {
        Long sourceId = stationService.findIdByName(request.getSourceName());
        Long targetId = stationService.findIdByName(request.getTargetName());
        List<Line> lines = lineService.findAll();

        Graph graph = Graph.of(lines, PathType.of(request.getType()));
        GraphResponse graphResponse = graph.findPath(sourceId, targetId);

        List<Long> path = graphResponse.getPath();

        List<StationResponse> stationResponses = StationResponse.listOf(
            stationService.findAllById(path));
        int totalDistance = graphResponse.getTotalDistance();
        int totalDuration = graphResponse.getTotalDuration();

        List<StationResponse> sortedStationResponses = sort(path, stationResponses);

        return new PathResponse(sortedStationResponses, totalDistance, totalDuration);
    }

    private List<StationResponse> sort(List<Long> path, List<StationResponse> stationResponses) {
        List<StationResponse> result = new ArrayList<>();

        for (Long stationId : path) {
            StationResponse response = stationResponses.stream()
                .filter(stationResponse -> stationResponse.getId().equals(stationId))
                .findAny().orElseThrow(NotFoundStationException::new);
            result.add(response);
        }
        return result;
    }
}
