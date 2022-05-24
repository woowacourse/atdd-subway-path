package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.AgeDiscount;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.fare.strategy.fare.DefaultFareStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse getPath(long sourceId, long targetId, int age) {
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.getShortestPath(getStationById(sourceId), getStationById(targetId),
                new Sections(sectionDao.findAll()));

        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(), AgeDiscount.findAgeDiscount(age));
        return PathResponse.from(path,
                fareCalculator.calculate(path.getDistance(), path.getMaxExtraFare(lineDao.findAll())));
    }

    private Station getStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
    }
}
