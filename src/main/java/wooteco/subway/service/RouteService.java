package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.graph.Cashier;
import wooteco.subway.domain.graph.Route;
import wooteco.subway.domain.graph.SubwayGraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.dto.DtoAssembler;
import wooteco.subway.service.dto.RouteResponse;

@Service
public class RouteService {

    private final SectionRepository sectionRepository;
    private final Cashier cashier;

    public RouteService(SectionRepository sectionRepository, Cashier cashier) {
        this.sectionRepository = sectionRepository;
        this.cashier = cashier;
    }

    public RouteResponse findRoute(Long sourceStationId, Long targetStationId, int age) {
        final List<Section> sections = sectionRepository.findSections();
        SubwayGraph subwayGraph = new SubwayGraph(sections);

        Route shortestRoute = findShortestRoute(subwayGraph, sourceStationId, targetStationId);
        final Long fare = cashier.calculateFare(shortestRoute.getDistance());
        return DtoAssembler.routeResponse(shortestRoute, fare);
    }

    private Route findShortestRoute(SubwayGraph subwayGraph, Long sourceStationId, Long targetStationId) {
        final Station sourceStation = sectionRepository.findStationById(sourceStationId);
        final Station targetStation = sectionRepository.findStationById(targetStationId);
        return subwayGraph.calculateShortestRoute(sourceStation, targetStation);
    }
}

