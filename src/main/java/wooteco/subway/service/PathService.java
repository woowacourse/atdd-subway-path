package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.path.FindPathStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.path.PathFindRequest;
import wooteco.subway.dto.path.PathFindResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final FindPathStrategy findPathStrategy;

    public PathService(final SectionDao sectionDao, final StationDao stationDao,
                       final FindPathStrategy findPathStrategy) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.findPathStrategy = findPathStrategy;
    }

    public PathFindResponse findPath(final PathFindRequest pathFindRequest) {
        Sections sections = new Sections(sectionDao.findAll());
        Station source = stationDao.findById(pathFindRequest.getSource());
        Station target = stationDao.findById(pathFindRequest.getTarget());
        Path path = findPathStrategy.findPath(source, target, sections);
        return PathFindResponse.from(path, path.calculateFare(pathFindRequest.getAge()));
    }
}
