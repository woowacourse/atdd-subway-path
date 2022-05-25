package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFactory;
import wooteco.subway.service.dto.path.PathRequestDto;
import wooteco.subway.service.dto.path.PathResponse;
import wooteco.subway.service.dto.station.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionService sectionService;
    private final PathFactory pathFactory;
    private final FareCalculator fareCalculator;

    public PathService(LineService lineService, StationService stationService, SectionService sectionService, PathFactory pathFactory, FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.pathFactory = pathFactory;
        this.fareCalculator = fareCalculator;
    }

    @Transactional(readOnly = true)
    public PathResponse getPath(PathRequestDto pathRequestDto) {
        Path path = makePath(pathRequestDto);
        int fare = makeFare(pathRequestDto, path);
        List<StationResponse> stations = toStationResponse(path);
        return new PathResponse(stations, path.getTotalDistance(), fare);
    }

    private Path makePath(PathRequestDto pathRequestDto) {
        List<Long> stationIds = getStationIds();
        Sections sections = sectionService.findAll();
        return pathFactory.makePath(pathRequestDto.getSource(), pathRequestDto.getTarget(), stationIds, sections);
    }

    private List<Long> getStationIds() {
        List<Long> stationIds = stationService.findStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        return stationIds;
    }

    private int makeFare(PathRequestDto pathRequestDto, Path path) {
        return fareCalculator.makeFare(path.getTotalDistance(), findMaxExtraFare(path), pathRequestDto.getAge());
    }

    private int findMaxExtraFare(Path path) {
        return path.getShortestSections().stream()
                .mapToInt(section -> lineService.getExtraFareById(section.getLineId()))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("추가 요금을 찾을 수 없습니다."));
    }

    private List<StationResponse> toStationResponse(Path path) {
        Map<Long, Station> stations = stationService.findAll()
                .stream()
                .collect(Collectors.toMap(Station::getId, station -> station));

        return path.getShortestPath().stream()
                .map(stations::get)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
