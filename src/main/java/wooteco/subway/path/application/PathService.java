package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.dao.SectionTable;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final SectionDao sectionDao;

    public PathService(StationService stationService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }


    public PathResponse searchPath(Long sourceStationId, Long targetStationId) {
        List<SectionTable> sectionTables = sectionDao.findAll();


        PathFinder pathFinder = new PathFinder(sectionTables);
        List<Long> shortestStationIds = pathFinder.getShortestStations(sourceStationId, targetStationId);
        int shortestDistance = pathFinder.getShortestDistance(sourceStationId, targetStationId);
        List<Station> shortestStations = stationService.findStationByIds(shortestStationIds);
        return new PathResponse(StationResponse.listOf(shortestStations), shortestDistance);
    }
}
