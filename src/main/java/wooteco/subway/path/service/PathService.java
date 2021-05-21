package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.path.domain.ShortestPathStrategy;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.domain.Stations;
import wooteco.subway.station.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(long sourceStationId, long targetStationId) {
        List<Section> sections = sectionDao.findAll();
        Stations stations = stationService.findAllStations();
        SubwayMap subwayMap = new SubwayMap(ShortestPathStrategy.DIJKSTRA, sections);

        List<Station> shortestPath = subwayMap.findShortestPath(sourceStationId, targetStationId)
                .stream()
                .map(stations::getStationById)
                .collect(Collectors.toList());
        int shortestDistance = subwayMap.findShortestDistance(sourceStationId, targetStationId);

        return new PathResponse(shortestPath, shortestDistance);
    }
}
