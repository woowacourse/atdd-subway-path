package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayGraph;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse createPath(final Long sourceStationId, final Long targetStationId, final int age) {
        Station source = stationDao.findById(sourceStationId);
        Station target = stationDao.findById(targetStationId);
        return createPathResponse(source, target, age);
    }

    private PathResponse createPathResponse(final Station source, final Station target, final int age) {
        Path path = findPath(source, target, age);
        return PathResponse.from(path);
    }

    private Path findPath(final Station source, final Station target, final int age) {
        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(new Sections(sectionDao.findAll()));
        return subwayGraph.findShortestPath(source, target, age);
    }

}
