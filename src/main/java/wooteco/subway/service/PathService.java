package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineDao lineDao;

    public PathService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public PathResponse findShortestPaths(Long sourceStationId, Long targetStationId) {
        final Lines lines = new Lines(lineDao.findAll());
        final Path path = new Path(lines);
        final List<Station> stations = path.shortestPath(sourceStationId, targetStationId);
        final int distance = path.distance(sourceStationId, targetStationId);

        return new PathResponse(stations, distance);
    }
}
