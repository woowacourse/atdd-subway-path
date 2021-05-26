package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.StationNonexistenceException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.Lines;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findShortestPaths(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationDao.findById(sourceStationId)
                .orElseThrow(StationNonexistenceException::new);
        Station targetStation = stationDao.findById(targetStationId)
                .orElseThrow(StationNonexistenceException::new);

        SubwayMap subwayMap = new SubwayMap(new Lines(lineDao.findAll()));
        List<Station> stations = subwayMap.shortestPath(sourceStation, targetStation);
        int distance = subwayMap.distance(sourceStation, targetStation);

        return new PathResponse(StationResponse.listOf(stations), distance);
    }
}
