package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.*;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareBuilder;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathBuilder;
import wooteco.subway.service.dto.path.PathRequestDto;
import wooteco.subway.service.dto.path.PathResponse;
import wooteco.subway.service.dto.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(LineService lineService, StationService stationService, SectionService sectionService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathResponse getPath(PathRequestDto pathRequestDto) {
        Path path = makePath(pathRequestDto);
        Fare fare = makeFare(pathRequestDto, path);
        List<StationResponse> stations = toStationResponse(path);
        return new PathResponse(stations, path.getTotalDistance(), fare.getFare());
    }

    private Path makePath(PathRequestDto pathRequestDto) {
        PathBuilder pathBuilder = new PathBuilder();
        List<Long> stationIds = getStationIds();
        Sections sections = sectionService.findAll();
        return pathBuilder.makePath(pathRequestDto.getSource(), pathRequestDto.getTarget(), stationIds, sections);
    }

    private List<Long> getStationIds() {
        List<Long> stationIds = stationService.findStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        return stationIds;
    }

    private Fare makeFare(PathRequestDto pathRequestDto, Path path) {
        FareBuilder fareBuilder = new FareBuilder();
        return fareBuilder.makeFare(path.getTotalDistance(), findMaxExtraFare(path), pathRequestDto.getAge());
    }

    private int findMaxExtraFare(Path path) {
        int maxExtraFare = path.getShortestSections().stream()
                .mapToInt(section -> lineService.getExtraFareById(section.getLineId()))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("추가 요금을 찾을 수 없습니다."));
        return maxExtraFare;
    }

    private List<StationResponse> toStationResponse(Path path) {
        List<StationResponse> stations = stationService.findAll().stream()
                .filter(it -> path.getShortestPath().contains(it.getId()))
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return stations;
    }
}
