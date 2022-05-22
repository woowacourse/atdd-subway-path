package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
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
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final PathFindStrategy pathFindStrategy;
    private final FareStrategy fareStrategy;

    public PathService(StationDao stationDao, LineDao lineDao, SectionDao sectionDao,
                       PathFindStrategy pathFindStrategy, FareStrategy fareStrategy) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
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
        int maxExtraFare = lineDao.findMaxExtraFareByLineId(path.getLines());

        return new PathResponse(path, fare + maxExtraFare);
    }
}
