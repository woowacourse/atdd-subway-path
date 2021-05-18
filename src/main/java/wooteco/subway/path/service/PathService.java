package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.controller.dto.PathResponse;
import wooteco.subway.path.domain.BaseShortestPathAlgorithm;
import wooteco.subway.path.domain.DijkstraShortestPathAlgorithm;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.WeightedGraph;
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

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        final List<Line> lines = lineDao.findAll();
        final Station source = stationDao.findById(sourceId);
        final Station target = stationDao.findById(targetId);

        final BaseShortestPathAlgorithm shortestPathAlgorithm = new DijkstraShortestPathAlgorithm(WeightedGraph.of(lines));
        final Path shortestPath = shortestPathAlgorithm.findShortestPath(source, target);
        return PathResponse.of(shortestPath);
    }
}
