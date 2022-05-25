package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.DiscountPolicy;
import wooteco.subway.domain.fare.FarePolicy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.service.dto.request.PathServiceRequest;
import wooteco.subway.service.dto.response.PathServiceResponse;

@Service
public class SpringPathService implements PathService {

    private final SectionService sectionService;
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public SpringPathService(SectionService sectionService, StationService stationService, LineService lineService,
                             PathFinder pathFinder) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    @Override
    public PathServiceResponse findPath(PathServiceRequest pathServiceRequest) {
        final Path path = findShortestPath(pathServiceRequest);
        final long extraFare = findMaximumExtraFare(path.getLineIds());

        final long fare = FarePolicy.calculateByDistance(path.getDistance(), extraFare);
        final long discountedFare = DiscountPolicy.calculateFareByAge(fare, pathServiceRequest.getAge());

        return new PathServiceResponse(path.getStations(), path.getDistance(), discountedFare);
    }

    private Path findShortestPath(PathServiceRequest pathServiceRequest) {
        final Sections sections = new Sections(findAllSections());
        final Station source = stationService.findById(pathServiceRequest.getSource());
        final Station target = stationService.findById(pathServiceRequest.getTarget());

        return pathFinder.searchShortestPath(sections, source, target);
    }

    private List<Section> findAllSections() {
        return sectionService.findAll()
                .stream()
                .flatMap(sections -> sections.getSections().stream())
                .collect(Collectors.toList());
    }

    private long findMaximumExtraFare(List<Long> lineIds) {
        return lineService.findAll()
                .stream()
                .filter(line -> lineIds.contains(line.getId()))
                .mapToLong(Line::getExtraFare)
                .max()
                .orElse(0L);
    }
}
