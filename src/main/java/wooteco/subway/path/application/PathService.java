package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.DijkstraStationMap;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.StationMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse shortestPath(Long sourceId, Long targetId) {
        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);
        StationMap stationMap = new DijkstraStationMap(lineDao.findAll());

        Path path = stationMap.pathOf(source, target);
        return PathResponse.of(path);
    }
}
