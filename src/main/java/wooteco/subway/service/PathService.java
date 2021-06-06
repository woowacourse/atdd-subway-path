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

    private final StationDao stationDao;
    private final Route route;

    public PathService(StationDao stationDao, Route route) {
        this.stationDao = stationDao;
        this.route = route;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        Station sourceStation = stationDao.findById(sourceId);
        Station targetStation = stationDao.findById(targetId);

        List<Station> shortestStations = route.findShortestStation(sourceStation, targetStation);
        int shortestDistance = route.findShortestDistance(sourceStation, targetStation);

        return PathResponse.of(shortestStations, shortestDistance);
    }
}
