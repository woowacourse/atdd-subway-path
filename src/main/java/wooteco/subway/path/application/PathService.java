package wooteco.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.infrastructure.ShortestPath;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final ShortestPath shortestPath;

    public PathService(SectionDao sectionDao, StationDao stationDao,
                       ShortestPath shortestPath) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.shortestPath = shortestPath;
    }

    public PathResponse findPaths(long sourceId, long targetId) {
        List<Section> sections = sectionDao.findAll();

        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);
        Path path = shortestPath.getPath(source, target, sections);

        return new PathResponse(path);
    }
}