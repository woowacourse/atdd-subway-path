package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Paths;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayGraph;
import wooteco.subway.dto.PathsResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathsResponse createPaths(final Long sourceStationId, final Long targetStationId, final int age) {
        Station source = stationDao.findById(sourceStationId);
        Station target = stationDao.findById(targetStationId);
        return createPathsResponse(source, target);
    }

    private PathsResponse createPathsResponse(final Station source, final Station target) {
        SubwayGraph subwayGraph = initSubwayGraph();
        Paths paths = subwayGraph.createPathsResult(source, target);
        return PathsResponse.of(paths);
    }

    private SubwayGraph initSubwayGraph() {
        Sections sections = new Sections(sectionDao.findAll());
        return new SubwayGraph(sections);
    }
}
