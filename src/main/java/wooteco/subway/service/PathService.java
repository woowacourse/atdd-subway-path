package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.fare.strategy.fare.DefaultFareStrategy;
import wooteco.subway.domain.fare.strategy.utils.AgeDiscountStrategyFinder;
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

    public PathResponse getPath(Long sourceId, Long targetId, int age) {
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.getShortestPath(getStationById(sourceId), getStationById(targetId),
                new Sections(sectionDao.findAll()));
        List<Long> passedLineIds = path.findPassedLineIds();

        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(),
                AgeDiscountStrategyFinder.findStrategy(age));

        int maxExtraFare = lineDao.getMaxFareByLineIds(passedLineIds);

        return PathResponse.from(
                path,
                fareCalculator.calculate(path.getDistance(), maxExtraFare));
    }

    private Station getStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
    }
}
