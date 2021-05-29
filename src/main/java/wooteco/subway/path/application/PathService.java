package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final ShortestPath shortestPath;

    public PathService(StationDao stationDao, SectionDao sectionDao, ShortestPath shortestPath) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.shortestPath = shortestPath;
    }

    public void resetPathGraphBySections() {
        shortestPath.resetGraph(sectionDao.findAll());
    }

    public PathResponse findPaths(long sourceId, long targetId) {
        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);
        Path path = shortestPath.getPath(source, target);

        return new PathResponse(path);
    }
}