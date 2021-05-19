package wooteco.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.path.domain.Route;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        Station sourceStation = stationDao.findById(sourceId);
        Station targetStation = stationDao.findById(targetId);
        Route route = new Route(sectionDao.findAll());
        List<Station> shortestStation = route.findShortestStation(sourceStation, targetStation);
        int shortestDistance = route.findShortestDistance(sourceStation, targetStation);
        return new PathResponse(
            shortestStation.stream().map(StationResponse::of).collect(Collectors.toList()),
            shortestDistance
        );
    }
}
