package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.fare.FareStrategy;
import wooteco.subway.domain.strategy.path.PathFindStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final PathFindStrategy pathFindStrategy;
    private final FareStrategy fareStrategy;

    public PathService(StationDao stationDao, SectionDao sectionDao, PathFindStrategy pathFindStrategy, FareStrategy fareStrategy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pathFindStrategy = pathFindStrategy;
        this.fareStrategy = fareStrategy;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource());
        Station target = stationDao.findById(pathRequest.getTarget());

        Sections sections = new Sections(sectionDao.findAll());

        Path path = pathFindStrategy.calculatePath(source, target, sections);
        int fare = fareStrategy.calculateFare(path.getDistance());

        return new PathResponse(path, fare);
    }
}
