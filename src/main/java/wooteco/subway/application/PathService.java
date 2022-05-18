package wooteco.subway.application;

import org.springframework.stereotype.Service;
import wooteco.subway.Infrastructure.SectionDao;
import wooteco.subway.Infrastructure.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.constant.NotExistException;
import wooteco.subway.exception.constant.NotExistException.Which;

import java.util.List;

@Service
public class PathService {

    private final FareCalculator fareCalculator;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(FareCalculator fareCalculator, SectionDao sectionDao, StationDao stationDao) {
        this.fareCalculator = fareCalculator;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long from, Long to) {
        validateExistStations(from, to);
        List<Section> sections = sectionDao.findAll();
        PathFinder pathFinder = new PathFinder(sections);
        List<Long> path = pathFinder.findPath(from, to);
        int distance = pathFinder.findDistance(from, to);
        int fare = fareCalculator.calculateFare(distance);
        List<Station> stations = stationDao.findByIdIn(path);

        return new PathResponse(stations, distance, fare);
    }

    private void validateExistStations(Long from, Long to) {
        if (!stationDao.existById(from) || !stationDao.existById(to)) {
            throw new NotExistException(Which.STATION);
        }
    }
}
