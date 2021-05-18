package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.DistanceMap;
import wooteco.subway.path.domain.ShortestPathMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final DistanceMap distanceMap;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.distanceMap = new DistanceMap();
    }

    public PathResponse shortestPath(Long sourceId, Long targetId) {
        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);

        ShortestPathMap shortestPathMap = distanceMap.toShortestPathMap(lineDao.findAll());

        List<Station> path = shortestPathMap.path(source, target);
        int distance = shortestPathMap.distance(source, target);
        return PathResponse.of(path, distance);
    }
}
