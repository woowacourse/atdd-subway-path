package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.exception.notfound.StationNotFoundException;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.PathResponse;
import wooteco.subway.web.dto.StationResponse;

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
        final Station sourceStation =
            stationDao.findById(sourceStationId).orElseThrow(StationNotFoundException::new);
        final Station targetStation =
            stationDao.findById(targetStationId).orElseThrow(StationNotFoundException::new);

        Path path = new Path(new Lines(lineDao.findAll()));
        final List<Station> stations = path.shortestPath(sourceStation, targetStation);
        final int distance = path.distance(sourceStation, targetStation);

        return new PathResponse(StationResponse.listOf(stations), distance);
    }
}
