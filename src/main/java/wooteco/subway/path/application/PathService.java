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

        List<Section> sectionsFromList = new ArrayList<>();
        for (Line line : lines) {
            sectionsFromList.addAll(line.getSections().getSections());
        }
        Sections sections = new Sections(sectionsFromList);

        DijkstraPath dijkstraPath = new DijkstraPath(sourceStation, targetStation, sections);
        List<StationResponse> stationResponses = dijkstraPath.findShortestPath(stationService);
        int dDistance = dijkstraPath.findShortestDistance();

        return new PathResponse(stationResponses, dDistance);
    }
}
