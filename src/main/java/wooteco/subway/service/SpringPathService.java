package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.request.PathServiceRequest;
import wooteco.subway.service.dto.response.PathServiceResponse;

@Service
public class SpringPathService implements PathService {

    private final SectionService sectionService;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public SpringPathService(SectionService sectionService, StationService stationService, PathFinder pathFinder,
                             FareCalculator fareCalculator) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    @Override
    public PathServiceResponse findPath(PathServiceRequest pathServiceRequest) {
        final List<Section> allSections = findAllSections();

        final Station source = stationService.findById(pathServiceRequest.getSource());
        final Station target = stationService.findById(pathServiceRequest.getTarget());

        final Path path = pathFinder.searchShortestPath(allSections, source, target);
        final Long fare = fareCalculator.calculate(path.getDistance());

        return new PathServiceResponse(path.getStations(), path.getDistance(), fare);
    }

    private List<Section> findAllSections() {
        return sectionService.findAll()
                .stream()
                .flatMap(sections -> sections.getSections().stream())
                .collect(Collectors.toList());
    }
}
