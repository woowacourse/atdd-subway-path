package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.Age;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.service.dto.path.PathRequestDto;
import wooteco.subway.service.dto.path.PathResponseDto;
import wooteco.subway.service.dto.station.StationResponseDto;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;
    private final LineService lineService;

    public PathService(StationService stationService, SectionService sectionService, LineService lineService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponseDto getPath(PathRequestDto pathRequestDto) {
        List<Long> stationIds = getStationIds();
        Sections sections = sectionService.findAll();

        Path path = Path.of(pathRequestDto.getSource(), pathRequestDto.getTarget(), stationIds, sections);
        Fare fare = getDefaultFare(path.getShortestPathSections());

        Age age = Age.calculateAge(pathRequestDto.getAge());
        return new PathResponseDto(toStationResponseDto(path), path.getTotalDistance(), fare.calculateFare(path.getTotalDistance(), age));
    }

    private Fare getDefaultFare(List<Section> shortestPathSections) {
        int maxExtraFare = shortestPathSections.stream()
                .mapToInt(section -> lineService.getExtraFareByLineId(section.getLineId()))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("추가 요금을 찾을 수 없습니다."));
        return new Fare(maxExtraFare);
    }

    private List<Long> getStationIds() {
        return stationService.findStations().stream()
                .map(StationResponseDto::getId)
                .collect(Collectors.toList());
    }

    private List<StationResponseDto> toStationResponseDto(Path path) {
        return path.getShortestPath().stream()
                .map(stationService::findById)
                .map(StationResponseDto::new)
                .collect(Collectors.toList());
    }
}
