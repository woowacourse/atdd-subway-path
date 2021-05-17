package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }


    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);

        Path path = new Path(lines);
        List<Station> shortestPath = path.getShortestPath(sourceStation, targetStation);
        List<StationResponse> shortestPathResponse = StationResponse.listOf(shortestPath);
        int totalDistance = path.getTotalDistance(sourceStation, targetStation);

        return new PathResponse(shortestPathResponse, totalDistance);
    }
}
