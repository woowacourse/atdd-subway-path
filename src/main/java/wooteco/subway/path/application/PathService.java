package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.dao.SectionTable;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
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


        Path path = new Path(sectionTables);
        List<Long> shortestStationIds = path.getShortestStations(sourceStationId, targetStationId);
        int shortestDistance = path.getShortestDistance(sourceStationId, targetStationId);
        List<Station> shortestStations = stationService.findStationByIds(shortestStationIds);
        return new PathResponse(StationResponse.listOf(shortestStations), shortestDistance);
    }
}
