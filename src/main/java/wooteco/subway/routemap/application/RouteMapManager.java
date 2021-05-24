package wooteco.subway.routemap.application;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.routemap.infrastructure.RouteMap;
import wooteco.subway.station.domain.Station;

@Service
public class RouteMapManager {

    private final RouteMap routeMap;

    public RouteMapManager(RouteMap routeMap) {
        this.routeMap = routeMap;
    }

    public void updateMap(Lines lines) {
        updateStations(lines.toDistinctStations());
        updateSections(lines);
    }

    public void addStation(Station station) {
        routeMap.addStation(station);
    }

    public void updateStations(Set<Station> stations) {
        routeMap.updateStations(stations);
    }

    public void updateSections(Lines lines) {
        routeMap.updateSections(lines);
    }
}
