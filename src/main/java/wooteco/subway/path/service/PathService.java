package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.JgraphtGraph;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.path.domain.StationMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(Long fromStationId, Long toStationId) {
        List<Line> all = lineDao.findAll();
        Station from = stationDao.findById(fromStationId);
        Station to = stationDao.findById(toStationId);
        StationMap stationMap = new StationMap(new JgraphtGraph<>(), all);

        ShortestPath shortedPath = stationMap.findShortedPath(from, to);
        return PathResponse.of(shortedPath);
    }
}
