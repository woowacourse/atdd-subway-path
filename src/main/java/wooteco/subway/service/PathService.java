package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.ShortestPathAlgorithm;
import wooteco.subway.domain.DijkstraShortestPathAlgorithm;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.WeightedGraph;
import wooteco.subway.domain.Station;
import wooteco.subway.web.response.PathResponse;

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

        final ShortestPathAlgorithm shortestPathAlgorithm = new DijkstraShortestPathAlgorithm(WeightedGraph.of(lines));
        final Path shortestPath = shortestPathAlgorithm.findShortestPath(source, target);
        return PathResponse.of(shortestPath);
    }
}
