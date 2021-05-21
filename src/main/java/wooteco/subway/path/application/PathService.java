package wooteco.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.infrastructure.ShortestPath;
import wooteco.subway.path.infrastructure.ShortestPathWithDijkstra;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPaths(long sourceId, long targetId) {
        List<Section> sections = sectionDao.findAll();

        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);

        Path path = new Path(new ShortestPathWithDijkstra(sections), source, target);
        return new PathResponse(path);
    }
}
