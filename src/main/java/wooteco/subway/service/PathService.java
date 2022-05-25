package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.AgeDiscountPolicy;
import wooteco.subway.domain.fare.FarePolicy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFindStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final PathFindStrategy pathFindStrategy;

    public PathService(LineDao lineDao, StationDao stationDao, PathFindStrategy pathFindStrategy) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.pathFindStrategy = pathFindStrategy;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Line> lines = lineDao.findAll();
        Station sourceStation = findStationById(pathRequest.getSource());
        Station targetStation = findStationById(pathRequest.getTarget());
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(pathRequest.getAge());

        Path path = pathFindStrategy.findPath(lines, sourceStation, targetStation);
        int fare = FarePolicy.calculateFare(path.getDistance(), path.getUsedLines().findMaxExtraFare());

        return PathResponse.of(path, ageDiscountPolicy.discount(fare));
    }

    private Station findStationById(Long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException("존재하지 않는 id입니다.");
        }
        return stationDao.findById(id);
    }
}
