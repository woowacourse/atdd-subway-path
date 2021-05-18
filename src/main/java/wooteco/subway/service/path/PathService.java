package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.BaseShortestPathAlgorithm;
import wooteco.subway.domain.path.DijkstraShortestPathAlgorithm;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.WeightedGraph;
import wooteco.subway.domain.station.Station;
import wooteco.subway.web.path.dto.PathResponse;

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

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        final List<Line> lines = lineDao.findAll();
        final Station source = stationDao.findById(sourceId);
        final Station target = stationDao.findById(targetId);

        final BaseShortestPathAlgorithm shortestPathAlgorithm = new DijkstraShortestPathAlgorithm(WeightedGraph.of(lines));
        final Path shortestPath = shortestPathAlgorithm.findShortestPath(source, target);
        return PathResponse.of(shortestPath);
    }
}
