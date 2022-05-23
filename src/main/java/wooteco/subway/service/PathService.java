package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.service.dto.path.PathRequestDto;
import wooteco.subway.service.dto.path.PathResponse;
import wooteco.subway.service.dto.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathResponse getPath(PathRequestDto pathRequestDto) {
        List<Long> stationIds = stationService.findStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        Sections sections = sectionService.findAll();
        Path path = new Path(pathRequestDto.getSource(), pathRequestDto.getTarget(), stationIds, sections);
        Fare fare = new Fare();
        List<StationResponse> stations = path.getShortestPath().stream()
                .map(stationService::findById)
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return new PathResponse(stations, path.getTotalDistance(), fare.calculateFare(path.getTotalDistance()));
    }
}
