package wooteco.subway.routemap.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.routemap.infrastructure.RouteMap;

@Service
public class RouteMapManager {

    private final RouteMap routeMap;

    public RouteMapManager(RouteMap routeMap) {
        this.routeMap = routeMap;
    }

    public void updateMap(Lines lines) {
        updateStations(lines);
        updateSections(lines);
    }

    public void updateStations(Lines lines) {
        routeMap.updateStations(lines);
    }

    public void updateSections(Lines lines) {
        routeMap.updateSections(lines);
    }
}
