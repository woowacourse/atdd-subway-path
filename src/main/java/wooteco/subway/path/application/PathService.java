package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
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
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final Station source = stationService.findStationById(sourceId);
        final Station target = stationService.findStationById(targetId);

        final Path shortest = new Path(lineService.findLines());

        final List<Station> route = shortest.route(source, target);
        final int totalDistance = shortest.distance(source, target);

        return new PathResponse(StationResponse.listOf(route), totalDistance);
    }
}
