package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Route;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.response.PathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        Station sourceStation = stationDao.showById(sourceId);
        Station targetStation = stationDao.showById(targetId);

        Route route = new Route(sectionDao.findAll());
        List<Station> shortestStations = route.findShortestStation(sourceStation, targetStation);
        int shortestDistance = route.findShortestDistance(sourceStation, targetStation);

        return PathResponse.of(shortestStations, shortestDistance);
    }
}
