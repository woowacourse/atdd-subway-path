package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Graph;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        List<Line> lines = lineDao.findAll();
        Graph graph = new Graph(lines);
        Path path = graph.shortestPath(stationDao.findById(sourceId), stationDao.findById(targetId));
        return new PathResponse(StationResponse.listOf(path.stations()), (int) path.distance());
    }
}
