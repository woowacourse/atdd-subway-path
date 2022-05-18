package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathGraph;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        List<Line> lines = lineDao.findAll();
        PathGraph pathGraph = new PathGraph(lines);

        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        Path path = pathGraph.findShortestPath(sourceStation, targetStation);

        return PathResponse.of(path);
    }

    private Station findStationById(Long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException("존재하지 않는 id입니다.");
        }
        return stationDao.findById(id);
    }
}
