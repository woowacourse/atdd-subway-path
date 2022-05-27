package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.secion.Sections;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse createPath(final Long sourceStationId, final Long targetStationId, final int age) {
        Station source = stationDao.findById(sourceStationId);
        Station target = stationDao.findById(targetStationId);
        return createPathResponse(source, target, age);
    }

    private PathResponse createPathResponse(final Station source, final Station target, final int age) {
        Path path = findPath(source, target);
        int fare = calculateFare(age, path);
        return PathResponse.of(path, fare);
    }

    private Path findPath(Station source, Station target) {
        PathFinder pathFinder = initPathFinder(source, target);
        return pathFinder.getPath();
    }

    private PathFinder initPathFinder(final Station source, final Station target) {
        Sections sections = new Sections(sectionDao.findAll());
        return PathFinder.init(sections, source, target);
    }

    private int calculateFare(int age, Path path) {
        Lines lines = new Lines(lineDao.findAll());
        FareCalculator fareCalculator = new FareCalculator(path.getLineIds());
        return fareCalculator.calculateFare(age, path.getDistance(), lines);
    }

}
