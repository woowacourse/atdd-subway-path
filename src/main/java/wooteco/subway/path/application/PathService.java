package wooteco.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.util.DijkstraPath;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> lines = lineService.findLines();

        List<Section> allSectionFromSubway = new ArrayList<>();
        lines.stream().map(line -> line.getSections().getSections())
            .forEach(allSectionFromSubway::addAll);
        Sections sections = new Sections(allSectionFromSubway);

        DijkstraPath dijkstraPath = new DijkstraPath(sections);
        List<StationResponse> stationResponses = dijkstraPath
            .findShortestRouteToStationResponse(sourceStation, targetStation);
        int dDistance = dijkstraPath.findShortestDistance(sourceStation, targetStation);

        return new PathResponse(stationResponses, dDistance);
    }
}
