package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
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
        List<Long> stationIds = getStationIds();
        Sections sections = sectionService.findAll();
        Path path = new Path(pathRequestDto.getSource(), pathRequestDto.getTarget(), stationIds, sections);
        List<Section> shortestSections = path.getShortestSections();
        System.out.println("구간 구하기 : " + shortestSections);  // ㅎㅇ
        System.out.println(lineService.getExtraFareById(1L));
        int maxExtraFare = shortestSections.stream()
                .mapToInt(section -> lineService.getExtraFareById(section.getLineId()))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("추가 요금을 찾을 수 없습니다."));
        Fare fare = new Fare(maxExtraFare);
        List<StationResponse> stations = toStationResponse(path);
        return new PathResponse(stations, path.getTotalDistance(), fare.calculateFare(path.getTotalDistance()));
    }

    private List<Long> getStationIds() {
        List<Long> stationIds = stationService.findStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        return stationIds;
    }

    private List<StationResponse> toStationResponse(Path path) {
        List<StationResponse> stations = stationService.findAll().stream()
                .filter(it -> path.getShortestPath().contains(it.getId()))
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return stations;
    }
}
