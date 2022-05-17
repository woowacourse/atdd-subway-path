package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.route.Route;
import wooteco.subway.domain.route.Router;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.dto.DtoAssembler;
import wooteco.subway.service.dto.RouteResponse;

@Service
public class RouteService {

    private final SectionRepository sectionRepository;
    private final Router router;

    public RouteService(SectionRepository sectionRepository, Router router) {
        this.sectionRepository = sectionRepository;
        this.router = router;
    }

    public RouteResponse findRoute(Long sourceId, Long targetId, int age) {
        final List<Section> sections = sectionRepository.findSections();
        final Station sourceStation = sectionRepository.findStationById(sourceId);
        final Station targetStation = sectionRepository.findStationById(targetId);

        final Route shortestRoute = router.findShortestRoute(sections, sourceStation, targetStation);

        return DtoAssembler.routeResponse(shortestRoute);
    }
}

